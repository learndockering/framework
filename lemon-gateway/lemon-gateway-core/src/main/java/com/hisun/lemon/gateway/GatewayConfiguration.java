package com.hisun.lemon.gateway;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.DefaultErrorAttributes;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import com.hisun.lemon.common.utils.JudgeUtils;
import com.hisun.lemon.framework.config.LemonEnvironment;
import com.hisun.lemon.gateway.filter.GatewayEntryFilter;

/**
 * gateway configuration
 * @author yuzhou
 * @date 2017年7月25日
 * @time 下午12:44:33
 *
 */
@Configuration
public class GatewayConfiguration {
    
    @Configuration
    public static class LemonMvcConfiguration {
        
        @Autowired
        public LemonEnvironment lemonEnvironment;
        
        @Bean
        public LocaleResolver localeResolver() {
            AcceptHeaderLocaleResolver localeResolver = new AcceptHeaderLocaleResolver();
            localeResolver.setDefaultLocale(lemonEnvironment.getDefaultLocale());
            List<Locale> supports = lemonEnvironment.getSupportLocales();
            if(JudgeUtils.isNotEmpty(supports)) {
                localeResolver.setSupportedLocales(supports);
            }
            return localeResolver;
        }
    }
    
    @Configuration
    public static class ErrorMvcConfiguration {
        private String[] ignoreExceptionInfoArray = new String[]{"exception", "message"};
        @Bean
        public DefaultErrorAttributes errorAttributes(){
            return new DefaultErrorAttributes() {
                @Override
                public Map<String, Object> getErrorAttributes(RequestAttributes requestAttributes, boolean includeStackTrace) {
                    Map<String, Object> result = super.getErrorAttributes(requestAttributes, includeStackTrace);
                    String[] ignoreExceptionInfos = ignoreExceptionInfoArray;
                    Stream.of(ignoreExceptionInfos).forEach(s -> result.remove(s));
                    return result;
                }
            };
        }
    }
    
    @Configuration
    public static class WebConfiguration {
        private LocaleResolver localeResolver;
        
        public WebConfiguration(LocaleResolver localeResolver) {
            this.localeResolver = localeResolver;
        }
        
        @Bean
        public GatewayEntryFilter gatewayEntryFilter() {
            return new GatewayEntryFilter(localeResolver);
        }
        
        @Bean
        public FilterRegistrationBean gatewayEntryFilterRegistration() {
            FilterRegistrationBean registration = new FilterRegistrationBean();
            registration.setFilter(gatewayEntryFilter());
            registration.addUrlPatterns("/*");
            registration.setName("gatewayEntryFilter");
            registration.setOrder(Ordered.HIGHEST_PRECEDENCE);
            return registration;
        } 
    }
    
}
