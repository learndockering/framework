package com.hisun.lemon.framework.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hisun.lemon.common.exception.LemonException;

/**
 * @author yuzhou
 * @date 2017年8月23日
 * @time 上午10:48:20
 *
 */
public class ObjectMapperHelper {
    
    /**
     * 生成json字符串
     * 
     * @param objectMapper
     * @param object
     * @return
     */
    public static String writeValueAsString(ObjectMapper objectMapper, Object object, boolean toStringWhenError) {
        if(null == object) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            if (toStringWhenError) {
                return "{\"Object\":\""+String.valueOf(object)+"\"}";
            } else {
                throw LemonException.create(e);
            }
        }
    }
}
