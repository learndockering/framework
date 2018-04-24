package com.hisun.lemon.framework.aop;

import java.time.Instant;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.hisun.lemon.common.utils.DateTimeUtils;
import com.hisun.lemon.framework.data.BaseDO;
import com.hisun.lemon.framework.idgenerate.auto.AutoIdGenResolver;

@Aspect
@Configuration
public class DaoAspect {
    private static final Logger logger = LoggerFactory.getLogger(DaoAspect.class);
    
    @Autowired
    private AutoIdGenResolver autoIdGenResolver;
    
    @Pointcut("execution (* com.hisun..*Dao.*(..))")
    public void anyhisunDaoMethod(){
    }
    
    @Pointcut("@within(org.apache.ibatis.annotations.Mapper)")
    public void anyMapperMethod(){}
    
    @Around("anyhisunDaoMethod()||anyMapperMethod()")
    public Object doAroundDao(ProceedingJoinPoint pjp) throws Throwable{
        Instant startInstant = Instant.now();
        String methodName = pjp.getSignature().getName();
        
        Object obj = null;
        try{
            Object[] args = pjp.getArgs();
            if(StringUtils.startsWith(methodName, "insert")) {
                Stream.of(args).filter(arg -> arg instanceof BaseDO).forEach(baseDO ->{
                    autoIdGenResolver.resolveIdGen(baseDO);
                    ((BaseDO)baseDO).preInsert();
                });
            }
            
            if(StringUtils.startsWith(methodName, "update")) {
                Stream.of(args).filter(arg -> arg instanceof BaseDO).forEach(baseDao ->{
                    ((BaseDO)baseDao).preUpdate();
                });
            }
            
            obj = pjp.proceed();
        }catch(Throwable e){
            if(logger.isErrorEnabled()) {
                logger.error("Unexpected err occured at executing dao method, ",e);
            }
            throw e;
        }finally{
            if(logger.isDebugEnabled()) {
                logger.debug("executed {}/s in method {}", DateTimeUtils.durationMillis(startInstant, Instant.now()), methodName);
            }
        }
        return obj;
    }
}
