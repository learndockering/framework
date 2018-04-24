package com.hisun.lemon.framework.idgenerate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.hisun.lemon.common.exception.LemonException;
import com.hisun.lemon.common.utils.JudgeUtils;
import com.hisun.lemon.common.utils.ResourceUtils;
import com.hisun.lemon.framework.idgenerate.config.IdGenProperties;
import com.hisun.lemon.framework.redis.serializer.LongRedisSerializer;
import com.hisun.lemon.framework.utils.LemonUtils;

/**
 * ID 生成器
 * 保证key对应的ID生成唯一且增长
 * 不保证key对应的ID会顺序增长，有可能某段值因其他原因(服务重启、redis故障等)丢失
 * @author yuzhou
 * @date 2017年6月15日
 * @time 上午9:32:04
 *
 */
public class IdGenerator {
    private static final Logger logger = LoggerFactory.getLogger(IdGenerator.class);
    
    private static final String DEFAULT_PREFIX_ID_GEN = "IDGEN";
    private static final int DEFAULT_DELTA = 500;
    private static final String DEFAULT_IDGEN_LUA_FILE_PATH = "lua" + ResourceUtils.JAR_PACKAGE_PATH_SEPARATOR + "IdGen.lua";
    
    private static final Map<String, IdStore> ID_STORE_MAP = new ConcurrentHashMap<>();
    private Lock lock = new ReentrantLock();
    private String idgenScript = null;
    
    private RedisTemplate<String, Long> redisTemplate;
    
    private IdGenProperties idGenProperties;
    
    public IdGenerator() {}
    
    public IdGenerator(RedisTemplate<String, Long> redisTemplate, IdGenProperties idGenProperties) {
        this.redisTemplate = redisTemplate;
        this.idGenProperties = idGenProperties;
    }
    
    /**
     * 
     * 生成ID，key在application 范围内要唯一
     * 如果不需要多个应用共用ID生成器，建议使用此方法生成ID
     * 
     * @param key
     * @return
     */
    public Long generateId(String key) {
        return generateId(key, false);
    }
    
    /**
     * 钟对共用一个redis所有应用生成唯一ID
     * @param key
     * @return
     */
    public Long generateGlobalId(String key) {
        return generateId(key, true);
    }
    
    /**
     * 生成ID
     * 
     * @param key
     * @param global 是否全局的 true ：全局 false : 钟对application
     * @return
     */
    private Long generateId(String key, boolean global) {
        if(JudgeUtils.isBlank(key)) {
            throw new LemonException(LemonException.SYS_ERROR_MSGCD, "Key is null for generating id.");
        }
        IdStore idStore = ID_STORE_MAP.get(key);
        if(null == idStore) {
            lock.lock();
            try {
                idStore = ID_STORE_MAP.get(key);
                if(null == idStore) {
                    idStore = newIdStore(key, global);
                    ID_STORE_MAP.put(key, idStore);
                }
            } finally {
                lock.unlock();
            }
        }
        while(true) {
            Long value = idStore.nextValue();
            if(value < 0) {
                lock.lock();
                try{
                    idStore = ID_STORE_MAP.get(key);
                    value = idStore.nextValue();
                    if(value < 0) {
                        idStore = newIdStore(key, global);
                        value = idStore.nextValue();
                        ID_STORE_MAP.put(key, idStore);
                    }
                } finally {
                    lock.unlock();
                }
                //这个判断应该不会进去，除非newIdStore没有对maxValue进行控制
                if(value < 0) {
                    idStore = ID_STORE_MAP.get(key);
                    continue;
                }
            }
            if(logger.isDebugEnabled()) {
                logger.debug("acquire id \"{}\" with key \"{}\".", value, key);
            }
            return value;
        }
    }
    
