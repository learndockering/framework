package com.hisun.lemon.framework.validation;

import javax.validation.Validation;
import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hisun.lemon.common.extension.ExtensionLoader;
/**
 * Validator工厂
 * SringBoot 环境优先springboot validator
 * @author yuzhou
 * @date 2017年5月17日
 * @time 上午9:52:00
 *
 */
@Component("beanValidatorFactory")
public class BeanValidatorFactory {
    
    /**
     * 单例模式
     * 
     */
    private BeanValidator beanValidator;
    
    @Autowired
    private Validator validator;
    
    /**
     * 获取validatorFactory
     * @return
     */
    public static BeanValidatorFactory getValidatorFactory() {
        BeanValidatorFactory factory = ExtensionLoader.getSpringBean("beanValidatorFactory", BeanValidatorFactory.class);
        if(null != factory) {
            return factory;
        }
        return InnerValidatorFactory.validatorFactory;
    }
    
    private static class InnerValidatorFactory {
        static final BeanValidatorFactory validatorFactory = new BeanValidatorFactory();
    }
    
    private BeanValidatorFactory() {
    }
    
    /**
     * 获取Validator
     * @return
     */
    public BeanValidator getBeanValidator() {
        if(null != beanValidator){
            return beanValidator;
        }
        synchronized(this) {
            if(null != beanValidator){
                return beanValidator;
            }
            if(null != validator) {
                beanValidator = new BeanValidator(validator, ValidatorType.SPRING_BOOT);
            } else {
                beanValidator = new BeanValidator(createBeanValidator(), ValidatorType.BEAN_VALIDATOR);
            }
        }
        
        return this.beanValidator;
    }
    
    /**
     * 采用java SPI机制，可以自动发现hibernate validator
     * @return
     */
    private Validator createBeanValidator() {
        javax.validation.ValidatorFactory factory = Validation.buildDefaultValidatorFactory(); 
        Validator validator = factory.getValidator();
        return validator;
    }
    
    enum ValidatorType {
        SPRING_BOOT, BEAN_VALIDATOR
    }
}
