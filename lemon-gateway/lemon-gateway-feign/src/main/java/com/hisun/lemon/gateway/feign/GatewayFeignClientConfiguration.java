package com.hisun.lemon.gateway.feign;

import javax.validation.Validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;

import com.hisun.lemon.framework.springcloud.fegin.LemonFeignClientsConfiguration;

import feign.codec.Encoder;

/**
 * @author yuzhou
 * @date 2017年8月12日
 * @time 下午2:38:29
 *
 */
@Configuration
@AutoConfigureBefore(LemonFeignClientsConfiguration.class)
public class GatewayFeignClientConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(GatewayFeignClientConfiguration.class);
    
    private ObjectFactory<HttpMessageConverters> messageConverters;
    
    private Validator validator;
    
    @Value("${feign.validation.enabled:false}")
    private boolean enabledValidated;
    
    public GatewayFeignClientConfiguration(ObjectFactory<HttpMessageConverters> messageConverters, Validator validator) {
        this.messageConverters = messageConverters;
        this.validator = validator;
    }
    
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnClass(name={"com.hisun.lemon.GatewayApplication"})
    public Encoder feignEncoder(@Qualifier("mvcConversionService") ConversionService conversionService) {
        if(logger.isInfoEnabled()) {
            logger.info("creating feign encoder object LemonSpringEncoder.");
        }
        GatewaySpringEncoder gatewaySpringEncoder = new GatewaySpringEncoder(this.messageConverters, conversionService, this.validator, this.enabledValidated);
        return gatewaySpringEncoder;
    }
}
