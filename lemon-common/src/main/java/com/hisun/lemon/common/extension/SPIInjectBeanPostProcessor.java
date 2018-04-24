package com.hisun.lemon.common.extension;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.Ordered;

import com.hisun.lemon.common.utils.AnnotationUtils;
import com.hisun.lemon.common.utils.JudgeUtils;
import com.hisun.lemon.common.utils.ReflectionUtils;

/**
 * @author yuzhou
 * @date 2017年9月23日
 * @time 下午2:17:55
 *
 */
public class SPIInjectBeanPostProcessor implements Ordered, ApplicationContextAware, SmartInitializingSingleton, ApplicationListener<ContextRefreshedEvent>, BeanPostProcessor {
    private static final Logger logger = LoggerFactory.getLogger(SPIInjectBeanPostProcessor.class);
    private ApplicationContext applicationContext;
    private final Set<Class<?>> nonAnnotatedClasses = Collections.newSetFromMap(new ConcurrentHashMap<Class<?>, Boolean>(64));
    private List<SPIInjectMetedata> spiInjectMetedataList = new CopyOnWriteArrayList<>();
    
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (event.getApplicationContext() == this.applicationContext) {
            if(logger.isInfoEnabled()) {
                logger.info("Starting to inject SPI service. ");
            }
            if(JudgeUtils.isNotEmpty(this.spiInjectMetedataList)) {
                injectSPI(this.spiInjectMetedataList);
                this.spiInjectMetedataList.clear();
            }
        }
    }

    private void injectSPI(List<SPIInjectMetedata> spiInjectMetedataList) {
        spiInjectMetedataList.stream().parallel().forEach(s -> injectSPI(s));
    }
    
    private void injectSPI(SPIInjectMetedata spiInjectMetedata) {
        Object spiService = null;
        switch (spiInjectMetedata.getInject().type()) {
        case LIST:
            spiService = ExtensionLoader.getExtensionServices(spiInjectMetedata.getServiceType());
            break;
        case ADAPTIVE:
            spiService = ExtensionLoader.getExtensionAdaptiveService(spiInjectMetedata.getServiceType());
            break;
        case ACTIVATE:
            spiService = ExtensionLoader.getExtensionActivateService(spiInjectMetedata.getServiceType());
            break;
        case RANDOM:
            spiService = Optional.ofNullable(ExtensionLoader.getExtensionServices(spiInjectMetedata.getServiceType())).map(l -> l.get(0)).orElse(null);
            break;
        default:
            break;
        }
        if(JudgeUtils.isNotNull(spiService)) {
            ReflectionUtils.setField(spiInjectMetedata.getField(), spiInjectMetedata.getBean(), spiService);
            if(logger.isInfoEnabled()) {
                logger.info("Injected SPI service {} to object {} property {}.", spiService, spiInjectMetedata.getBean(), spiInjectMetedata.getField().getName());
            }
        }
    }
    
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName)
            throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> targetClass = AopUtils.getTargetClass(bean);
        if (!this.nonAnnotatedClasses.contains(targetClass)) {
            Field[] fields = targetClass.getDeclaredFields();
            if(JudgeUtils.isNotEmpty(fields)) {
                List<SPIInjectMetedata> spiInjectMetedataForBean = Stream.of(fields).parallel().filter(f -> AnnotationUtils.isAnnotationPresent(f, Inject.class))
                    .map(f -> resolveSPIInjectMetedata(bean, f)).collect(Collectors.toCollection(ArrayList::new));
                if (JudgeUtils.isNotEmpty(spiInjectMetedataForBean)) {
                    this.spiInjectMetedataList.addAll(spiInjectMetedataForBean);
                } else {
                    this.nonAnnotatedClasses.add(targetClass);
                }
            }
        }
        return bean;
    }
    

    private SPIInjectMetedata resolveSPIInjectMetedata(Object bean, Field f) {
        Class<?> spiServiceClass = f.getType();
        if(List.class.isAssignableFrom(f.getType())) {
            spiServiceClass = ReflectionUtils.getGenericClass(f);
        }
        return SPIInjectMetedata.instance(bean ,f, AnnotationUtils.findAnnotation(f, Inject.class), spiServiceClass);
    }

    @Override
    public void afterSingletonsInstantiated() {
        
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public int getOrder() {
        return LOWEST_PRECEDENCE;
    }

    public static class SPIInjectMetedata {
        private Object bean;
        private Field field;
        private Inject inject;
        private Class<?> serviceType;
        
        public SPIInjectMetedata(Object bean, Field field, Inject inject, Class<?> serviceType) {
            this.bean = bean;
            this.field = field;
            this.inject = inject;
            this.serviceType = serviceType;
        }
        public Object getBean() {
            return bean;
        }
        public void setBean(Object bean) {
            this.bean = bean;
        }
        public Field getField() {
            return field;
        }
        public void setField(Field field) {
            this.field = field;
        }
        public Inject getInject() {
            return inject;
        }
        public void setInject(Inject inject) {
            this.inject = inject;
        }
        public Class<?> getServiceType() {
            return serviceType;
        }
        public void setServiceType(Class<?> serviceType) {
            this.serviceType = serviceType;
        }

        public static SPIInjectMetedata instance(Object bean, Field field, Inject inject, Class<?> serviceType) {
            return new SPIInjectMetedata(bean, field, inject, serviceType);
        }
    }
}
