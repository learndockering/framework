package com.hisun.lemon.framework.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * GenericDTO子类不作为parameter的property
 * @author yuzhou
 * @date 2017年8月23日
 * @time 下午6:02:01
 *
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface IgnoreLemonBody {

}
