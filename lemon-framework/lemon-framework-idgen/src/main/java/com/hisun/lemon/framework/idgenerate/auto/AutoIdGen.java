package com.hisun.lemon.framework.idgenerate.auto;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.core.annotation.AliasFor;

/**
 * 注解DO ID property
 * 生成ID值并自动赋值
 * 
 * @author yuzhou
 * @date 2017年6月19日
 * @time 上午10:36:55
 *
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface AutoIdGen {
    @AliasFor("key")
    String value() default "";
    
    /**
     * ID对应redis的key
     * @return
     */
    @AliasFor("value")
    String key() default "";
    
    /**
     * Id前缀
     * 
     * @return
     */
    String prefix() default "";
    
    /**
     * Id 生成策略
     * @return
     */
    Class<? extends IdGenStrategy> idGenStrategy() default DefaultIdGenStrategy.class;
}
