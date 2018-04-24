package com.hisun.lemon.framework.springcloud.fegin;

import static feign.Util.valuesOrEmpty;

import java.io.IOException;
import java.lang.reflect.Type;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.feign.support.ResponseEntityDecoder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hisun.lemon.common.utils.JudgeUtils;
import com.hisun.lemon.framework.data.GenericDTO;
import com.hisun.lemon.framework.data.LemonData;
import com.hisun.lemon.framework.data.LemonHolder;

import feign.FeignException;
import feign.Response;
import feign.codec.Decoder;

/**
 * lemon 扩展fegin decoder
 * @author yuzhou
 * @date 2017年7月4日
 * @time 上午11:46:07
 *
 */
public class LemonResponseEntityDecoder extends ResponseEntityDecoder {
    private static final Logger logger = LoggerFactory.getLogger(LemonResponseEntityDecoder.class);
    
    private ObjectMapper objectMapper;

    public LemonResponseEntityDecoder(Decoder decoder, ObjectMapper objectMapper) {
        super(decoder);
        this.objectMapper = objectMapper;
    }
    
    @Override
    public Object decode(final Response response, Type type) throws IOException,
            FeignException {
        Object result =  super.decode(response, type);
        if(result != null && result instanceof GenericDTO) {
            GenericDTO<?> genericDTO = (GenericDTO<?>) result;
            resolveRouteInfo(genericDTO);
        }
        if(logger.isInfoEnabled()) {
            logger.info("Received msg {}", responseToString(response, result));
        }
        return result;
    }

    private void resolveRouteInfo(GenericDTO<?> genericDTO) {
        LemonData lemonData = LemonHolder.getLemonData();
        //非web入口的交易LemonData没有初始化
        if(JudgeUtils.isNull(lemonData)) {
            return;
        }
        lemonData.setRouteInfo(genericDTO.getRouteInfo());
        if(logger.isDebugEnabled()) {
            logger.debug("Resolved route info for lemonData {}", lemonData);
        }
    }
    
    private String responseToString(Response response, Object object) {
        StringBuilder builder = new StringBuilder("HTTP/1.1 ").append(response.status());
        if (response.reason() != null) builder.append(' ').append(response.reason());
        builder.append('\n');
        for (String field : response.headers().keySet()) {
          for (String value : valuesOrEmpty(response.headers(), field)) {
            builder.append(field).append(": ").append(value).append('\n');
          }
        }
        if (response.body() != null) {
            try {
                builder.append('\n').append(objectMapper.writeValueAsString(object));
            } catch (IOException e) {
            }
        }
        return builder.toString();
    }

}
