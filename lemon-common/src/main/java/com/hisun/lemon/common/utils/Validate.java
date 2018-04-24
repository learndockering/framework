package com.hisun.lemon.common.utils;

/**
 * 继承common validate
 * @author yuzhou
 * @date 2017年7月8日
 * @time 下午12:21:54
 *
 */
public class Validate extends org.apache.commons.lang3.Validate {
    
    /**
     * 非负
     * @param i
     * @param msg
     */
    public static void nonNegative(int i , String msg) {
        if(i < 0) {
            throw new IllegalArgumentException(msg);
        }
    }
    
    /**
     * 非负
     * @param i
     * @param msg
     */
    public static void nonNegative(double d , String msg) {
        if(d < 0) {
            throw new IllegalArgumentException(msg);
        }
    }
    /**
     * 非负
     * @param i
     * @param msg
     */
    public static void nonNegative(long d , String msg) {
        if(d < 0) {
            throw new IllegalArgumentException(msg);
        }
    }
}
