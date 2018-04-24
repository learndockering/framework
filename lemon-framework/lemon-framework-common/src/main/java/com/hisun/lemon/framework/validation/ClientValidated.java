package com.hisun.lemon.framework.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标注在客户端进行bean validate
 * @author yuzhou
 * @date 2017年7月8日
 * @time 下午3:06:55
 *
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface ClientValidated {
    /**
     * 分组功能
     * @return
     */
    Class<?>[] value() default {};

}
