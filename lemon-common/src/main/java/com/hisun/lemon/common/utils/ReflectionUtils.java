package com.hisun.lemon.common.utils;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.UndeclaredThrowableException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.util.ClassUtils;

/**
 * 反射辅助类
 * @author yuzhou
 * @date 2017年6月14日
 * @time 上午10:46:40
 *
 */
public class ReflectionUtils {
    protected static final String[] PRIMITIVE_NAMES = new String[] { "boolean",
        "byte", "char", "double", "float", "int", "long", "short", "void" };

    protected static final Class<?>[] PRIMITIVES = new Class[] { boolean.class,
        byte.class, char.class, double.class, float.class, int.class,
        long.class, short.class, Void.TYPE };
    
    private static final Map<Class<?>,Field[]> declaredFieldsCache = new ConcurrentHashMap<>(256);
    
    /**
     * 根据类获取所有的Fields
     * @param clazz
     * @return
     */
    public static <T> Field[] getDeclaredFields(Class<T> clazz){
        if (null == clazz) {
            return new Field[]{};
        }
        Field[] fields = declaredFieldsCache.get(clazz);
        if (null == fields) {
            fields = clazz.getDeclaredFields();
            declaredFieldsCache.put(clazz, fields);
        }
        return Optional.ofNullable(fields).orElseGet(() -> new Field[]{});
    }
    
    public static Object getField(Field field ,Object object){
        makeAccessible(field);
        try {
            return field.get(object);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            handleReflectionException(e);
        }
        throw new IllegalStateException("Should never get here");
    }
    
    public static void setField(Field field ,Object object , Object value){
        makeAccessible(field);
        try {
            field.set(object, value);
        } catch (IllegalArgumentException e){
            try {
                field.set(object, convertType(field.getType(), value));
            } catch (IllegalArgumentException | IllegalAccessException e1) {
                handleReflectionException(e);
            }
        } catch (IllegalAccessException e) {
            handleReflectionException(e);
        }
    }
    
    public static Object convertType(Class<?> returnType , Object value){
        if (returnType == null || value == null) {
            return null;
        } else if (returnType == String.class) {
            return String.valueOf(value);
        } else if (returnType == boolean.class || returnType == Boolean.class) {
            return Boolean.valueOf(String.valueOf(value));
        } else if (returnType == char.class || returnType == Character.class) {
            return Character.valueOf((char)value);
        } else if (returnType == byte.class || returnType == Byte.class) {
            return Byte.valueOf(String.valueOf(value));
        } else if (returnType == short.class || returnType == Short.class) {
            return Short.valueOf(String.valueOf(value));
        } else if (returnType == int.class || returnType == Integer.class) {
            return Integer.valueOf(String.valueOf(value));
        } else if (returnType == long.class || returnType == Long.class) {
            return Long.valueOf(String.valueOf(value));
        } else if (returnType == float.class || returnType == Float.class) {
            return Float.valueOf(String.valueOf(value));
        } else if (returnType == double.class || returnType == Double.class) {
            return Double.valueOf(String.valueOf(value));
        } else if (returnType == BigDecimal.class) {
            return new BigDecimal(value.toString());
        } else if (returnType == LocalDate.class) {
            return DateTimeUtils.parseLocalDate(value.toString());
        } else if (returnType == LocalDateTime.class) {
            return DateTimeUtils.parseLocalDateTime(value.toString());
        }
        return value;
    }
    
    /**
     * Invoke the specified {@link Method} against the supplied target object with no arguments.
     * The target object can be {@code null} when invoking a static {@link Method}.
     * <p>Thrown exceptions are handled via a call to {@link #handleReflectionException}.
     * @param method the method to invoke
     * @param target the target object to invoke the method on
     * @return the invocation result, if any
     * @see #invokeMethod(java.lang.reflect.Method, Object, Object[])
     */
    public static Object invokeMethod(Method method, Object target) {
        return invokeMethod(method, target, new Object[0]);
    }

    /**
     * Invoke the specified {@link Method} against the supplied target object with the
     * supplied arguments. The target object can be {@code null} when invoking a
     * static {@link Method}.
     * <p>Thrown exceptions are handled via a call to {@link #handleReflectionException}.
     * @param method the method to invoke
     * @param target the target object to invoke the method on
     * @param args the invocation arguments (may be {@code null})
     * @return the invocation result, if any
     */
    public static Object invokeMethod(Method method, Object target, Object... args) {
        try {
            return method.invoke(target, args);
        }
        catch (Exception ex) {
            handleReflectionException(ex);
        }
        throw new IllegalStateException("Should never get here");
    }
    
