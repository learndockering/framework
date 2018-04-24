package com.hisun.lemon.framework.utils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;

import com.hisun.lemon.common.exception.BeanValidationErrorResolver;
import com.hisun.lemon.common.extension.ExtensionLoader;
import com.hisun.lemon.common.utils.JudgeUtils;

/**
 * @author yuzhou
 * @date 2017年7月19日
 * @time 下午2:12:20
 *
 */
public class BeanValidationInvalidHelper {
    private static final Logger logger = LoggerFactory.getLogger(BeanValidationInvalidHelper.class);
    
    private static List<BeanValidationErrorResolver> beanValidationErrorResolvers;
    
    static {
        beanValidationErrorResolvers = ExtensionLoader.getExtensionServices(BeanValidationErrorResolver.class);
        if(logger.isInfoEnabled()) {
            logger.info("bean validation error resolvers {}", beanValidationErrorResolvers);
        }
    }
    
    /**
     * 校验结果详情
     * @param throwable
     */
    public static void resolveBeanValidationInvalidIfNecessary(Throwable throwable) {
        if(throwable instanceof Exception) {
            resolveBeanValidationInvalidIfNecessary((Exception) throwable);
        }
    }
    
    /**
     * 校验结果详情
     * @param exception
     * @param logger
     */
    public static void resolveBeanValidationInvalidIfNecessary(Exception exception) {
        boolean resolved = false;
        if(JudgeUtils.isNotEmpty(beanValidationErrorResolvers)) {
            for(BeanValidationErrorResolver resolver : beanValidationErrorResolvers) {
                if(resolver.support(exception)) {
                    resolver.resolve(exception);
                    resolved = true;
                    break;
                }
            }
        }
        if(resolved) {
            return ;
        }
        if (exception instanceof BindException) {
            BindException error = (BindException) exception;
            String msg = error.getBindingResult().getFieldErrors().stream().map(fieldError -> fieldError.getField()+":"+fieldError.getDefaultMessage())
                .collect(Collectors.joining("~"));
            if(logger.isErrorEnabled()) {
                logger.error("Bind exception : {} ", msg);
            }
            return;
        }
        if(null != exception.getCause() && exception.getCause() instanceof ConstraintViolationException) {
            ConstraintViolationException cve = (ConstraintViolationException) exception.getCause();
            Set<ConstraintViolation<?>> cvs = cve.getConstraintViolations();
            if(JudgeUtils.isNotNull(cvs)) {
                String msg = cvs.stream().map(cv -> cv.getRootBeanClass().getSimpleName() + "."+ cv.getPropertyPath()+":"+cv.getMessage()).collect(Collectors.joining("~"));
                if(logger.isErrorEnabled()) {
                    logger.error("Constraint violation : {}",msg);
                }
            }
            
        }
    }
}
