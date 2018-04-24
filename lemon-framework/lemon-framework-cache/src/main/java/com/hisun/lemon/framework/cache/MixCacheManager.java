package com.hisun.lemon.framework.cache;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.support.AbstractCacheManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.hisun.lemon.common.KVPair;
import com.hisun.lemon.common.extension.ExtensionLoader;
import com.hisun.lemon.common.utils.JudgeUtils;
import com.hisun.lemon.common.utils.Validate;

/**
 * 混合缓存管理
 * 支持ehcache，redis
 * @author yuzhou
 * @date 2017年7月17日
 * @time 下午4:06:28
 *
 */
public class MixCacheManager extends AbstractCacheManager implements ApplicationContextAware{
    public static final Logger logger = LoggerFactory.getLogger(MixCacheManager.class);
    
    private ApplicationContext applicationContext;
    private Map<CacheType, CacheManager> cacheManagers;
    
    public MixCacheManager() {
        super();
    }
    
    public void initCacheManagers() {
        if(JudgeUtils.isNotNull(this.cacheManagers)) {
            return;
        }
        Map<String,CacheManager> cacheManagerMap = ExtensionLoader.getSpringBeansOfType(this.applicationContext, CacheManager.class);
        /*if(cacheManagerMap != null && cacheManagerMap.size() > 0) {
            Map<String, CacheManager> cmMap =  cacheManagerMap.values().stream().filter(cm -> !(cm instanceof MixCacheManager)).map(cm -> {
                return new KVPair<String, CacheManager>(CacheHelper.getCacheTypeForString(cm), cm);
                }).collect(Collectors.toMap(KVPair<String, CacheManager>::getK, KVPair<String, CacheManager>::getV));
            this.cacheManagers = Collections.unmodifiableMap(cmMap);
        }*/
        if(JudgeUtils.isNotEmpty(cacheManagerMap)) {
            Map<CacheType, CacheManager> cacheManagerMapTmp = cacheManagerMap.values().stream().filter(cm -> !(cm instanceof MixCacheManager)).map(cm -> KVPair.instance(CacheHelper.getCacheType(cm), cm))
                .collect(Collectors.toMap(KVPair::getK, KVPair::getV));
            this.cacheManagers = Collections.unmodifiableMap(cacheManagerMapTmp);
        }
        Validate.notNull(this.cacheManagers, "cache managers can not be null.");
        Validate.notEmpty(this.cacheManagers.values().toArray(), "cache managers can not be null.");
        if(logger.isInfoEnabled()) {
            logger.info("find cache managers {}.", this.cacheManagers);
        }
    }

    @Override
    protected Collection<? extends Cache> loadCaches() {
        initCacheManagers();
        Collection<Cache> caches = new ArrayList<>();
        this.cacheManagers.values().stream().forEach(cm -> {
            Collection<String> cacheNames = cm.getCacheNames();
            if(JudgeUtils.isNotNull(cacheNames)) {
                cacheNames.stream().forEach(cacheName -> {
                    caches.add(cm.getCache(cacheName));
                });
            }
        });
        if(logger.isInfoEnabled()) {
            logger.info("load caches {}", caches);
        }
        return caches;
    }

    @Override
    protected Cache getMissingCache(String name) {
        if(StringUtils.startsWith(name, CacheType.REDIS.getCacheType())) {
            Cache cache = this.cacheManagers.get(CacheType.REDIS).getCache(CacheHelper.removeCacheTypeForCacheName(name, CacheType.REDIS));
            if(logger.isInfoEnabled()) {
                logger.info("get missing redis cache {} with name {}", cache, name);
            }
            return cache;
        }
        
        if(StringUtils.startsWith(name, CacheType.JCACHE.getCacheType())) {
            Cache cache = this.cacheManagers.get(CacheType.JCACHE).getCache(CacheHelper.removeCacheTypeForCacheName(name, CacheType.JCACHE));
            if(logger.isInfoEnabled()) {
                logger.info("get missing jcache cache {} with name {}", cache, name);
            }
            return cache;
        }
        
        if(StringUtils.startsWith(name, CacheType.EHCACHE.getCacheType())) {
            Cache cache = this.cacheManagers.get(CacheType.EHCACHE).getCache(CacheHelper.removeCacheTypeForCacheName(name, CacheType.EHCACHE));
            if(logger.isInfoEnabled()) {
                logger.info("get missing ehcache cache {} with name {}", cache, name);
            }
            return cache;
        }
        
        Cache cache = this.cacheManagers.get(CacheType.EHCACHE).getCache(name);
        if(logger.isInfoEnabled()) {
            logger.info("get missing default ehcache cache {} with name {}", cache, name);
        }
        return cache;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

}