    /**
     * 获取类的属性的读方法（属性名，方法）
     * @param clazz
     * @return
     */
    public static <T> Map<String,Method> getDeclareReadMethods(Class<T> clazz){
        Map<String,Method> readMethods = new HashMap<>();
        Arrays.asList(getPropertyDescriptors(clazz)).forEach(pd -> {
            Method readMethod = pd.getReadMethod();
            makeAccessible(readMethod);
            readMethods.put(pd.getName(), readMethod);
            });
        return readMethods;
    }
    
    /**
     * 获取类的属性的读方法（属性名，方法）
     * @param clazz
     * @return
     */
    public static <T> Map<String,Method> getDeclareWriteMethods(Class<T> clazz){
        Map<String,Method> writeMethods = new HashMap<>();
        Arrays.asList(getPropertyDescriptors(clazz)).stream().forEach(pd -> {
            Method writeMethod = pd.getWriteMethod();
            makeAccessible(writeMethod);
            writeMethods.put(pd.getName(), writeMethod);
        });
        return writeMethods;
    }
    
    /**
     * 获取类的属性的写方法（属性名，方法），包含非public的方法
     * 
     * @param clazz
     * @return
     */
    public static <T> Map<String,Method> getAllDeclareWriteMethods(Class<T> clazz){
        Map<String,Method> writeMethods = new HashMap<>();
        Arrays.asList(getPropertyDescriptors(clazz)).stream().forEach(pd -> {
            Method writeMethod = pd.getWriteMethod();
            if(null == writeMethod) {
                writeMethod = getDeclaredMethodByName(clazz, "set"+ StringUtils.capitalize(pd.getName()));
            }
            if (null != writeMethod) {
                makeAccessible(writeMethod);
                writeMethods.put(pd.getName(), writeMethod);
            } else {
                System.out.println(pd.getName());
            }
        });
        return writeMethods;
    }
    
    public static Method getWriteMethod(PropertyDescriptor pd, Class<?> clazz) {
        Method writeMethod = pd.getWriteMethod();
        if(null == writeMethod) {
            writeMethod = getDeclaredMethodByName(clazz, "set"+ StringUtils.capitalize(pd.getName()));
        }
        return writeMethod;
    }

    public static PropertyDescriptor[] getPropertyDescriptors(Class<?> clazz) {
        try {
            return Introspector.getBeanInfo(clazz).getPropertyDescriptors();
        } catch (IntrospectionException e) {
            throw new IllegalStateException("Could not get bean info for class "+clazz, e);
        }
    }
    
