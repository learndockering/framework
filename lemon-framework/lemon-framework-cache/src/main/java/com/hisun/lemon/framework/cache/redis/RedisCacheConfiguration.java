package com.hisun.lemon.framework.cache.redis;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.cache.CacheManager;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hisun.lemon.common.KVPair;
import com.hisun.lemon.common.utils.JudgeUtils;
import com.hisun.lemon.common.utils.StringUtils;
import com.hisun.lemon.framework.cache.CacheType;
import com.hisun.lemon.framework.cache.NamedCacheResolver;

/**
 * Redis configuration
 * 
 * @author yuzhou
 * @date 2017年6月14日
 * @time 下午3:07:50
 *
 */
@Configuration
@ConditionalOnClass(RedisCacheManager.class)
public class RedisCacheConfiguration implements EnvironmentAware{
    private static final Logger logger = LoggerFactory.getLogger(RedisCacheConfiguration.class);
    
    private Environment environment;
    /**
     * redis 默认过期时间
     */
    @Value("${spring.redis.default-expiration:600}")
    private Long redisDefaultExpiration;
    
    @Value("${lemon.cache.cacheName.prefix:CACHENAME}")
    private String redisCacheNamePrefix;
    
    @Autowired
    private RedisTemplate<?,?> redisTemplate;
    
    @Bean
    public CacheManager redisCacheManager(RedisTemplate<String, String> stringRedisTemplate0) {
        RedisCacheManager rcm = new RedisCacheManager(stringRedisTemplate0);
        //设置缓存过期时间
        rcm.setDefaultExpiration(redisDefaultExpiration);//秒
        //cache name 对应的缓存时间
        Map<String, Long> expires = parseRedisExpires();
        if(logger.isInfoEnabled()) {
            logger.info("redis default expiration is {}/s, cache expirations are {}", this.redisDefaultExpiration, expires);
        }
        rcm.setExpires(expires);
        return rcm;
    }
    
    /**
     * 解析redis过期时间
     * @return
     */
    private Map<String, Long> parseRedisExpires() {
        RelaxedPropertyResolver propertyResolver = new RelaxedPropertyResolver(this.environment, "lemon.cache.redis.expires");
        Map<String, Object> subProperties = propertyResolver.getSubProperties(".");
        if (JudgeUtils.isNotEmpty(subProperties)) {
            Map<String, Long> redisExprires = subProperties.entrySet().stream().filter(sp -> JudgeUtils.isNotEmpty(sp.getKey())).map(sp -> 
                new KVPair<String, Long>(StringUtils.parsePlaceHolder(sp.getKey(), this.environment), NumberUtils.toLong(String.valueOf(sp.getValue())))
                ).collect(Collectors.toMap(KVPair::getK, KVPair::getV));
            return Collections.unmodifiableMap(redisExprires);
        }
        return null;
    }

    /**
     * 缓存专用
     * @param factory
     * @return
     */
    @Bean
    public RedisTemplate<String, String> stringRedisTemplate0(RedisConnectionFactory factory) {
        StringRedisTemplate template = new StringRedisTemplate(factory);
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());    //jsr310,localeDate 等java8 解决
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(mapper);
        template.setValueSerializer(jackson2JsonRedisSerializer);
        template.afterPropertiesSet();
        return template;
    }
    
    /**
     * redis cache resolver
     * @param cacheManager
     * @return
     */
    @Bean
    public CacheResolver redisCacheResolver(CacheManager cacheManager) {
        return new NamedCacheResolver(cacheManager, CacheType.REDIS, this.redisCacheNamePrefix);
    }
    
    /**
     * key 都是以String类型
     */
    @PostConstruct
    public void setRedisTemplateSerializer() {
            redisTemplate.setKeySerializer(new StringRedisSerializer());
            redisTemplate.setHashKeySerializer(new StringRedisSerializer());
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
    
}
