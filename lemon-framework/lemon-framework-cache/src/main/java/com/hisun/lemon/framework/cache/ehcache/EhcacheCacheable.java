package com.hisun.lemon.framework.cache.ehcache;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.cache.annotation.Cacheable;

/**
 * 
 * 使用ehcache
 * 
 * @author yuzhou
 * @date 2017年7月17日
 * @time 下午7:56:33
 *
 */
@Cacheable(cacheResolver="ehcacheCacheResoler")
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface EhcacheCacheable {

}
