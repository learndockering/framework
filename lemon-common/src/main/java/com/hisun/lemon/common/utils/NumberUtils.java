package com.hisun.lemon.common.utils;

/**
 * Number utils
 * @author yuzhou
 * @date 2017年7月18日
 * @time 下午1:43:48
 *
 */
public class NumberUtils extends org.apache.commons.lang3.math.NumberUtils {
    
    /**
     * @param value
     * @param defaultVal
     * @return
     */
    public static Integer getDefaultIfNull(Integer value, Integer defaultVal) {
        if(null == value) {
            return defaultVal;
        }
        return value;
    }
    
    /**
     * @param value
     * @param defaultVal
     * @return
     */
    public static Long getDefaultIfNull(Long value, Long defaultVal) {
        if(null == value) {
            return defaultVal;
        }
        return value;
    }
    
}
