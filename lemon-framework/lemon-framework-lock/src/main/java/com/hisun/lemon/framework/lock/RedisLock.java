package com.hisun.lemon.framework.lock;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import com.hisun.lemon.common.Callback;
import com.hisun.lemon.common.utils.DateTimeUtils;
import com.hisun.lemon.common.utils.Validate;

/**
 * Redison实现 分布式锁
 * @author yuzhou
 * @date 2017年7月19日
 * @time 下午7:40:44
 *
 */
public class RedisLock implements DistributedLocker, InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(RedisLock.class);
    
    private final static String LOCKER_PREFIX = "LOCK.";
    
    private Integer defaultLeaseTime;
    
    private Integer defaultWaitTime;
    
    private RedissonClient redissonClient;
    
    /**
     * @param redissonClient    redisson client
     * @param defaultLeaseTime  默认自动解锁时间
     * @param defaultWaitTime   默认的获取锁等待时间
     */
    public RedisLock(RedissonClient redissonClient, Integer defaultLeaseTime, Integer defaultWaitTime) {
        this.redissonClient = redissonClient;
        this.defaultLeaseTime = defaultLeaseTime;
        this.defaultWaitTime = defaultWaitTime;
    }
    
    @Override
    public <T> T lock(String lockName, Callback<T> callback)
            throws UnableToAquireLockException , Exception{
        return lock(lockName, this.defaultLeaseTime, defaultWaitTime, callback);
    }

    @Override
    public <T> T lock(String lockName, int leaseTime, int waitTime, Callback<T> callback)
            throws UnableToAquireLockException , Exception{
        RLock lock = redissonClient.getLock(LOCKER_PREFIX + lockName);
        Instant startInstant = Instant.now(); 
        boolean success = lock.tryLock(waitTime, leaseTime, TimeUnit.SECONDS);
        if (success) {
            if(logger.isDebugEnabled()) {
                logger.debug("Acquired distributed lock {} success, wait time {}/ms. ", lockName, DateTimeUtils.durationMillis(startInstant, Instant.now()));
            }
            try {
                return callback.callback();
            } finally {
                lock.unlock();
                if(logger.isDebugEnabled()) {
                    logger.debug("Release distributed lock {} success. used lock time {}/ms", lockName, DateTimeUtils.durationMillis(startInstant, Instant.now()));
                }
            }
        }
        throw new UnableToAquireLockException();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Validate.notNull(this.defaultLeaseTime, "Distributed lock default exipre time can not be null, please config with key \"lemon.lock.defaultLeaseTime\"");
        Validate.notNull(this.defaultWaitTime, "Distributed lock default exipre time can not be null, please config with key \"lemon.lock.defaultWaitTime\"");
        Validate.notNull(this.redissonClient, "Distributed lock redisson client can not be null.");
    }

}
