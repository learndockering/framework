package com.hisun.lemon.framework.cache.ehcache;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.context.annotation.Configuration;

/**
 * ehcache configuration
 * 
 * @author yuzhou
 * @date 2017年7月17日
 * @time 下午4:56:44
 *
 */
@Configuration
@ConditionalOnClass(EhCacheCacheManager.class)
public class EhcacheConfiguration {
    
}
