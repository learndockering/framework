package com.hisun.lemon.gateway.zuul;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.netflix.zuul.filters.RouteLocator;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.cloud.netflix.zuul.filters.route.ZuulFallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.util.UrlPathHelper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hisun.lemon.gateway.common.LemonObjectMapper;
import com.hisun.lemon.gateway.common.config.ZuulExtensionProperties;
import com.hisun.lemon.gateway.common.response.ResponseMessageResolver;
import com.hisun.lemon.gateway.common.signature.SignatureResolver;
import com.hisun.lemon.gateway.service.ISecurityService;
import com.hisun.lemon.gateway.zuul.filter.ZuulFilterConfiguration;
import com.hisun.lemon.gateway.zuul.hystrix.fallback.ResponseZuulFallbackProvider;
import com.hisun.lemon.gateway.zuul.signature.ZuulSignatureResolver;

/**
 * @author yuzhou
 * @date 2017年9月8日
 * @time 下午9:34:00
 *
 */
@Configuration
@EnableZuulProxy
@Import(ZuulFilterConfiguration.class)
public class ZuulConfiguration {
    
    @Configuration
    @EnableConfigurationProperties({ ZuulExtensionProperties.class })
    public static class ZuulHelperConfiguration {
        @Bean
        public ZuulHelper zuulHelper(ZuulExtensionProperties zuulExtensionProperties, ZuulProperties properties, 
                RouteLocator routeLocator, ObjectMapper objectMapper, ISecurityService signatureService) {
            UrlPathHelper urlPathHelper = new UrlPathHelper();
            urlPathHelper.setRemoveSemicolonContent(properties.isRemoveSemicolonContent());
            return new ZuulHelper(new LemonObjectMapper(objectMapper), urlPathHelper, routeLocator, zuulExtensionProperties, signatureService);
        }
    }
    
    @Bean
    public SignatureResolver signatureResolver(ZuulHelper zuulHelper) {
        return new ZuulSignatureResolver(zuulHelper);
    }
    
    @Bean
    public ZuulFallbackProvider responseZuulFallbackProvider(ResponseMessageResolver responseMessageResolver) {
        return new ResponseZuulFallbackProvider(responseMessageResolver);
    }
}
