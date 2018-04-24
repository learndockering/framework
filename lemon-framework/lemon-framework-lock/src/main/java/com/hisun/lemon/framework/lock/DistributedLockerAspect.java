package com.hisun.lemon.framework.lock;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

import com.hisun.lemon.common.exception.LemonException;
import com.hisun.lemon.common.utils.Validate;

/**
 * 分布式锁Aspect
 * @author yuzhou
 * @date 2017年7月20日
 * @time 上午11:51:42
 *
 */
@Configuration
@Aspect
public class DistributedLockerAspect implements Ordered {
    private static final Logger logger = LoggerFactory.getLogger(DistributedLockerAspect.class);
    
    @Autowired
    private DistributedLocker distributedLocker;

    @Around("@annotation(locked)")
    public void lock(ProceedingJoinPoint pjp, Locked locked) {
        Validate.notEmpty(locked.lockName());
        try {
            distributedLocker.lock(locked.lockName(), locked.leaseTime(), locked.waitTime(), 
            () -> {return proceed(pjp);});
        } catch (UnableToAquireLockException e) {
            if(!(locked.ignoreUnableToAquireLockException() || locked.ignoreException())) {
                LemonException.throwLemonException(e);
            }
            if(logger.isWarnEnabled()) {
                logger.warn("Method {} has not aquired lock with lock name {}, lease time {}, wait time {}", 
                    pjp.getSignature().getName(), locked.lockName(), locked.leaseTime(), locked.waitTime());
            }
        } catch (LemonException e) {
            if(! locked.ignoreException()) {
                throw e;
            }
            if(logger.isWarnEnabled()) {
                logger.warn("Lemon exception accured at lock aspect. ", e);
            }
        } catch (Throwable e) {
            if(! locked.ignoreException()) {
                LemonException.throwLemonException(e);
            }
            if(logger.isWarnEnabled()) {
                logger.warn("Unexpected throwable accured at lock aspect. ", e);
            }
        }
    }
    
    public Object proceed(ProceedingJoinPoint pjp) {
        try {
            return pjp.proceed();
        } catch (Throwable e) {
            LemonException.throwLemonException(e);
            return null;
        }
    }
    
    
    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE-10;
    }

}
