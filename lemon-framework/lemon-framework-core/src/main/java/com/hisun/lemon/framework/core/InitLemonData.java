package com.hisun.lemon.framework.core;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 初始化LemonData
 * @author yuzhou
 * @date 2017年9月7日
 * @time 下午7:47:18
 *
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface InitLemonData {
    
}
