package com.hisun.lemon.framework.cache.jcache;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.spi.CachingProvider;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.cache.jcache.JCacheCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.util.StringUtils;

import com.hisun.lemon.framework.cache.CacheType;
import com.hisun.lemon.framework.cache.NamedCacheResolver;


@Configuration
@EnableConfigurationProperties(CacheProperties.class)
public class JCacheConfiguration {
    
    private final CacheProperties cacheProperties;
    
    public JCacheConfiguration(CacheProperties cacheProperties) {
        this.cacheProperties = cacheProperties;
    }
    
    @Bean  
    public JCacheCacheManager jcacheCacheManager(CacheManager jsr107cacheManager) throws IOException{  
        JCacheCacheManager cm = new JCacheCacheManager();  
        cm.setCacheManager(jsr107cacheManager);  
        return cm;  
    }  
      
   /* @Bean  
    public CacheManager jsr107cacheManager(){  
        CachingProvider provider = Caching.getCachingProvider();  
        CacheManager cacheManager = provider.getCacheManager();  
            MutableConfiguration<Long, String> configuration =  
                new MutableConfiguration<Long, String>()  
                        // Cannot set type for store! this may be a bug in spring or ehCache  
                    //.setTypes(Long.class, String.class)  
                    .setStoreByValue(false)   
                .setExpiryPolicyFactory(CreatedExpiryPolicy.factoryOf(Duration.ONE_MINUTE));  
            cacheManager.createCache("foo", configuration);  
          
        return cacheManager;  
    }  */
    
    
    @Bean  
    public CacheManager jsr107cacheManager() throws IOException{ 
        CacheManager jCacheCacheManager = createCacheManager();
        List<String> cacheNames = this.cacheProperties.getCacheNames();
        if (!CollectionUtils.isEmpty(cacheNames)) {
            for (String cacheName : cacheNames) {
                jCacheCacheManager.createCache(cacheName, getDefaultCacheConfiguration());
            }
        }
        customize(jCacheCacheManager);
        return jCacheCacheManager;
    }
    
    @Bean
    public CacheResolver jCacheCacheResoler(org.springframework.cache.CacheManager cacheManager) {
        return new NamedCacheResolver(cacheManager, CacheType.JCACHE);
    }
      
    private CacheManager createCacheManager() throws IOException {
        CachingProvider cachingProvider = getCachingProvider(
                this.cacheProperties.getJcache().getProvider());
        Resource configLocation = this.cacheProperties
                .resolveConfigLocation(this.cacheProperties.getJcache().getConfig());
        if (configLocation != null) {
            return cachingProvider.getCacheManager(configLocation.getURI(),
                    cachingProvider.getDefaultClassLoader(),
                    createCacheManagerProperties(configLocation));
        }
        return cachingProvider.getCacheManager();
    }

    private CachingProvider getCachingProvider(String cachingProviderFqn) {
        if (StringUtils.hasText(cachingProviderFqn)) {
            return Caching.getCachingProvider(cachingProviderFqn);
        }
        return Caching.getCachingProvider();
    }

    private Properties createCacheManagerProperties(Resource configLocation)
            throws IOException {
        Properties properties = new Properties();
        // Hazelcast does not use the URI as a mean to specify a custom config.
        properties.setProperty("hazelcast.config.location",
                configLocation.getURI().toString());
        return properties;
    }

    /**
     * 默认缓存配置
     * @return
     */
    private javax.cache.configuration.Configuration<?, ?> getDefaultCacheConfiguration() {
       /* if (this.defaultCacheConfiguration != null) {
            return this.defaultCacheConfiguration;
        }*/
        /*MutableConfiguration<Long, String> configuration =
                new MutableConfiguration<Long, String>()  
                    .setTypes(Long.class, String.class)   
                    .setStoreByValue(false)   
                    .setExpiryPolicyFactory(CreatedExpiryPolicy.factoryOf(Duration.ONE_MINUTE));  
            Cache<Long, String> cache = cacheManager.createCache("jCache", configuration);*/
        return new MutableConfiguration<Object, Object>();
    }

    private void customize(CacheManager cacheManager) {
        /*if (this.cacheManagerCustomizers != null) {
            AnnotationAwareOrderComparator.sort(this.cacheManagerCustomizers);
            for (JCacheManagerCustomizer customizer : this.cacheManagerCustomizers) {
                customizer.customize(cacheManager);
            }
        }*/
    }
}
