package com.hisun.lemon.framework.utils;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

/**
 * bean validation
 * JSR303,JSR349
 * 
 * @author yuzhou
 * @date 2017年7月8日
 * @time 下午2:55:57
 *
 */
public class ValidatorUtils {
    private static final String SEPARATOR_0 = ": ";
    private static final String SEPARATOR_1 = "; ";
    /**
     * 调用JSR303的validate方法, 验证失败时抛出ConstraintViolationException.
     */
    public static void validateWithException(Validator validator , Object object, Class<?>... groups)
            throws ConstraintViolationException {
        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(object, groups);
        if (!constraintViolations.isEmpty()) {
            throw new ConstraintViolationException(constraintViolations);
        }
    }
    
    /**
     * 
     * @param messages
     * @param object
     * @param groups
     * @return
     */
    public static boolean validate(Validator validator ,StringBuilder messages, Object object, Class<?>... groups) {
        try{
            validateWithException(validator, object, groups);
        }catch(ConstraintViolationException ex){
            ex.getConstraintViolations().stream().forEachOrdered(cv -> {
                if(messages.length() != 0) {
                    messages.append(SEPARATOR_1);
                }
                messages.append(cv.getPropertyPath()).append(SEPARATOR_0).append(cv.getMessage());
            });
            return false;
        }
        return true;
    }
}
