package com.hisun.lemon.framework.datasource;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

/**
 * 动态数据源AOP
 * @author yuzhou
 * @date 2017年7月7日
 * @time 下午2:27:41
 *
 */
@Component
@Aspect
public class DynamicDataSourceAspect implements Ordered {
    private Logger logger = LoggerFactory.getLogger(DynamicDataSourceAspect.class);
    
    @Before("@annotation(targetDataSource)")
    public void changeDataSource(JoinPoint joinPoint, TargetDataSource targetDataSource) {
        DynamicDataSource.setDatasource(targetDataSource.value());
        if(logger.isDebugEnabled()) {
            logger.debug("dataSource change to {}" , targetDataSource.value());
        }
    }
    
    @After("@annotation(targetDataSource)")
    public void releaseDataSource(JoinPoint joinPoint, TargetDataSource targetDataSource) {
        DynamicDataSource.clearDatasource();
        if(logger.isDebugEnabled()) {
            logger.debug("clear dataSource after.");
        }
    }
    
    @AfterThrowing("@annotation(targetDataSource)")
    public void releaseDataSourceAfterThrowing(JoinPoint joinPoint, TargetDataSource targetDataSource) {
        DynamicDataSource.clearDatasource();
        if(logger.isDebugEnabled()) {
            logger.debug("clear dataSource after throwing.");
        }
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 1;
    }
    
}
