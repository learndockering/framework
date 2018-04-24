package com.hisun.lemon.framework.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.aop.support.StaticMethodMatcherPointcut;

import com.hisun.lemon.common.utils.AnnotationUtils;
import com.hisun.lemon.common.utils.JudgeUtils;

/**
 * @author yuzhou
 * @date 2017年9月8日
 * @time 下午2:01:25
 *
 */
public class InitLemonDataPointcut extends StaticMethodMatcherPointcut {

    private List<Class<? extends Annotation>> initLemonDataAnnotations = new ArrayList<>();
    
    {
        this.initLemonDataAnnotations.add(InitLemonData.class);
    }
    
    @Override
    public boolean matches(Method method, Class<?> targetClass) {
        boolean flag = false;
        for(Class<? extends Annotation> clazz : initLemonDataAnnotations) {
            Collection<? extends Annotation> initLemonDataAnnotations = AnnotationUtils.getAllMergedAnnotations(method, clazz);
            if(JudgeUtils.isNotEmpty(initLemonDataAnnotations)) {
                flag = true;
                break;
            }
        }
        return flag;
    }
    
    protected void addInitLemonDataAnnotation(Class<? extends Annotation> initLemonDataAnnotation) {
        this.initLemonDataAnnotations.add(initLemonDataAnnotation);
    }

    public List<Class<? extends Annotation>> getInitLemonDataAnnotations() {
        return this.initLemonDataAnnotations;
    }
    
    public boolean containInitLemonDataAnnotation(Class<? extends Annotation> clazz) {
        return this.getInitLemonDataAnnotations().contains(clazz);
    }
}