    private IdStore newIdStore(String key, boolean global) {
        IdStore oldIdStore = ID_STORE_MAP.get(key);
        Long maxValue = null;
        List<String> keys;
        if(null != oldIdStore) {
            maxValue = oldIdStore.maxValue;
            keys = oldIdStore.keys;
        } else {
            maxValue = getMaxValue(key);
            keys = new ArrayList<>();
            keys.add(getIdgenKey(global));
            keys.add(key);
        }
        if(null == maxValue) {
            maxValue = -1L;
        }
        /*Long localMaxId = this.getRedisTemplate().opsForHash().increment(getIdgenKey(global), key, delta);
        Long currentId = localMaxId - delta + 1;
        if(null != maxValue) {
            if(currentId > maxValue) {
                currentId = 1L;
                localMaxId = this.getRedisTemplate().opsForHash().increment(getIdgenKey(global), key, 1 - localMaxId);
            }
            localMaxId = localMaxId <= maxValue ? localMaxId : maxValue;
        }*/
       // Integer delta = this.idGenProperties.getDelta();
        Integer delta = getDelta(key);
        Long localMaxId = this.getRedisTemplate().execute(new DefaultRedisScript<Long>(this.idgenScript, Long.class), 
            new StringRedisSerializer(), new LongRedisSerializer(), keys, String.valueOf(maxValue), String.valueOf(delta));
        Long currentId = localMaxId - delta + 1;
        if(-1 != maxValue) {
            localMaxId = localMaxId <= maxValue ? localMaxId : maxValue;
        }
        if(logger.isDebugEnabled()) {
            logger.debug("new Id store with key \"{}\", currentId \"{}\", localMaxId \"{}\", maxValue \"{}\", delta \"{}\".", key, currentId, localMaxId, maxValue, delta);
        }
        return new IdStore(new AtomicLong(currentId), localMaxId, maxValue, keys);
    }
    
    public Integer getDelta(String key) {
        Integer delta = this.idGenProperties.getDelta().get(key);
        if(JudgeUtils.isNull(delta)) {
            delta = this.idGenProperties.getDelta().get(IdGenProperties.DEFAULT_DELTA_KEY);
        }
        if(JudgeUtils.isNull(delta)) {
            delta = DEFAULT_DELTA;
        }
        return delta;
    }

    public RedisTemplate<String, Long> getRedisTemplate() {
        return redisTemplate;
    }

    public void setRedisTemplate(RedisTemplate<String, Long> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
    
    static class IdStore {
        private AtomicLong localIdGen;
        private Long localMaxValue;
        //全局最大值，超过该值继续从1开始;该值为null则无穷自增，达到long最大值
        private Long maxValue;
        private List<String> keys;
        
        public IdStore(AtomicLong localIdGen, Long localMaxValue, Long maxValue, List<String> keys) {
            this.localIdGen = localIdGen;
            this.localMaxValue = localMaxValue;
            this.maxValue = maxValue;
            this.keys = keys;
        }
        
        public Long nextValue() {
            Long value = localIdGen.getAndIncrement();
            if(value > localMaxValue) {
                return -1L;
            }
            if(-1 != maxValue && value > maxValue) {
                return -2L;
            }
            return value;
        }
    }
    
    private String getIdgenKey(boolean global) {
        String idgenPrefix = idGenProperties.getPrefix();
        if(JudgeUtils.isBlank(idgenPrefix)) {
            idgenPrefix = DEFAULT_PREFIX_ID_GEN;
        }
        if(global) {
            return idgenPrefix;
        }
        return idgenPrefix + "." + LemonUtils.getApplicationName();
    }
    
    private Long getMaxValue(String key) {
        return LemonUtils.getProperty("lemon.idgen.max-value." + key, Long.class);
    }
    
    @PostConstruct
    public void init() throws IOException {
        idgenScript = ResourceUtils.getFileContent(DEFAULT_IDGEN_LUA_FILE_PATH);
        if(logger.isDebugEnabled()) {
            logger.debug("Load IdGen lua script {} ~~~ {}", DEFAULT_IDGEN_LUA_FILE_PATH, this.idgenScript);
        }
    }

    public IdGenProperties getIdGenProperties() {
        return idGenProperties;
    }

    public void setIdGenProperties(IdGenProperties idGenProperties) {
        this.idGenProperties = idGenProperties;
    }
}
