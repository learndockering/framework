package com.hisun.lemon.framework.cache.ehcache;

import org.ehcache.event.CacheEvent;
import org.ehcache.event.CacheEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EventLogger implements CacheEventListener<Object, Object> {
    private static final Logger logger = LoggerFactory.getLogger(EventLogger.class);
    
    @Override
    public void onEvent(CacheEvent<? extends Object, ? extends Object> event) {
        if(logger.isDebugEnabled()) {
            logger.debug("Ehcache operation, event {}, key {}, old value {}, new value {}", event.getType(), event.getKey(), event.getOldValue(), event.getNewValue());
        }
    }

}
