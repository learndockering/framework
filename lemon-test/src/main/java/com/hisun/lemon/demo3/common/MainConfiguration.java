package com.hisun.lemon.demo3.common;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

//@ContextConfiguration(initializers = ConfigFileApplicationContextInitializer.class)
@Configuration
public class MainConfiguration {
    
    @Autowired
    private TestSetting testSetting;
    
    @PostConstruct
    public void aa() {
        System.out.println(testSetting.getName());
    }
    
}
