package com.hisun.lemon.framework.cache;

import com.hisun.lemon.common.utils.StringUtils;

/**
 * 缓存类别
 * @author yuzhou
 * @date 2017年7月17日
 * @time 下午5:35:16
 *
 */
public enum CacheType {
    REDIS, EHCACHE, JCACHE;
    public String getCacheType() {
        return StringUtils.lowerCase(this.name());
    }
}
