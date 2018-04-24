package com.hisun.lemon.framework.redis.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.hisun.lemon.framework.redis.random.BindingTokenRandomTemplete;
import com.hisun.lemon.framework.utils.RandomTemplete;

/**
 * @author yuzhou
 * @date 2017年9月8日
 * @time 下午5:43:32
 *
 */
@Configuration
public class RedisConfiguration {
    
    @Bean
    public RandomTemplete bindingTokenRandomTemplete(StringRedisTemplate stringRedisTemplate) {
        return new BindingTokenRandomTemplete(stringRedisTemplate);
    }
}
