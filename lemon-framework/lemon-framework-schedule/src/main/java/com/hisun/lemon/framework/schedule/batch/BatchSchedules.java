package com.hisun.lemon.framework.schedule.batch;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author yuzhou
 * @date 2017年9月5日
 * @time 下午8:07:11
 *
 */
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface BatchSchedules {
    BatchScheduled[] value();
}
