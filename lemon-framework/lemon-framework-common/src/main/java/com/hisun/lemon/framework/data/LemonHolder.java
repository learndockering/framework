package com.hisun.lemon.framework.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.NamedThreadLocal;

/**
 * lemon holder
 * @author yuzhou
 * @date 2017年6月30日
 * @time 下午4:13:22
 *
 */
public class LemonHolder {
    private static final Logger logger = LoggerFactory.getLogger(LemonHolder.class);
    
    private static final ThreadLocal<LemonData> lemonDataHolder = new NamedThreadLocal<LemonData>("LemonDataHolder"){
        protected LemonData initialValue() {
            return new LemonData();
        }
    };
 
    public static LemonData getLemonData() {
        return lemonDataHolder.get();
    }
    
    public static void setLemonData(LemonData lemonData) {
        lemonDataHolder.set(lemonData);
        if(logger.isDebugEnabled()) {
            logger.debug("set lemond data {}",lemonData);
        }
    }
    
    public static void clear() {
        lemonDataHolder.remove();
    }
    
}
