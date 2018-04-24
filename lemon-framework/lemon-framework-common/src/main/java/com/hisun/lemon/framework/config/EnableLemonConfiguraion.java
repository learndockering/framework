package com.hisun.lemon.framework.config;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * lemon configuration
 * @author yuzhou
 * @date 2017年7月25日
 * @time 下午12:52:07
 *
 */
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Target({ java.lang.annotation.ElementType.TYPE })
@Documented
@Import(LemonEnvironment.class)
@Configuration
public @interface EnableLemonConfiguraion {

}

