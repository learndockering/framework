package com.hisun.lemon.common.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hisun.lemon.common.Callback;
import com.hisun.lemon.common.Callback0;
import com.hisun.lemon.common.exception.LemonException;

/**
 * 判断
 * 
 * @author yuzhou
 * @date 2017年6月14日
 * @time 上午9:42:59
 *
 */
public class JudgeUtils {
    private static final Logger logger = LoggerFactory.getLogger(JudgeUtils.class);
    
    public static final String successfulMsgCode = "00000";
    
    public static <T> boolean isNull(T t) {
        return null == t;
    }
    
    public static <T> boolean isNotNull(T t) {
        return null != t;
    }
    
    @SafeVarargs
    public static <T> boolean isNullAny(T... ts) {
        if(null == ts) {
            return true;
        }
        
        for(T t: ts) {
            if(null == t) {
                return true;
            }
        }
        return false;
    }
    
    public static <T, L> boolean equals(T t, L l) {
        return t.equals(l);
    }
    
    public static <T, L> boolean notEquals(T t, L l) {
        return ! equals(t, l);
    }
    
    public static boolean equals(String str1, String str2) {
        return StringUtils.equals(str1, str2);
    }
    public static boolean equalsIgnoreCase(String str1, String str2) {
        return StringUtils.equalsIgnoreCase(str1, str2);
    }
    
    public static boolean notEquals(String str1, String str2) {
        return ! equals(str1, str2);
    }
    
    public static boolean equalsAny(String str1, String... strs) {
        if(null == strs && null == str1) {
            return true;
        }
        if(null == strs) {
            return false;
        }
        boolean f = false;
        for(String s : strs) {
            if(equals(str1, s)) {
                f = true;
                break;
            }
        }
        return f;
    }
    
    public static boolean isEmpty(String str) {
        return StringUtils.isEmpty(str);
    }
    
    public static boolean isNotEmpty(String str) {
        return ! isEmpty(str);
    }
    
    public static boolean isBlank(String str) {
        return StringUtils.isBlank(str);
    }
    
    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }
    
    public static boolean isNotBlankAll(String... args) {
        if(null == args) {
            return false;
        }
        boolean f = true;
        for(String s : args) {
            if(isBlank(s)) {
                f = false;
                break;
            }
        }
        return f;
    }
    
    public static boolean isBlankAll(String... args) {
        if(null == args) {
            return true;
        }
        boolean f = true;
        for(String s : args) {
            if(isNotBlank(s)) {
                f = false;
                break;
            }
        }
        return f;
    }
    
    public static boolean isBlankAny(String... args) {
        if(null == args) {
            return true;
        }
        boolean f = false;
        for(String s : args) {
            if(isBlank(s)) {
                f = true;
                break;
            }
        }
        return f;
    }
    
    /**
     * 集合是否为空
     * @param c
     * @return
     */
    public static boolean isEmpty(Collection<?> c) {
        if(null == c) {
            return true;
        }
        if(c.size() <= 0) {
            return true;
        }
        return false;
    }
    
    /**
     * 集合是否不为空
     * @param c
     * @return
     */
    public static boolean isNotEmpty(Collection<?> c) {
        return !isEmpty(c);
    }
    
    /**
     * 判断map是否为空
     * @param map
     * @return
     */
    public static boolean isEmpty(Map<?, ?> map) {
        if(null == map) {
            return true;
        }
        if(map.size() <= 0) {
            return true;
        }
        return false;
    }
    
    /**
     * 判断map是否不为空
     * @param map
     * @return
     */
    public static boolean isNotEmpty(Map<?, ?> map) {
        return !isEmpty(map);
    }
    
    /**
     * 判断消息是否成功
     * @param msgCd
     * @return
     */
    public static boolean isSuccess(String msgCd) {
        if(StringUtils.isEmpty(msgCd)) {
            throw new LemonException("MsgCd is null.");
        }
        int len = msgCd.length();
        return StringUtils.equals(msgCd.substring(len - 5), successfulMsgCode);
    }
    
    public static <T> boolean isEmpty(T[] ts) {
        if(null == ts) {
            return true;
        }
        if(ts.length <= 0) {
            return true;
        }
        return false;
    }
    
    public static <T> boolean isNotEmpty(T[] ts) {
        return !isEmpty(ts);
    }
    
    /**
     * 包含
     * @param args
     * @param s
     * @return
     */
    public static boolean contain(String[] args, String s) {
        if(isEmpty(args)) return false;
        boolean f = false;
        for(String a : args) {
            if(equals(a, s)) {
                f = true;
                break;
            }
        }
        return f;
    }
    
    /**
     * 交易执行失败
     * @param msgCd
     * @return
     */
    public static boolean isNotSuccess(String msgCd) {
        return ! isSuccess(msgCd);
    }
    
    /**
     * 非
     * @param flag
     * @return
     */
    public static boolean not (boolean flag) {
        return ! flag;
    }
    
    public static boolean isTrue(Boolean flag, boolean defaultFlag) {
        if(null == flag) {
            return defaultFlag;
        }
        return flag;
    }
    
    /**
     * 流检测
     * @param inputStream
     * @return
     */
    public static boolean available(InputStream inputStream) {
        try {
            if(null != inputStream && inputStream.available() > 0) {
                return true;
            }
        } catch (IOException e) {
            if(logger.isErrorEnabled()) {
                logger.error("Failed to during available input stream. ", e);
            }
        }
        return false;
    }
    
    /**
     * @param flag
     * @param callback
     */
    public static <T> T callbackIfNecessary(boolean flag, Callback<T> callback) {
        if(flag) {
            return callback.callback();
        }
        return null;
    }
    
    /**
     * @param flag
     * @param callback
     */
    public static void callbackIfNecessary(boolean flag, Callback0 callback) {
        if(flag) {
            callback.callback();
        }
    }
    
    /**
     * @param flag
     * @param callback
     */
    public static void callbackIfNecessary(Callback<Boolean> callback, Callback0 callback0) {
        if(callback.callback()) {
            callback0.callback();
        }
    }

}
