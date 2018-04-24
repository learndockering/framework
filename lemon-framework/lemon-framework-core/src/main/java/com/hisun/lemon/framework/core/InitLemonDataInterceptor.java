package com.hisun.lemon.framework.core;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import com.hisun.lemon.common.context.LemonContext;
import com.hisun.lemon.framework.data.LemonHolder;

/**
 * @author yuzhou
 * @date 2017年9月8日
 * @time 下午2:16:51
 *
 */
public class InitLemonDataInterceptor implements MethodInterceptor {
    private LemonDataInitializer lemonDataInitializer;
    
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        try{
            lemonDataInitializer.initLemonData();
            return invocation.proceed();
        } finally {
            LemonHolder.clear();
            LemonContext.clearCurrentContext();
        }
    }

    public void setLemonDataInitializer(LemonDataInitializer lemonDataInitializer) {
        this.lemonDataInitializer = lemonDataInitializer;
    }
}
