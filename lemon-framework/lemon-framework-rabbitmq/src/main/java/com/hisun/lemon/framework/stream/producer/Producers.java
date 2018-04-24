package com.hisun.lemon.framework.stream.producer;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 消息生产者
 * 注解在方法上，会自动把方法的返回值发送到Broken
 * @author yuzhou
 * @date 2017年8月10日
 * @time 下午7:17:17
 *
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Producers {
    Producer[] value();
}
