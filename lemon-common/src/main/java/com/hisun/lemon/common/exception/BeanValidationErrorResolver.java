package com.hisun.lemon.common.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author yuzhou
 * @date 2017年9月15日
 * @time 下午2:41:04
 *
 */
public interface BeanValidationErrorResolver {
    Logger logger = LoggerFactory.getLogger(BeanValidationErrorResolver.class);
    
    boolean support(Throwable throwable);
    void resolve(Throwable throwable);
}
