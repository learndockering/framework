package com.hisun.lemon.framework.cumulative;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * 累计配置
 * @author yuzhou
 * @date 2017年7月20日
 * @time 下午6:23:22
 *
 */
@Configuration
public class CumulativeConfiguration {
    
    @Bean
    public Cumulative redisCumulative(@Qualifier("stringRedisTemplate") RedisTemplate<String, String> redisTemplate) {
        return new RedisCumulative(redisTemplate);
    }
}
