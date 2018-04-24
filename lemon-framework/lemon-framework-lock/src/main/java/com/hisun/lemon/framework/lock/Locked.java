package com.hisun.lemon.framework.lock;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标注一个方法，表示该方法会用分布式锁锁定
 * @author yuzhou
 * @date 2017年7月20日
 * @time 上午11:45:45
 *
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Locked {
    /**
     * 锁名
     * @return
     */
    String lockName();
    /**
     * 等待锁超时时间，单位：秒
     * 默认30s
     * @return
     */
    int waitTime() default LockConfiguration.DEFAULT_WAIT_TIME;
    
    /**
     * 自动解锁时间，单位秒
     * 自动解锁时间一定得大于方法执行时间，否则会导致锁提前释放
     * 默认100s
     * @return
     */
    int leaseTime() default LockConfiguration.DEFAULT_LEASE_TIME;
    
    /**
     * 忽略所有异常，否则会往外抛
     * @return
     */
    boolean ignoreException() default false;
    
    /**
     * 忽略没有获取到锁的异常，默认为true
     * 
     * @return
     */
    boolean ignoreUnableToAquireLockException() default true;
}
