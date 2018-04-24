package com.hisun.lemon.common.extension;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标注在实现类上，表示是适配器
 * @author yuzhou
 * @date 2017年9月22日
 * @time 下午4:33:20
 *
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Adaptive {
    
}
