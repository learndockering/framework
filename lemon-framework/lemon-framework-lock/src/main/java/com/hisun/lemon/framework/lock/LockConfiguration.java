package com.hisun.lemon.framework.lock;

import static com.hisun.lemon.common.utils.NumberUtils.getDefaultIfNull;

import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.hisun.lemon.framework.lock.LockConfiguration.LockProperties;
import com.hisun.lemon.framework.redisson.RedissonConfiguration;

/**
 * 分布式锁配置
 * @author yuzhou
 * @date 2017年7月20日
 * @time 上午11:21:02
 *
 */
@Configuration
@EnableConfigurationProperties(LockProperties.class)
@AutoConfigureAfter({RedissonConfiguration.class})
public class LockConfiguration {
    public static final int DEFAULT_LEASE_TIME = 100;
    public static final int DEFAULT_WAIT_TIME = 30; 

    @Autowired
    private LockProperties lockProperties;
    
    @Bean
    public DistributedLocker redisLock(RedissonClient redissonClient) {
        return new RedisLock(redissonClient, getDefaultIfNull(this.lockProperties.getDefaultLeaseTime(), DEFAULT_LEASE_TIME),
            getDefaultIfNull(this.lockProperties.getDefaultWaitTime(), DEFAULT_WAIT_TIME));
    }
    
    @ConfigurationProperties(prefix = "lemon.lock")
    public static class LockProperties {
        private Integer defaultLeaseTime;
        private Integer defaultWaitTime;
        
        public Integer getDefaultLeaseTime() {
            return defaultLeaseTime;
        }
        public void setDefaultLeaseTime(Integer defaultLeaseTime) {
            this.defaultLeaseTime = defaultLeaseTime;
        }
        public Integer getDefaultWaitTime() {
            return defaultWaitTime;
        }
        public void setDefaultWaitTime(Integer defaultWaitTime) {
            this.defaultWaitTime = defaultWaitTime;
        }
        
    }
}
