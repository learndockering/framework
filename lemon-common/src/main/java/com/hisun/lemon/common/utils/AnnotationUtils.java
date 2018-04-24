package com.hisun.lemon.common.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.core.annotation.AnnotatedElementUtils;

/**
 * @author yuzhou
 * @date 2017年6月14日
 * @time 上午10:45:30
 *
 */
public class AnnotationUtils{
    
    public static final Map<Class<?>,Map<Class<?>,Annotation>> classAnnotationMaps = new ConcurrentHashMap<>();
    
    public static <T extends Annotation> T findAnnotation(Field field,Class<T> annotationType) {
        if (null == field || null == annotationType){
            return null;
        }
        //TODO 可以缓存，待优化
        return field.getAnnotation(annotationType);
    }
    
    public static <T extends Annotation> List<T> findAnnotations(Field field,Class<T> annotationType) {
        if (null == field || null == annotationType){
            return null;
        }
        //TODO 可以缓存，待优化
        return Arrays.asList(field.getAnnotationsByType(annotationType));
    }
    
    @SuppressWarnings("unchecked")
    public static <T extends Annotation> T findAnnotation(Class<?> clazz, Class<T> annotationType) {
        if (null == clazz || null == annotationType){
            return null;
        }
        Map<Class<?>, Annotation> annotationMaps = classAnnotationMaps.get(clazz);
        if(null == annotationMaps ){
            synchronized (AnnotationUtils.class) {
                if(null == classAnnotationMaps.get(clazz)) {
                    annotationMaps = new ConcurrentHashMap<>();
                    classAnnotationMaps.put(clazz, annotationMaps);
                }
                annotationMaps = classAnnotationMaps.get(clazz);
            }
        } 
        Annotation annotation = annotationMaps.get(annotationType);
        if(null != annotation) {
            return (T) annotation;
        }
        if(null == annotationMaps.get(annotationType)) {
            synchronized (AnnotationUtils.class) {
                if(null == annotationMaps.get(annotationType)) {
                    annotation = clazz.getAnnotation(annotationType);
                    if(null == annotation) {
                        return null;
                    }
                    annotationMaps.put(annotationType, annotation);
                }
            }
        }
        return (T)annotationMaps.get(annotationType);
    }
    
    public static <T extends Annotation> List<T> findAnnotations(Class<?> clazz,Class<T> annotationType) {
        if (null == clazz || null == annotationType){
            return null;
        }
        //TODO 可以缓存，待优化
        return Arrays.asList(clazz.getAnnotationsByType(annotationType));
    }
    
    public static <T extends Annotation> boolean isAnnotationPresent(Field field ,Class<T> annotation){
        return field.isAnnotationPresent(annotation);
    }
    
    public static <T extends Annotation> boolean isAnnotationPresent(Class<?> clazz ,Class<T> annotation){
        return clazz.isAnnotationPresent(annotation);
    }
    
    public static <A extends Annotation> Collection<A> getAllMergedAnnotations(AnnotatedElement element, Class<A> annotationType) {
        return AnnotatedElementUtils.getAllMergedAnnotations(element, annotationType);
    }
    
}
