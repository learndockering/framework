package com.hisun.lemon.common.extension;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;

@Configuration
public class LemonCommonConfiguration {
    
    @Bean
    public ExtensionLoader extensionLoader() {
        return new ExtensionLoader();
    }

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public SPIInjectBeanPostProcessor spiInjectBeanPostProcessor() {
        return new SPIInjectBeanPostProcessor();
    }
}
