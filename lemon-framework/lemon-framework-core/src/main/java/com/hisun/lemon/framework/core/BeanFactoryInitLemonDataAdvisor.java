package com.hisun.lemon.framework.core;

import java.lang.annotation.Annotation;
import java.util.Collection;

import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractBeanFactoryPointcutAdvisor;

import com.hisun.lemon.common.utils.JudgeUtils;

/**
 * @author yuzhou
 * @date 2017年9月8日
 * @time 下午1:23:05
 *
 */
@SuppressWarnings("serial")
public class BeanFactoryInitLemonDataAdvisor extends AbstractBeanFactoryPointcutAdvisor {

    private InitLemonDataPointcut pointcut = new InitLemonDataPointcut();
    
    @Override
    public Pointcut getPointcut() {
        return pointcut;
    }
    
    public void addSupportInitLemonDataAnnoataion(Collection<Class<? extends Annotation>> supportAnnotations) {
        if(JudgeUtils.isNotEmpty(supportAnnotations)) {
            supportAnnotations.stream().filter(a -> ! pointcut.containInitLemonDataAnnotation(a)).forEach(pointcut::addInitLemonDataAnnotation);
        }
    }
}
