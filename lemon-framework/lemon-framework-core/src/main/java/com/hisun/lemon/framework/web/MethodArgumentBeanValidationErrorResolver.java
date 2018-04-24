package com.hisun.lemon.framework.web;

import java.util.stream.Collectors;

import org.springframework.web.bind.MethodArgumentNotValidException;

import com.hisun.lemon.common.exception.BeanValidationErrorResolver;

/**
 * SPI
 * @author yuzhou
 * @date 2017年9月15日
 * @time 下午2:43:59
 *
 */
public class MethodArgumentBeanValidationErrorResolver implements
        BeanValidationErrorResolver {

    @Override
    public boolean support(Throwable throwable) {
        if (throwable instanceof MethodArgumentNotValidException) {
            return true;
        }
        return false;
    }

    @Override
    public void resolve(Throwable throwable) {
        MethodArgumentNotValidException error = (MethodArgumentNotValidException) throwable;
        String msg = error.getBindingResult().getFieldErrors().stream().map(fieldError -> fieldError.getField()+":"+fieldError.getDefaultMessage())
            .collect(Collectors.joining("~"));
        if(logger.isErrorEnabled()) {
            logger.error("Method argument not valid : {} ", msg);
        }
    }

}
