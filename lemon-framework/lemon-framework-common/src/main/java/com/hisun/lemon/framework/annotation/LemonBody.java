package com.hisun.lemon.framework.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * GET请求参数为GenericDTO或其子类对象
 * @author yuzhou
 * @date 2017年8月22日
 * @time 下午1:07:51
 *
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
public @interface LemonBody {

}
