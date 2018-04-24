package com.hisun.lemon.common.context;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 上下文
 * @author yuzhou
 * @date 2017年9月9日
 * @time 下午3:31:39
 *
 */
public class LemonContext extends ConcurrentHashMap<Object, Object>{
    private static final long serialVersionUID = 2729826264846708794L;
    
    private static final String NEED_PROCESS_MSG_INFO = "NEED_PROCESS_MSG_INFO";
    private static final String NEED_PUT_MDC = "NEED_PUT_MDC";
    
    private static final ThreadLocal<LemonContext> currentContextHolder = new ThreadLocal<LemonContext>(){
        protected LemonContext initialValue() {
            return new LemonContext();
        }
    };
    
    public static LemonContext getCurrentContext() {
        return currentContextHolder.get();
    }
    
    public static void setCurrentContext(LemonContext lemonContext) {
        currentContextHolder.set(lemonContext);
    }
    
    public static void putToCurrentContext(Object key, Object value) {
        getCurrentContext().put(key, value);
    }
    
    public static Object getFromCurrentContext(Object key) {
        return getCurrentContext().get(key);
    }
    
    public boolean getBooleanOrDefault(Object key, boolean defaultVal) {
        return Optional.ofNullable(get(key)).map(Boolean.class::cast).orElse(defaultVal);
    }
    
    public static void removeFromCurrentContext(Object key) {
        getCurrentContext().remove(key);
    }
    
    public static void clearCurrentContext() {
        currentContextHolder.remove();
    }
    
    public static void setNeedProcessMsgInfo() {
        putToCurrentContext(NEED_PROCESS_MSG_INFO, Boolean.valueOf(true));
    }
    
    public static boolean needProcessMsgInfo() {
        return Optional.ofNullable(getCurrentContext().get(NEED_PROCESS_MSG_INFO)).map(Boolean.class::cast).orElse(false);
    }
    
    public static void setAlreadyPutMDC() {
        putToCurrentContext(NEED_PUT_MDC, Boolean.valueOf(false));
    }
    
    public static boolean needPutMDC() {
        return Optional.ofNullable(getFromCurrentContext(NEED_PUT_MDC)).map(Boolean.class::cast).orElse(true);
    }
    
}
