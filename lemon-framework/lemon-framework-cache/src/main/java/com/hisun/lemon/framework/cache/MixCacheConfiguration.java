package com.hisun.lemon.framework.cache;

import java.lang.reflect.Method;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.hisun.lemon.common.utils.StringUtils;
import com.hisun.lemon.framework.cache.ehcache.EhcacheConfiguration;
import com.hisun.lemon.framework.cache.jcache.JCacheConfiguration;
import com.hisun.lemon.framework.cache.redis.RedisCacheConfiguration;

/**
 * 混合缓存配置
 * @author yuzhou
 * @date 2017年7月17日
 * @time 下午4:54:44
 *
 */
@Configuration
@EnableCaching
@AutoConfigureAfter({RedisCacheConfiguration.class, EhcacheConfiguration.class, JCacheConfiguration.class})
public class MixCacheConfiguration extends CachingConfigurerSupport {
    
    @Value("${spring.application.name}")
    private String applicationName;
    
    @Override
    @Bean
    public CacheManager cacheManager() {
        return new MixCacheManager();  
    }
    
    @Override
    @Bean  
    public KeyGenerator keyGenerator(){
        return new KeyGenerator() {
            @Override  
            public Object generate(Object target, Method method, Object... params) {  
                StringBuilder sb = new StringBuilder(CacheHelper.CACHE_KEY_PREFIX); 
                sb.append(getApplicationName()).append(CacheHelper.CACHE_KEY_SEPARATOR)
                   .append(target.getClass().getSimpleName()).append(CacheHelper.CACHE_KEY_SEPARATOR)
                   .append(method.getName());
                if(null != params) {
                    for (Object obj : params) {
                        sb.append(CacheHelper.CACHE_KEY_SEPARATOR).append(StringUtils.toString(Optional.ofNullable(obj).orElse("")));  
                    }  
                }
                return sb.toString();  
            }  
        };  
    }  
    
    private String getApplicationName() {
        return this.applicationName;
    }
}
