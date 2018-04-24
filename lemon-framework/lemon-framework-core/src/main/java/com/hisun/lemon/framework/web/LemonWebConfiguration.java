package com.hisun.lemon.framework.web;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.DefaultErrorAttributes;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.web.bind.support.ConfigurableWebBindingInitializer;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import com.hisun.lemon.framework.web.bind.generic.LemonServletModelAttributeMethodProcessor;
import com.hisun.lemon.framework.web.converter.LocalDateTimeToStringConverter;
import com.hisun.lemon.framework.web.converter.LocalDateToStringConverter;
import com.hisun.lemon.framework.web.converter.LocalTimeToStringConverter;
import com.hisun.lemon.framework.web.converter.StringToLocalDateConverter;
import com.hisun.lemon.framework.web.converter.StringToLocalDateTimeConverter;
import com.hisun.lemon.framework.web.converter.StringToLocalTimeConverter;
import com.hisun.lemon.framework.web.filter.TradeEntryFilter;

/**
 * lemon web 相关配置
 * @author yuzhou
 * @date 2017年9月9日
 * @time 下午2:45:56
 *
 */
@Configuration
public class LemonWebConfiguration {

    @Configuration
    public static class WebConfiguration {
        
        @Bean
        public TradeEntryFilter tradeEntryFilter() {
            return new TradeEntryFilter();
        }
        
        @Bean
        public FilterRegistrationBean tradeEntryFilterRegistration() {
            FilterRegistrationBean registration = new FilterRegistrationBean();
            registration.setFilter(tradeEntryFilter());
            registration.addUrlPatterns("/*");
            registration.setName("tradeEntryFilter");
            registration.setOrder(Ordered.HIGHEST_PRECEDENCE);
            return registration;
        } 
    }
    
    @Configuration
    public static class WebBindingConfiguration {
        @Autowired
        private RequestMappingHandlerAdapter handlerAdapter;
        
        @PostConstruct
        public void addConversionConfig() {
            ConfigurableWebBindingInitializer initializer = (ConfigurableWebBindingInitializer)handlerAdapter.getWebBindingInitializer();
            if (initializer.getConversionService() != null) {
                GenericConversionService genericConversionService = (GenericConversionService)initializer.getConversionService();
                genericConversionService.addConverter(new StringToLocalDateConverter());
                genericConversionService.addConverter(new StringToLocalDateTimeConverter());
                genericConversionService.addConverter(new StringToLocalTimeConverter());
                genericConversionService.addConverter(new LocalTimeToStringConverter());
                genericConversionService.addConverter(new LocalDateTimeToStringConverter());
                genericConversionService.addConverter(new LocalDateToStringConverter());
            }
        }
    }
    
    @Configuration
    public static class WebMvcConfigurer extends WebMvcConfigurerAdapter {
        @Override
        public void addInterceptors(InterceptorRegistry registry) {
            //registry.addInterceptor(new LocaleChangeInterceptor());
        }
        
        @Override
        public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
            super.addArgumentResolvers(argumentResolvers);
            argumentResolvers.add(new LemonServletModelAttributeMethodProcessor());
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
}
