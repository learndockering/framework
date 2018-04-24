package com.hisun.lemon.common.utils;

import java.lang.reflect.InvocationTargetException;

import com.hisun.lemon.common.exception.LemonException;

/**
 * BeanUtils
 * 
 * @author yuzhou
 * @date 2017年6月13日
 * @time 下午5:21:22
 *
 */
public class BeanUtils {
    /**
     * 属性拷贝
     * @param dest 目的对象 
     * @param orig 源对象
     */
    public static void copyProperties(Object dest, Object orig){
        try {
            org.apache.commons.beanutils.BeanUtils.copyProperties(dest, orig);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new LemonException(e);
        }
    }
}
