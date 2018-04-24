package com.hisun.lemon.gateway.common.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hisun.lemon.gateway.common.response.GatewayResourceResponseMessageResolver;
import com.hisun.lemon.gateway.common.response.ResponseMessageResolver;

/**
 * @author yuzhou
 * @date 2017年9月11日
 * @time 下午2:49:49
 *
 */
@Configuration
public class GatewayCommonConfiguration {
    @Bean
    public ResponseMessageResolver gatewayResourceResponseMessageResolver(ObjectMapper objectMapper, MessageSource messageSource) {
        return new GatewayResourceResponseMessageResolver(objectMapper, messageSource);
    }
}
