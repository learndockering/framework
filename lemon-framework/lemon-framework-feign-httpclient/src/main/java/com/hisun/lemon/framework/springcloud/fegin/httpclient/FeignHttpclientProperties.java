package com.hisun.lemon.framework.springcloud.fegin.httpclient;

import static com.hisun.lemon.common.utils.NumberUtils.getDefaultIfNull;

/**
 * http client properties
 * @author yuzhou
 * @date 2017年7月5日
 * @time 下午3:17:22
 *
 */
public class FeignHttpclientProperties {
    public static final int DEFAULT_MAX_TOTAL = 200;
    public static final int DEFAULT_MAX_PER_ROUTE = 20;
    public static final long DEFAULT_ALIVE = 60000;
    public static final int DEFAULT_IDLE_TIMEOUT_MILLIS = 30000;
    
    private Integer maxTotal;
    private Integer maxPerRoute;
    private Long alive;
    private Integer idleTimeoutMillis;
    
    public Integer getMaxTotal() {
        return getDefaultIfNull(maxTotal, DEFAULT_MAX_TOTAL);
    }
    public void setMaxTotal(Integer maxTotal) {
        this.maxTotal = maxTotal;
    }
    public Integer getMaxPerRoute() {
        return getDefaultIfNull(maxPerRoute, DEFAULT_MAX_PER_ROUTE);
    }
    public void setMaxPerRoute(Integer maxPerRoute) {
        this.maxPerRoute = maxPerRoute;
    }
    public Long getAlive() {
        return getDefaultIfNull(alive, DEFAULT_ALIVE);
    }
    public void setAlive(Long alive) {
        this.alive = alive;
    }
    public Integer getIdleTimeoutMillis() {
        return getDefaultIfNull(idleTimeoutMillis, DEFAULT_IDLE_TIMEOUT_MILLIS);
    }
    public void setIdleTimeoutMillis(Integer idleTimeoutMillis) {
        this.idleTimeoutMillis = idleTimeoutMillis;
    }
    
}
