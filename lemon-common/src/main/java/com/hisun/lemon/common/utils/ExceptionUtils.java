package com.hisun.lemon.common.utils;

import com.hisun.lemon.common.Callback;

/**
 * @author yuzhou
 * @date 2017年8月17日
 * @time 下午9:53:46
 *
 */
public class ExceptionUtils {
    
    public static <T> T ignoreException(Callback<T> c) {
        try {
            return c.callback();
        } catch (Exception e) {
            return null;
        }
    }

}
