package com.hisun.lemon.framework.cache;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

/**
 * enable mix cache
 * @author yuzhou
 * @date 2017年7月18日
 * @time 上午8:51:48
 *
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@Import({MixCacheConfiguration.class})
public @interface EnableMixCache {
    
}
