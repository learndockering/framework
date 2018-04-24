package com.hisun.lemon.framework.validation;

import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

import org.springframework.validation.annotation.Validated;

import com.hisun.lemon.common.utils.AnnotationUtils;
import com.hisun.lemon.framework.utils.ValidatorUtils;
import com.hisun.lemon.framework.validation.BeanValidatorFactory.ValidatorType;

/**
 * Bean validator
 * @author yuzhou
 * @date 2017年5月17日
 * @time 下午12:56:34
 *
 */
public class BeanValidator {
    
    private Validator validator;
    
    private ValidatorType validatorType;
    
    public BeanValidator(Validator validator, ValidatorType validatorType) {
        this.validator = validator;
        this.validatorType = validatorType;
    }
    
    public boolean validate(StringBuilder messages, Object object, Class<?>... groups) {
        Validated validated = AnnotationUtils.findAnnotation(object.getClass(), Validated.class);
        if( null == validated) {
            return true;
        }
        if(null == groups) {
            groups = validated.value();
        }
        return ValidatorUtils.validate(validator, messages, object, groups);
    }
    
    /**
     * 调用JSR303的validate方法, 验证失败时抛出ConstraintViolationException.
     */
    public void validateWithException(Object object, Class<?>... groups)
            throws ConstraintViolationException {
        ValidatorUtils.validateWithException(validator, object, groups);
    }
    
    public ValidatorType getValidatorType() {
        return this.validatorType;
    }
    
}
