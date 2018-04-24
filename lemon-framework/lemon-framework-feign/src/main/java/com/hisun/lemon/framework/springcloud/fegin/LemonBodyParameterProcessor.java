package com.hisun.lemon.framework.springcloud.fegin;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import javax.ws.rs.HttpMethod;

import org.springframework.cloud.netflix.feign.AnnotatedParameterProcessor;

import com.hisun.lemon.common.utils.AnnotationUtils;
import com.hisun.lemon.common.utils.ReflectionUtils;
import com.hisun.lemon.framework.annotation.IgnoreLemonBody;
import com.hisun.lemon.framework.annotation.LemonBody;
import com.hisun.lemon.framework.data.GenericDTO;
import com.hisun.lemon.framework.data.GenericDTOFields;

import feign.MethodMetadata;

/**
 * @author yuzhou
 * @date 2017年8月23日
 * @time 下午3:58:15
 *
 */
public class LemonBodyParameterProcessor implements AnnotatedParameterProcessor {

    private static final Class<LemonBody> ANNOTATION = LemonBody.class;
    public static final String LEMON_BODY_HEADER_KEY = "x-lemon-body";
    private LemonBodyParameterResolver lemonBodyParameterResolver = LemonBodyParameterResolver.getInstance();
    
    @Override
    public Class<? extends Annotation> getAnnotationType() {
        return ANNOTATION;
    }

    @Override
    public boolean processArgument(AnnotatedParameterContext context, Annotation annotation, Method method) {
        int parameterIndex = context.getParameterIndex();
        Class<?> parameterType = method.getParameterTypes()[parameterIndex];
        MethodMetadata metadata = context.getMethodMetadata();
        if(! GenericDTO.class.isAssignableFrom(parameterType)) {
            throw new IllegalArgumentException("Parameter annotationed by \"@LemonBody\" type must be assignable from GenericDTO, parameter index \""+parameterIndex +"\", method \""+method.getName()+"\".");
        }
        if(! HttpMethod.GET.toString().equals(metadata.template().method())) {
            throw new IllegalArgumentException("Annotation \"@LemonBody\" can only use for method \"GET\", illegal method \""+method.getName()+"\"");
        }
        String[] paramNames = fetchParamsByType(parameterType);
        metadata.template().header(LEMON_BODY_HEADER_KEY, paramNames);
        lemonBodyParameterResolver.resolverClass(parameterType);
        //not a http annotation
        return false;
    }
    
    private String[] fetchParamsByType(Class<?> parameterType) {
        List<String> parameterNames = new ArrayList<>();
        if(! ReflectionUtils.sameClass(GenericDTO.class, parameterType)) {
            Stream.of(ReflectionUtils.getDeclaredFields(parameterType)).filter(f -> !AnnotationUtils.isAnnotationPresent(f, IgnoreLemonBody.class)).forEach(c -> parameterNames.add(c.getName()));
        }
        Stream.of(GenericDTOFields.LEMON_FIELDS).forEach(parameterNames::add);
        return parameterNames.toArray(new String[parameterNames.size()]);
    }
    
}
