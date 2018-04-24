package com.hisun.lemon.common.extension;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * 标注在接口上，激活实现类
 * 标注在SPI实现类上，激活实现
 * 
 * @author yuzhou
 * @date 2017年4月20日
 * @time 上午11:42:38
 *
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Activate {
    public String value() default "";
}
