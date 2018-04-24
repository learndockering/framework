package com.hisun.lemon.framework.idgenerate.auto;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

import com.hisun.lemon.common.exception.LemonException;
import com.hisun.lemon.common.scanner.ClassScanner;
import com.hisun.lemon.common.scanner.ScannerCallback;
import com.hisun.lemon.common.utils.AnnotationUtils;
import com.hisun.lemon.common.utils.ReflectionUtils;
import com.hisun.lemon.common.utils.StringUtils;

//@Component
public class AutoIdGenResolver implements BeanFactoryPostProcessor{
    private static final Logger logger = LoggerFactory.getLogger(AutoIdGenResolver.class);
    
    private static final String DEFAULT_DO_PACKAGE = "com.hisun.lemon.*.entity";
    private static final Map<Class<?>, IdGenMetedata> idGenMetadatas = new HashMap<>();
    private static final Map<Class<? extends IdGenStrategy>, IdGenStrategy> idGenStrategys = new HashMap<>();

    private String doPackage;
    
    
    public AutoIdGenResolver(){}
    
    public AutoIdGenResolver(String doPackage) {
        this.doPackage = doPackage;
    }
    
    @Override
    public void postProcessBeanFactory(
            ConfigurableListableBeanFactory beanFactory) throws BeansException {
        if(logger.isInfoEnabled()) {
            logger.info("Starting scan DO package {}.", getDOPackage());
        }
        ClassScanner scanner = new ClassScanner();
        scanner.scan(new ScannerCallback(){
            @Override
            public void callback(String className) {
                if(logger.isInfoEnabled()) {
                    logger.info("Finded DO class {}", className);
                }
                analyseDO(ReflectionUtils.forName(className));
            }} ,getDOPackage());
    }

    private void analyseDO(Class<?> clazz) {
        Field[] fields = ReflectionUtils.getDeclaredFields(clazz);
        Arrays.asList(fields).stream().filter(field -> AnnotationUtils.isAnnotationPresent(field, AutoIdGen.class))
            .forEach(field -> {
                analyseAutoIdgen(clazz, field);
            });
    }
    
    private void analyseAutoIdgen(Class<?> clazz, Field field) {
        AutoIdGen autoIdGen = AnnotationUtils.findAnnotation(field, AutoIdGen.class);
        Method setIdMethod = ReflectionUtils.getAccessibleWriteMethodByField(clazz, field);
        IdGenStrategy idGenStrategy = null;
        if (idGenStrategys.containsKey(autoIdGen.idGenStrategy())) {
            idGenStrategy = idGenStrategys.get(autoIdGen.idGenStrategy());
        } else {
            idGenStrategy = ReflectionUtils.newInstance(autoIdGen.idGenStrategy());
            idGenStrategys.put(autoIdGen.idGenStrategy(), idGenStrategy);
        }
        idGenMetadatas.put(clazz, new IdGenMetedata(StringUtils.getDefaultIfEmpty(autoIdGen.key(), autoIdGen.value()),autoIdGen.prefix(), setIdMethod, idGenStrategy));
    }
    
    /**
     * 解决自动ID赋值
     * @param obj
     */
    public void resolveIdGen(Object obj) {
        Class<?> clazz = obj.getClass();
        if(idGenMetadatas.containsKey(clazz)) {
            IdGenMetedata idGenMetadata = idGenMetadatas.get(clazz);
            try {
                idGenMetadata.getMethod().invoke(obj, idGenMetadata.getIdGenStrategy().genId(idGenMetadata.getKey(),idGenMetadata.getPrefix()));
            } catch (IllegalAccessException | IllegalArgumentException
                    | InvocationTargetException e) {
                throw new LemonException(e);
            }
        }
    }
    
    public String getDOPackage() {
        return StringUtils.getDefaultIfEmpty(this.doPackage, DEFAULT_DO_PACKAGE);
    }
    
    static class IdGenMetedata {
        private String key;
        private String prefix;
        private Method method;
        private IdGenStrategy idGenStrategy;
        
        public IdGenMetedata(String key, String prefix, Method method,
                IdGenStrategy idGenStrategy) {
            this.key = key;
            this.prefix = prefix;
            this.method = method;
            this.idGenStrategy = idGenStrategy;
        }
        
        public String getPrefix() {
            return prefix;
        }
        public void setPrefix(String prefix) {
            this.prefix = prefix;
        }
        public Method getMethod() {
            return method;
        }
        public void setMethod(Method method) {
            this.method = method;
        }
        public IdGenStrategy getIdGenStrategy() {
            return idGenStrategy;
        }
        public void setIdGenStrategy(IdGenStrategy idGenStrategy) {
            this.idGenStrategy = idGenStrategy;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }
        
    }

}
