package com.hisun.lemon.framework.springcloud.fegin;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.cloud.netflix.feign.AnnotatedParameterProcessor;
import org.springframework.cloud.netflix.feign.FeignClientsConfiguration;
import org.springframework.cloud.netflix.feign.annotation.PathVariableParameterProcessor;
import org.springframework.cloud.netflix.feign.annotation.RequestHeaderParameterProcessor;
import org.springframework.cloud.netflix.feign.annotation.RequestParamParameterProcessor;
import org.springframework.cloud.netflix.feign.support.SpringDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;

import com.fasterxml.jackson.databind.ObjectMapper;

import feign.codec.Decoder;
import feign.codec.Encoder;

/**
 * 客户化feign配置
 * @author yuzhou
 * @date 2017年7月4日
 * @time 上午11:28:27
 * 
 * @see FeignClientsConfiguration
 */
@Configuration
public class LemonFeignClientsConfiguration{
    private static final Logger logger = LoggerFactory.getLogger(LemonFeignClientsConfiguration.class);
    
    private ObjectFactory<HttpMessageConverters> messageConverters;
    
    private ObjectMapper objectMapper;
    
    private Validator validator;
    
    @Value("${feign.validation.enabled:false}")
    private boolean enabledValidated;
    
    public LemonFeignClientsConfiguration(ObjectFactory<HttpMessageConverters> messageConverters, ObjectMapper objectMapper, Validator validator) {
        this.messageConverters = messageConverters;
        this.objectMapper = objectMapper;
        this.validator = validator;
    }
    
    @Bean
    public Decoder feignDecoder() {
        if(logger.isInfoEnabled()) {
            logger.info("creating spring bean LemonResponseEntityDecoder.");
        }
        return new LemonResponseEntityDecoder(new SpringDecoder(this.messageConverters), objectMapper);
    }
    
    @Bean
    @ConditionalOnMissingClass("com.hisun.lemon.GatewayApplication")
    public Encoder feignEncoder(@Qualifier("mvcConversionService") ConversionService conversionService) {
        if(logger.isInfoEnabled()) {
            logger.info("creating feign encoder object LemonSpringEncoder.");
        }
        return new LemonSpringEncoder(this.messageConverters, conversionService, validator, enabledValidated);
    }
    
    @Bean
    public List<AnnotatedParameterProcessor> parameterProcessors() {
        List<AnnotatedParameterProcessor> annotatedArgumentResolvers = new ArrayList<>();
        annotatedArgumentResolvers.add(new PathVariableParameterProcessor());
        annotatedArgumentResolvers.add(new RequestParamParameterProcessor());
        annotatedArgumentResolvers.add(new RequestHeaderParameterProcessor());
        annotatedArgumentResolvers.add(new LemonBodyParameterProcessor());
        return annotatedArgumentResolvers;
    }
    
/*    @Bean
    public FormattingConversionService feignConversionService() {
        FormattingConversionService conversionService = new DefaultFormattingConversionService();
        return conversionService;
    }*/
    
    /*
    @Bean
    @ConditionalOnMissingBean
    public Retryer feignRetryer() {
        return Retryer.NEVER_RETRY;
    }
    
    @Bean
    @Scope("prototype")
    @ConditionalOnMissingBean
    public Feign.Builder feignBuilder(Retryer retryer) {
        //client(new LemonClient(null, null))不起作用，见FeignRibbonClientAutoConfiguration,自己创建feign.Client.Default
        //return Feign.builder().retryer(retryer).logLevel(feign.Logger.Level.NONE).client(new LemonClient(null, null));
    }*/

  /*  @Bean
    @ConditionalOnMissingBean(FeignLoggerFactory.class)
    public FeignLoggerFactory feignLoggerFactory() {
        return new DefaultFeignLoggerFactory(logger);
    }*/
}