    public static Method getDeclaredMethodByName(final Class<?> clazz, final String methodName) {
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                return method;
            }
        }
        return null;
    }
    
    /**
     * 循环向上转型, 获取对象的DeclaredMethod,并强制设置为可访问.
     * 如向上转型到Object仍无法找到, 返回null.
     * 只匹配函数名。
     * 
     * 用于方法需要被多次调用的情况. 先使用本函数先取得Method,然后调用Method.invoke(Object obj, Object... args)
     */
    public static Method getAccessibleMethodByName(final Object obj, final String methodName) {
        for (Class<?> searchType = obj.getClass(); searchType != Object.class; searchType = searchType.getSuperclass()) {
            Method[] methods = searchType.getDeclaredMethods();
            for (Method method : methods) {
                if (method.getName().equals(methodName)) {
                    makeAccessible(method);
                    return method;
                }
            }
        }
        return null;
    }
    
    /**
     * 循环向上转型, 获取对象的DeclaredMethod,并强制设置为可访问.
     * 如向上转型到Object仍无法找到, 返回null.
     * 只匹配函数名。
     * 
     * 用于方法需要被多次调用的情况. 先使用本函数先取得Method,然后调用Method.invoke(Object obj, Object... args)
     */
    public static Method getAccessibleMethodByName(final Class<?> clazz, final String methodName) {
        for (Class<?> searchType = clazz; searchType != Object.class; searchType = searchType.getSuperclass()) {
            Method[] methods = searchType.getDeclaredMethods();
            for (Method method : methods) {
                if (method.getName().equals(methodName)) {
                    makeAccessible(method);
                    return method;
                }
            }
        }
        return null;
    }
    
    /**
     * 根据field name 查找set method
     * @param clazz
     * @param field
     * @return
     */
    public static Method getAccessibleWriteMethodByField(final Class<?> clazz, final Field field) {
        String fieldNm = field.getName();
        String methodName = "set"+fieldNm.substring(0, 1).toUpperCase()+fieldNm.substring(1);
        return getAccessibleMethodByName(clazz, methodName);
    }
    
    /**
     *  获取类的泛型
     * @param cls
     * @return
     */
    public static Class<?> getGenericClass(Class<?> cls) {
        return getGenericClass(cls, 0);
    }

    /**
     * 获取类的泛型
     * @param cls
     * @param i
     * @return
     */
    public static Class<?> getGenericClass(Class<?> cls, int i) {
        try {
            ParameterizedType parameterizedType = ((ParameterizedType) cls.getGenericInterfaces()[0]);
            Object genericClass = parameterizedType.getActualTypeArguments()[i];
            if (genericClass instanceof ParameterizedType) { // 处理多级泛型
                return (Class<?>) ((ParameterizedType) genericClass).getRawType();
            } else if (genericClass instanceof GenericArrayType) { // 处理数组泛型
                return (Class<?>) ((GenericArrayType) genericClass).getGenericComponentType();
            } else if (genericClass != null) {
                return (Class<?>) genericClass;
            }
        } catch (Throwable e) {
        }
        if (cls.getSuperclass() != null) {
            return getGenericClass(cls.getSuperclass(), i);
        } else {
            throw new IllegalArgumentException(cls.getName() + " generic type undefined!");
        }
    }
    
    /**
     * 获取feild的类的泛型
     * @param field
     * @return
     */
    public static Class<?> getGenericClass(Field field) {
        return getGenericClass(field,0);
    }
    
    public static Class<?> getGenericClass(Field field, int i) {
        ParameterizedType parameterizedType = (ParameterizedType)field.getGenericType();
        Object genericClass = (Class<?>) parameterizedType.getActualTypeArguments()[i];
        if (genericClass != null) {
            return (Class<?>) genericClass;
        }
        throw new IllegalArgumentException(field + " generic type undefined!");
    }
    
    public static <T> T newInstance(Class<T> clazz){
        try {
            return clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new IllegalStateException("Can't new instance for "+clazz+" :" + e.getMessage());
        }
    }
    
    public static boolean isList(Field field){
        if(List.class.isAssignableFrom(field.getType())){
            return true;
        }
        return false;
    }
    
    /**
     * Make the given field accessible, explicitly setting it accessible if
     * necessary. The {@code setAccessible(true)} method is only called
     * when actually necessary, to avoid unnecessary conflicts with a JVM
     * SecurityManager (if active).
     * @param field the field to make accessible
     * @see java.lang.reflect.Field#setAccessible
     */
    public static void makeAccessible(Field field) {
        if ((!Modifier.isPublic(field.getModifiers()) ||
                !Modifier.isPublic(field.getDeclaringClass().getModifiers()) ||
                Modifier.isFinal(field.getModifiers())) && !field.isAccessible()) {
            field.setAccessible(true);
        }
    }

    /**
     * Make the given method accessible, explicitly setting it accessible if
     * necessary. The {@code setAccessible(true)} method is only called
     * when actually necessary, to avoid unnecessary conflicts with a JVM
     * SecurityManager (if active).
     * @param method the method to make accessible
     * @see java.lang.reflect.Method#setAccessible
     */
    public static void makeAccessible(Method method) {
        if ((!Modifier.isPublic(method.getModifiers()) ||
                !Modifier.isPublic(method.getDeclaringClass().getModifiers())) && !method.isAccessible()) {
            method.setAccessible(true);
        }
    }
    
    /**
     * Make the given constructor accessible, explicitly setting it accessible
     * if necessary. The {@code setAccessible(true)} method is only called
     * when actually necessary, to avoid unnecessary conflicts with a JVM
     * SecurityManager (if active).
     * @param ctor the constructor to make accessible
     * @see java.lang.reflect.Constructor#setAccessible
     */
    public static void makeAccessible(Constructor<?> ctor) {
        if ((!Modifier.isPublic(ctor.getModifiers()) ||
                !Modifier.isPublic(ctor.getDeclaringClass().getModifiers())) && !ctor.isAccessible()) {
            ctor.setAccessible(true);
        }
    }
    
    /**
     * 是否有默认构造函数
     * @param clazz
     * @return
     */
    public static boolean hasDefaultConstructor(Class<?> clazz) { 
        try {
            clazz.getConstructor(new Class<?>[]{});
            return true;
        } catch (NoSuchMethodException e) {
            return false;
        } catch (SecurityException e) {
            throw e;
        }
    }
    
    public static boolean isPublic(Class<?> clazz) {
        return Modifier.isPublic(clazz.getModifiers());
    }
    
    /**
     * 是否简单类型
     * @param cls
     * @return
     */
    public static boolean isPrimitive(Class<?> cls) {
        return cls.isPrimitive() || cls == String.class || cls == Boolean.class || cls == Character.class 
                || Number.class.isAssignableFrom(cls) || Date.class.isAssignableFrom(cls);
    }
    
    public static Class<?> forName(String[] packages, String className){
        Class<?> clazz = forNamePrimitive(className);
        if(clazz != null) {
            return clazz;
        }
        if (packages != null && packages.length > 0) {
            for (String pkg : packages) {
                String className0 = pkg +"."+className;
                try{
                    return forName(className0);
                } catch (IllegalStateException e) {
                }
            }
        }
        throw new IllegalStateException("Class not found. packages="+(packages == null ? "" : Stream.of(packages).collect(Collectors.joining(" ")))
                +"  ,className = "+className);
    }

    public static Class<?> forName(String name){
        if (null == name || "".equals(name)) {
            return null;
        }
        Class<?> c = forNamePrimitive(name);
        if (c == null) {
            if (name.endsWith("[]")) {
                String nc = name.substring(0, name.length() - 2);
                try {
                    c = Class.forName(nc, true, Thread.currentThread().getContextClassLoader());
                } catch (ClassNotFoundException e) {
                    throw new IllegalStateException(e);
                }
                c = Array.newInstance(c, 0).getClass();
            } else {
                try {
                    c = Class.forName(name, true, Thread.currentThread().getContextClassLoader());
                } catch (ClassNotFoundException e) {
                    throw new IllegalStateException(e);
                }
            }
        }
        return c;
    }

    protected static Class<?> forNamePrimitive(String name) {
        if (name.length() <= 8) {
            int p = Arrays.binarySearch(PRIMITIVE_NAMES, name);
            if (p >= 0) {
                return PRIMITIVES[p];
            }
        }
        return null;
    }
    
    /**
     * Handle the given reflection exception. Should only be called if no
     * checked exception is expected to be thrown by the target method.
     * <p>Throws the underlying RuntimeException or Error in case of an
     * InvocationTargetException with such a root cause. Throws an
     * IllegalStateException with an appropriate message or
     * UndeclaredThrowableException otherwise.
     * @param ex the reflection exception to handle
     */
    public static void handleReflectionException(Exception ex) {
        if (ex instanceof NoSuchMethodException) {
            throw new IllegalStateException("Method not found: " + ex.getMessage());
        }
        if (ex instanceof IllegalAccessException) {
            throw new IllegalStateException("Could not access method: " + ex.getMessage());
        }
        if (ex instanceof InvocationTargetException) {
            handleInvocationTargetException((InvocationTargetException) ex);
        }
        if (ex instanceof RuntimeException) {
            throw (RuntimeException) ex;
        }
        throw new UndeclaredThrowableException(ex);
    }

    /**
     * Handle the given invocation target exception. Should only be called if no
     * checked exception is expected to be thrown by the target method.
     * <p>Throws the underlying RuntimeException or Error in case of such a root
     * cause. Throws an UndeclaredThrowableException otherwise.
     * @param ex the invocation target exception to handle
     */
    public static void handleInvocationTargetException(InvocationTargetException ex) {
        rethrowRuntimeException(ex.getTargetException());
    }

    /**
     * Rethrow the given {@link Throwable exception}, which is presumably the
     * <em>target exception</em> of an {@link InvocationTargetException}.
     * Should only be called if no checked exception is expected to be thrown
     * by the target method.
     * <p>Rethrows the underlying exception cast to a {@link RuntimeException} or
     * {@link Error} if appropriate; otherwise, throws an
     * {@link UndeclaredThrowableException}.
     * @param ex the exception to rethrow
     * @throws RuntimeException the rethrown exception
     */
    public static void rethrowRuntimeException(Throwable ex) {
        if (ex instanceof RuntimeException) {
            throw (RuntimeException) ex;
        }
        if (ex instanceof Error) {
            throw (Error) ex;
        }
        throw new UndeclaredThrowableException(ex);
    }

    /**
     * Rethrow the given {@link Throwable exception}, which is presumably the
     * <em>target exception</em> of an {@link InvocationTargetException}.
     * Should only be called if no checked exception is expected to be thrown
     * by the target method.
     * <p>Rethrows the underlying exception cast to an {@link Exception} or
     * {@link Error} if appropriate; otherwise, throws an
     * {@link UndeclaredThrowableException}.
     * @param ex the exception to rethrow
     * @throws Exception the rethrown exception (in case of a checked exception)
     */
    public static void rethrowException(Throwable ex) throws Exception {
        if (ex instanceof Exception) {
            throw (Exception) ex;
        }
        if (ex instanceof Error) {
            throw (Error) ex;
        }
        throw new UndeclaredThrowableException(ex);
    }
    
    public static boolean sameClass(Class<?> c1, Class<?> c2) {
        if(c1 == c2) {
            return true;
        }
        if(c1.isAssignableFrom(c2) && c2.isAssignableFrom(c1)) {
            return true;
        }
        return false;
    }
    
    public static boolean isPresent(String className) {
        return ClassUtils.isPresent(className, ReflectionUtils.class.getClassLoader());
    }

}
