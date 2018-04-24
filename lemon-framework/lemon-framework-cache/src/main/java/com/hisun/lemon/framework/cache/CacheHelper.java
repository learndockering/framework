package com.hisun.lemon.framework.cache;

import org.springframework.cache.CacheManager;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.jcache.JCacheCacheManager;
import org.springframework.data.redis.cache.RedisCacheManager;

import com.hisun.lemon.common.exception.ErrorMsgCode;
import com.hisun.lemon.common.exception.LemonException;
import com.hisun.lemon.common.utils.JudgeUtils;

/**
 * 辅助类
 * @author yuzhou
 * @date 2017年7月17日
 * @time 下午6:18:56
 *
 */
public class CacheHelper {
    public static final String CACHE_KEY_PREFIX = "CACHE.";
    public static final String CACHE_KEY_SEPARATOR = ".";
    
    /**
     * @param cacheManager
     * @return
     */
    public static CacheType getCacheType(CacheManager cacheManager) {
        if (cacheManager instanceof RedisCacheManager) {
            return CacheType.REDIS;
        }
        if(cacheManager instanceof JCacheCacheManager) {
            return CacheType.JCACHE;
        }
        if (cacheManager instanceof EhCacheCacheManager) {
            return CacheType.EHCACHE;
        }
        throw new NotSupportCacheTypeException("Cache manager "+cacheManager +" not supported.");
    }
    
    public static String removeCacheTypeForCacheName(String originalCacheName, CacheType cacheType) {
        if(JudgeUtils.isEmpty(originalCacheName)) {
            return originalCacheName;
        }
        if(originalCacheName.startsWith(cacheType.getCacheType())) {
            originalCacheName = originalCacheName.replaceFirst(cacheType.getCacheType()+NamedCacheResolver.CACHE_NAME_SEPARATOR, "");
        }
        return originalCacheName;
    }

    public static class NotSupportCacheTypeException extends LemonException{
        private static final long serialVersionUID = 1L;
        public NotSupportCacheTypeException(String msgInfo) {
            super(ErrorMsgCode.SYS_ERROR.getMsgCd(), msgInfo);
        }
    }
    
}
