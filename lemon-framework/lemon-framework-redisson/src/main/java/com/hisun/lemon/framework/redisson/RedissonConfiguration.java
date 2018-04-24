package com.hisun.lemon.framework.redisson;

import static com.hisun.lemon.common.utils.NumberUtils.getDefaultIfNull;

import org.apache.commons.lang3.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.core.type.classreading.MethodMetadataReadingVisitor;

import com.hisun.lemon.common.utils.JudgeUtils;
import com.hisun.lemon.framework.redisson.RedissonConfiguration.RedissonProperties;
/**
 * redisson configuration
 * 
 * @author yuzhou
 * @date 2017年7月19日
 * @time 下午8:42:37
 *
 */
@Configuration
@ConditionalOnClass(RedissonClient.class)
@ConditionalOnProperty(value = "lemon.redisson.enabled", matchIfMissing = false)
@EnableConfigurationProperties(RedissonProperties.class)
public class RedissonConfiguration {
    
    private static final String REDISSON_SINGLE_PREFIX = "redis://";
    private static final int DEFAULT_DATABASE = 0;
    private static final int DEFAULT_POOL_SIZE = 20;
    private static final int DEFAULT_IDLE_SIZE = 5;
    private static final int DEFAULT_IDLE_TIMEOUT = 60000;
    private static final int DEFAULT_CONNECTION_TIMEOUT = 3000;
    private static final int DEFAULT_TIMEOUT = 10000;
    
    @Autowired
    private RedissonProperties redissonProperties;
    
    @Bean
    public RedissonClient redissonClient(Config config) {
        RedissonClient redissonClient = Redisson.create(config);
        return redissonClient;
    }
    
//  config.useMasterSlaveConnection().setMasterAddress("127.0.0.1:6379").addSlaveAddress("127.0.0.1:6389").addSlaveAddress("127.0.0.1:6399");
//  config.useSentinelConnection().setMasterName("mymaster").addSentinelAddress("127.0.0.1:26389", "127.0.0.1:26379");
//  config.useClusterServers().addNodeAddress("127.0.0.1:7000");
    
    @Bean
    @Conditional(RedissonServerModeCondition.class)
    public Config singleConfig() {
        Config config = new Config();
        config.useSingleServer().setAddress(REDISSON_SINGLE_PREFIX + redissonProperties.getAddress());
        if (JudgeUtils.isNotEmpty(redissonProperties.getPassword())) {
            config.useSingleServer().setPassword(redissonProperties.getPassword());
        }
        config.useSingleServer().setDatabase(getDefaultIfNull(redissonProperties.getDatabase(), DEFAULT_DATABASE));
        config.useSingleServer().setConnectionPoolSize(getDefaultIfNull(redissonProperties.getPoolSize(), DEFAULT_POOL_SIZE));
        config.useSingleServer().setConnectionMinimumIdleSize(getDefaultIfNull(redissonProperties.getIdleSize(), DEFAULT_IDLE_SIZE));
        config.useSingleServer().setIdleConnectionTimeout(getDefaultIfNull(redissonProperties.getConnectionTimeout(), DEFAULT_IDLE_TIMEOUT));
        config.useSingleServer().setConnectTimeout(getDefaultIfNull(redissonProperties.getConnectionTimeout(), DEFAULT_CONNECTION_TIMEOUT));
        config.useSingleServer().setTimeout(getDefaultIfNull(redissonProperties.getTimeout(), DEFAULT_TIMEOUT));
        return config;
    }
    
    /**
     * Redisson 配置
     * @author yuzhou
     * @date 2017年7月20日
     * @time 上午9:51:05
     *
     */
    @ConfigurationProperties(prefix = "lemon.redisson")
    public static class RedissonProperties {
        private String mode;
        private String address;
        private String password;
        private Integer database;
        private Integer poolSize;
        private Integer idleSize;
        private Integer idleTimeout;
        private Integer connectionTimeout;
        private Integer timeout;
        public String getMode() {
            return mode;
        }
        public void setMode(String mode) {
            this.mode = mode;
        }
        public String getAddress() {
            return address;
        }
        public void setAddress(String address) {
            this.address = address;
        }
        public String getPassword() {
            return password;
        }
        public void setPassword(String password) {
            this.password = password;
        }
        public Integer getDatabase() {
            return database;
        }
        public void setDatabase(Integer database) {
            this.database = database;
        }
        public Integer getPoolSize() {
            return poolSize;
        }
        public void setPoolSize(Integer poolSize) {
            this.poolSize = poolSize;
        }
        public Integer getIdleSize() {
            return idleSize;
        }
        public void setIdleSize(Integer idleSize) {
            this.idleSize = idleSize;
        }
        public Integer getIdleTimeout() {
            return idleTimeout;
        }
        public void setIdleTimeout(Integer idleTimeout) {
            this.idleTimeout = idleTimeout;
        }
        public Integer getConnectionTimeout() {
            return connectionTimeout;
        }
        public void setConnectionTimeout(Integer connectionTimeout) {
            this.connectionTimeout = connectionTimeout;
        }
        public Integer getTimeout() {
            return timeout;
        }
        public void setTimeout(Integer timeout) {
            this.timeout = timeout;
        }
        
    }
    
    public static class RedissonServerModeCondition implements Condition {
        @Override
        public boolean matches(ConditionContext context,
                AnnotatedTypeMetadata metadata) {
            String mode = context.getEnvironment().getProperty("lemon.redisson.mode");
            if(StringUtils.isEmpty(mode)) {
                return false;
            }
            String methodName = ((MethodMetadataReadingVisitor)metadata).getMethodName();
            if(methodName.startsWith(mode)) {
                return true;
            } else {
                return false;
            }
        }
        
    }
}
