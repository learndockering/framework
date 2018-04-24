package com.hisun.lemon.framework.datasource;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 指定数据源
 * @author yuzhou
 * @date 2017年7月7日
 * @time 下午2:25:38
 *
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface TargetDataSource {
    /**
     * 数据源名称，默认为lemon
     * @return
     */
    String value() default "lemon";
}
