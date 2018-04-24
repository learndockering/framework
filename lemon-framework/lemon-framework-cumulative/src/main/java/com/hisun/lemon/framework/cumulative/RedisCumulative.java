package com.hisun.lemon.framework.cumulative;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.hisun.lemon.common.exception.ErrorMsgCode;
import com.hisun.lemon.common.exception.LemonException;
import com.hisun.lemon.common.utils.JudgeUtils;
import com.hisun.lemon.common.utils.ResourceUtils;
import com.hisun.lemon.framework.redis.serializer.LongRedisSerializer;

/**
 * Redis 实现累计
 * @author yuzhou
 * @date 2017年7月20日
 * @time 下午3:56:10
 *
 */
public class RedisCumulative implements Cumulative {
    private static final Logger logger = LoggerFactory.getLogger(RedisCumulative.class);
    
    public static final String HASH_KEY_PREFIX = "CUMULATIVE.";
    public static final String REDIS_CUMULATIVE_LUA_FILE_PATH = "lua" + ResourceUtils.JAR_PACKAGE_PATH_SEPARATOR + "cumulative.lua";;

    private String script;
    private RedisTemplate<String ,String> redisTemplate;
    
    /**
     * @param redisTemplate
     */
    public RedisCumulative(RedisTemplate<String ,String> redisTemplate) {
        this.redisTemplate = redisTemplate;
        try {
            script = ResourceUtils.getFileContent(REDIS_CUMULATIVE_LUA_FILE_PATH);
            if(logger.isInfoEnabled()) {
                logger.info("loaded cumulative script:");
                logger.info("{}", script);
            }
        } catch (IOException e) {
            LemonException.throwLemonException(e);
        }
    }
    
    @Override
    public void countByDay(String key, Dimension... dimensions) {
        if(JudgeUtils.isBlank(key) || null == dimensions || dimensions.length <= 0) {
            LemonException.throwLemonException(ErrorMsgCode.CUMULATIVE_ERROR.getMsgCd(), "param key or dimensions is null.");
        }
        List<String> keys = new ArrayList<>();
        keys.add(getKey(HASH_KEY_PREFIX, key, CumulativeMode.DAY));
        keys.add("");
        List<String> args = new ArrayList<>();
        args.add(CumulativeMode.DAY.getMode());
        args.add("");
        for(Dimension d : dimensions) {
            keys.add(d.getKey());
            args.add(d.getValue());
        }
        Object[] argsArray = args.toArray(new String[]{});
        if(logger.isDebugEnabled()) {
            logger.debug("redis day cumulative with keys {} ~~~ args {}", keys, argsArray);
        }
        Long rst = this.redisTemplate.execute(new DefaultRedisScript<Long>(this.script, Long.class),
            new StringRedisSerializer(), new LongRedisSerializer(), keys, argsArray);
        if(rst != 0) {
            LemonException.throwLemonException(ErrorMsgCode.CUMULATIVE_ERROR.getMsgCd(), 
                "redis day cumulative occured error, keys "+keys + ", args "+ argsArray);
        }
    }

    @Override
    public void countByMonth(String key, Dimension... dimensions) {
        if(JudgeUtils.isBlank(key) || null == dimensions || dimensions.length <= 0) {
            LemonException.throwLemonException(ErrorMsgCode.CUMULATIVE_ERROR.getMsgCd(), "param key or dimensions is null.");
        }
        List<String> keys = new ArrayList<>();
        keys.add("");
        keys.add(getKey(HASH_KEY_PREFIX, key, CumulativeMode.MONTH));
        List<String> args = new ArrayList<>();
        args.add(CumulativeMode.MONTH.getMode());
        args.add("");
        for(Dimension d : dimensions) {
            keys.add(d.getKey());
            args.add(d.getValue());
        }
        Object[] argsArray = args.toArray(new String[]{});
        if(logger.isDebugEnabled()) {
            logger.debug("redis month cumulative with keys {} ~~~ args {}", keys, argsArray);
        }
        Long rst = this.redisTemplate.execute(new DefaultRedisScript<Long>(this.script, Long.class),
            new StringRedisSerializer(), new LongRedisSerializer(), keys, argsArray);
        if(rst != 0) {
            LemonException.throwLemonException(ErrorMsgCode.CUMULATIVE_ERROR.getMsgCd(), 
                "redis month cumulative occured error, keys "+keys + ", args "+ argsArray);
        }
    }

    @Override
    public void countByDayAndMonth(String key, Dimension... dimensions) {
        if(JudgeUtils.isBlank(key) || null == dimensions || dimensions.length <= 0) {
            LemonException.throwLemonException(ErrorMsgCode.CUMULATIVE_ERROR.getMsgCd(), "param key or dimensions is null.");
        }
        List<String> keys = new ArrayList<>();
        keys.add(getKey(HASH_KEY_PREFIX, key, CumulativeMode.DAY));
        keys.add(getKey(HASH_KEY_PREFIX, key, CumulativeMode.MONTH));
        List<String> args = new ArrayList<>();
        args.add(CumulativeMode.DAY_AND_MONTH.getMode());
        args.add("");
        for(Dimension d : dimensions) {
            keys.add(d.getKey());
            args.add(d.getValue());
        }
        Object[] argsArray = args.toArray(new String[]{});
        if(logger.isDebugEnabled()) {
            logger.debug("redis day and month cumulative with keys {} ~~~ args {}", keys, argsArray);
        }
        Long rst = this.redisTemplate.execute(new DefaultRedisScript<Long>(this.script, Long.class),
            new StringRedisSerializer(), new LongRedisSerializer(), keys, argsArray);
        if(rst != 0) {
            LemonException.throwLemonException(ErrorMsgCode.CUMULATIVE_ERROR.getMsgCd(), 
                "redis day and month cumulative occured error, keys "+keys + ", args "+ argsArray);
        }
    }

    @Override
    public String queryByDay(String key, String dimensionKey) {
        Object obj = this.redisTemplate.opsForHash().get(getKey(HASH_KEY_PREFIX, key, CumulativeMode.DAY), dimensionKey);
        return String.valueOf(obj);
    }

    @Override
    public String queryByMonth(String key, String dimensionKey) {
        Object obj = this.redisTemplate.opsForHash().get(getKey(HASH_KEY_PREFIX, key, CumulativeMode.MONTH), dimensionKey);
        return String.valueOf(obj);
    }
    
}
