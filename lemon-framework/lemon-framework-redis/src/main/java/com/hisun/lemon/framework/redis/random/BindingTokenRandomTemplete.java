package com.hisun.lemon.framework.redis.random;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.hisun.lemon.common.exception.ErrorMsgCode;
import com.hisun.lemon.common.exception.LemonException;
import com.hisun.lemon.common.utils.JudgeUtils;
import com.hisun.lemon.framework.utils.LemonUtils;
import com.hisun.lemon.framework.utils.RandomTemplete;

/**
 * 绑定token随机数，实际与session绑定
 * @author yuzhou
 * @date 2017年9月8日
 * @time 下午5:08:00
 *
 */
public class BindingTokenRandomTemplete implements RandomTemplete{
    private static final Logger logger = LoggerFactory.getLogger(BindingTokenRandomTemplete.class);
    
    private static final String REDIS_KEY_PREFIX = "random.";
    private StringRedisTemplate stringRedisTemplate;
    
    public BindingTokenRandomTemplete(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }
    
    @Override
    public String apply(String key, int leaseTime, RandomType randomType, int length) {
        if(JudgeUtils.isNull(LemonUtils.getToken())) {
            LemonException.throwLemonException(ErrorMsgCode.SYS_ERROR.getMsgCd(), "Token is null during acquire random.");
        }
        String random = genRandom(randomType, length);
        stringRedisTemplate.boundValueOps(resolveRandomKey(key)).set(random, leaseTime, TimeUnit.MILLISECONDS);
        if(logger.isDebugEnabled()) {
            logger.debug("apply binding token random {} , key is {}", random, resolveRandomKey(key));
        }
        return random;
    }

    @Override
    public boolean validateOnce(String key, String random) {
        String randomFromRedis = acquireOnce(key);
        if(JudgeUtils.equals(random, randomFromRedis)) {
            return true;
        }
        return false;
    }

    @Override
    public String acquireOnce(String key) {
        if(JudgeUtils.isNull(LemonUtils.getToken())) {
            LemonException.throwLemonException(ErrorMsgCode.SYS_ERROR.getMsgCd(), "Token is null during acquire random.");
        }
        String randomFromRedis = stringRedisTemplate.boundValueOps(resolveRandomKey(key)).getAndSet("");
        stringRedisTemplate.delete(resolveRandomKey(key));
        if(logger.isDebugEnabled()) {
            logger.debug("acquire once binding token random {} , key is {}", randomFromRedis, resolveRandomKey(key));
        }
        return randomFromRedis;
    }
    
    private String resolveRandomKey(String key) {
        StringBuilder keyBuilder = new StringBuilder(REDIS_KEY_PREFIX);
        keyBuilder.append(LemonUtils.getToken()).append(".").append(key);
        return keyBuilder.toString();
    }
    
}
