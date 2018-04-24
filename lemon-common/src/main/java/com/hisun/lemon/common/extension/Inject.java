package com.hisun.lemon.common.extension;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * SPI 依赖注入
 * @author yuzhou
 * @date 2017年9月23日
 * @time 下午3:21:56
 *
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface Inject {
    
    Type type() default Type.LIST;
    
    public enum Type {
        LIST, ADAPTIVE, ACTIVATE, RANDOM
    }
}
