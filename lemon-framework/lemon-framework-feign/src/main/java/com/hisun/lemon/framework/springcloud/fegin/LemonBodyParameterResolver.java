package com.hisun.lemon.framework.springcloud.fegin;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.hisun.lemon.common.exception.LemonException;
import com.hisun.lemon.common.utils.JudgeUtils;
import com.hisun.lemon.common.utils.ReflectionUtils;
import com.hisun.lemon.framework.data.GenericDTO;

/**
 * @author yuzhou
 * @date 2017年8月23日
 * @time 下午8:43:08
 *
 */
public class LemonBodyParameterResolver {
    
    private Map<Class<?>,Map<String, Method>> readMethodsGenericClass = null;
    private static LemonBodyParameterResolver lemonBodyParameterResolver;
    
    private LemonBodyParameterResolver() {
        this.readMethodsGenericClass = new HashMap<>();
    }
    
    public static LemonBodyParameterResolver getInstance() {
        if(null == lemonBodyParameterResolver) {
            synchronized(LemonBodyParameterResolver.class) {
                if(null == lemonBodyParameterResolver) {
                    lemonBodyParameterResolver = new LemonBodyParameterResolver();
                }
            }
        }
        return lemonBodyParameterResolver;
    }
    
    public void resolverClass(Class<?> clazz) {
        if(! GenericDTO.class.isAssignableFrom(clazz)) {
            throw new IllegalArgumentException("Class \""+clazz+"\" must be assignable from \"GenericDTO\".");
        }
        readMethodsGenericClass.put(clazz, ReflectionUtils.getDeclareReadMethods(clazz));
    }
    
    public Object readParameterValue(Object object, Class<?> clazz, String parameterName) {
        Map<String, Method> readMethos = readMethodsGenericClass.get(clazz);
        if(JudgeUtils.isEmpty(readMethos)) {
            throw new IllegalStateException("Read methods do not be resloved for class \""+clazz+"\".");
        }
        Method readMethod = readMethos.get(parameterName);
        if(JudgeUtils.isNull(readMethod)) {
            throw new IllegalStateException("Pulic read method for property \""+parameterName+"\" does not exist in class \""+clazz+"\".");
        }
        try {
            Object[] args = null;
            return readMethod.invoke(object, args);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw LemonException.create(e);
        }
    }
}
