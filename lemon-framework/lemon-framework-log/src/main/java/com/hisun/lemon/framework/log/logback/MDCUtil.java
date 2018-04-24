package com.hisun.lemon.framework.log.logback;

import org.slf4j.MDC;

/**
 * @author yuzhou
 * @date 2017年5月25日
 * @time 下午7:55:52
 *
 */
public class MDCUtil {
    /**
     * 默认的日志MDC Key
     */
    public static final String LOG_MDC_KEY = "requestId";
    /**
     * 默认的日志MDC Value
     */
    public static final String LOG_MDC_DEFAULT = "UNKNOWN";

    /**
     * 设置MDC属性
     *
     * @param key   MDC Key
     * @param value MDC Value
     */
    public static void putMDCKey(String key, String value) {
        MDC.put(key, value != null ? value : LOG_MDC_DEFAULT);
    }

    /**
     * 设置MDC默认Key属性
     *
     * @param value MDC Value
     */
    public static void putMDCKey(String value) {
        putMDCKey(LOG_MDC_KEY, value);
    }

    /**
     * 删除MDC Key
     *
     * @param key MDC Key
     */
    public static void removeMDCKey(String key) {
        MDC.remove(key);
    }

    /**
     * 删除MDC默认的Key
     */
    public static void removeMDCKey() {
        removeMDCKey(LOG_MDC_KEY);
    }
}
