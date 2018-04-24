package com.hisun.lemon.framework.cache;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.cache.CacheManager;
import org.springframework.cache.interceptor.AbstractCacheResolver;
import org.springframework.cache.interceptor.CacheOperationInvocationContext;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

import com.hisun.lemon.common.utils.JudgeUtils;
import com.hisun.lemon.common.utils.StringUtils;

/**
 * cache name resolver
 * 
 * @author yuzhou
 * @date 2017年7月18日
 * @time 上午9:09:34
 *
 */
public class NamedCacheResolver extends AbstractCacheResolver implements EnvironmentAware {
    public static final String CACHE_NAME_SEPARATOR = ".";
    private CacheType cacheType;
    private String cacheNamePrefix;
    private Environment environment;
    
    public NamedCacheResolver(CacheManager cacheManager, CacheType cacheType) {
        super(cacheManager);
        this.cacheType = cacheType;
    }
    
    public NamedCacheResolver(CacheManager cacheManager, CacheType cacheType, String cacheNamePrefix) {
        this(cacheManager, cacheType);
        this.cacheNamePrefix = cacheNamePrefix;
    }
    
    @Override
    protected Collection<String> getCacheNames(CacheOperationInvocationContext<?> context) {
        Collection<String> cacheNames = context.getOperation().getCacheNames();
        if(JudgeUtils.isNotEmpty(cacheNames)) {
            return cacheNames.stream().map(c -> StringUtils.parsePlaceHolder(c, environment)).map(NamedCacheResolver.this::resolveCacheName)
                .collect(Collectors.toList());
        }
        return cacheNames;
    }
    
    private String resolveCacheName(String originalCacheName) {
        StringBuilder cacheName = new StringBuilder(this.cacheType.getCacheType()).append(CACHE_NAME_SEPARATOR);
        if(JudgeUtils.isNotEmpty(this.cacheNamePrefix) && !StringUtils.startsWith(originalCacheName, this.cacheNamePrefix)) {
            cacheName.append(this.cacheNamePrefix).append(CACHE_NAME_SEPARATOR);
        }
        cacheName.append(originalCacheName);
        return cacheName.toString();
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

}
