package com.hisun.lemon.framework.schedule.batch;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

/**
 * @author yuzhou
 * @date 2017年9月5日
 * @time 下午9:34:06
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(BatchSchedulingConfiguration.class)
@Documented
public @interface EnableBatchScheduling {

}
