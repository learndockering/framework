package com.hisun.lemon.framework.idgenerate.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.hisun.lemon.framework.idgenerate.IdGenerator;
import com.hisun.lemon.framework.idgenerate.auto.AutoIdGenResolver;

/**
 * 
 * @author yuzhou
 * @date 2017年9月1日
 * @time 上午11:52:09
 *
 */
@Configuration
@EnableConfigurationProperties({IdGenProperties.class})
public class IdGenConfiguration {
    
    @Bean
    public RedisTemplate<String, Long> stringLongRedisTemplate(RedisConnectionFactory factory) {
        RedisTemplate< String, Long > template =  new RedisTemplate< String, Long >();
        template.setConnectionFactory( factory );
        template.setKeySerializer( new StringRedisSerializer() );
        template.setHashValueSerializer( new GenericToStringSerializer< Long >( Long.class ) );
        template.setValueSerializer( new GenericToStringSerializer< Long >( Long.class ) );
        return template;
    }
    
    @Bean
    public IdGenerator idGenerator(RedisTemplate<String, Long> stringLongRedisTemplate, IdGenProperties idGenProperties) {
        return new IdGenerator(stringLongRedisTemplate, idGenProperties);
    }
    
    @Configuration
    public static class AutoIdGenResolverConfiguration {
        @Bean
        public static AutoIdGenResolver autoIdGenResolver(@Value("${lemon.idgen.autogen.DOPackage:com.hisun.lemon.*.entity}") String doPackage) {
            return new AutoIdGenResolver(doPackage);
        }
    }
    
}
