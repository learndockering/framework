package com.hisun.lemon.framework.actuator.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

import com.hisun.lemon.framework.actuator.filter.ShutdownFilter;

/**
 * @author yuzhou
 * @date 2017年9月21日
 * @time 下午3:26:52
 *
 */
@Configuration
public class ActuatorConfiguration {
    
    @Configuration
    public static class WebConfiguration {
        
        @Bean
        public ShutdownFilter shutdownFilter() {
            return new ShutdownFilter();
        }
        
        @Bean
        public FilterRegistrationBean shutdownFilterRegistration() {
            FilterRegistrationBean registration = new FilterRegistrationBean();
            registration.setFilter(shutdownFilter());
            registration.addUrlPatterns("/shutdown");
            registration.setName("shutdownFilter");
            registration.setOrder(Ordered.HIGHEST_PRECEDENCE-1);
            return registration;
        } 
    }
}
