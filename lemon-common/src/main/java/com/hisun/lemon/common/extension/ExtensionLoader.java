package com.hisun.lemon.common.extension;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.stream.Collectors;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.hisun.lemon.common.KVPair;
import com.hisun.lemon.common.LemonConstants;
import com.hisun.lemon.common.exception.ErrorMsgCode;
import com.hisun.lemon.common.exception.LemonException;
import com.hisun.lemon.common.utils.AnnotationUtils;
import com.hisun.lemon.common.utils.ReflectionUtils;
import com.hisun.lemon.common.utils.StringUtils;

/**
 * 扩展加载
 * @author yuzhou
 * @date 2017年6月14日
 * @time 上午10:45:48
 *
 */
public class ExtensionLoader implements ApplicationContextAware {
    
    private static final String PREFIX = "META-INF/services/";
    
    private static ApplicationContext applicationContext;
    
    /**
     * 获取激活的bean
     * 激活方式：类用Activate注解
     * @param clazz
     * @return
     */
    public static <T> T getActivateSpringBean(Class<T> clazz){
        if(! AnnotationUtils.isAnnotationPresent(clazz, Activate.class)) {
            throw new IllegalStateException("The class is not annotated by \"@Activate\". class = "+clazz);
        }
        String activateBeanName = AnnotationUtils.findAnnotation(clazz, Activate.class).value();
        return applicationContext.getBean(activateBeanName, clazz);
    }
    
    public static <T> T getSpringBean(String beanName, Class<T> clazz) {
        Object obj = applicationContext.getBean(beanName);
        if(null == obj) {
            return null;
        }
        return clazz.cast(obj);
    }
    
    public static <T> T getSpringBean(Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }

    public static <T> Map<String, T> getSpringBeansOfType(Class<T> clazz) {
        return applicationContext.getBeansOfType(clazz);
    }
    
    public static <T> Map<String, T> getSpringBeansOfType(ApplicationContext applicationContext, Class<T> clazz) {
        return applicationContext.getBeansOfType(clazz);
    }
    
    @Override
    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {
        ExtensionLoader.applicationContext = applicationContext;
    }

    /**
     * JAVA SPI机制
     * 获取接口／抽象类的扩展实例，扩展文件在META-INF/services/ 下；文件格式：className
     * 
     * @param service
     * @return
     */
    public static <T> List<T> getExtensionServices(Class<T> service) {
        List<T> list = new ArrayList<>();
        ServiceLoader<T> loader = ServiceLoader.load(service);
        Iterator<T> iterator = loader.iterator();
        while (true) {
            if (iterator.hasNext()) {
                list.add(iterator.next());
            } else {
                break;
            }
        }
        return list;
    }
    
    /**
     * 获取适配服务
     * @param service
     * @return
     */
    public static <T> T getExtensionAdaptiveService(Class<T> service) {
        T adaptive = null;
        ServiceLoader<T> loader = ServiceLoader.load(service);
        Iterator<T> iterator = loader.iterator();
        while (true) {
            if (iterator.hasNext()) {
                T s = iterator.next();
                if(AnnotationUtils.isAnnotationPresent(s.getClass(), Adaptive.class)) {
                    adaptive = s;
                    break;
                }
            } else {
                break;
            }
        }
        return adaptive;
    }
    
    /**
     * 获取适配服务
     * @param service
     * @return
     */
    public static <T> T getExtensionActivateService(Class<T> service) {
        List<T> activateServices = new ArrayList<>();
        ServiceLoader<T> loader = ServiceLoader.load(service);
        Iterator<T> iterator = loader.iterator();
        while (true) {
            if (iterator.hasNext()) {
                T s = iterator.next();
                if(AnnotationUtils.isAnnotationPresent(s.getClass(), Activate.class)) {
                    activateServices.add(s);
                }
            } else {
                break;
            }
        }
        if(activateServices.size() > 1) {
            LemonException.throwLemonException(ErrorMsgCode.SYS_ERROR.getMsgCd(), "Too many activate service exist for SPI "+ service);
        }
        return activateServices.get(0);
    }
    
    /**
     * 
     * 获取接口／抽象类的扩展类，扩展文件在META-INF/services/ 下；文件格式：key=calssName
     * 
     * @param service
     * @return
     */
    public static <T> Map<String, Class<?>> getExtensionClasses(Class<T> service) {
        
        ClassLoader cl = ExtensionLoader.class.getClassLoader();
        //        ClassLoader.getSystemClassLoader();
        Enumeration<URL> urls = null;
        try {
            urls =  cl.getResources(PREFIX + service.getName());
        } catch (IOException e1) {
            throw new IllegalStateException("Reading file occur error by path "+PREFIX+service.getName(), e1);
        }
        if(null == urls || !urls.hasMoreElements()) {
            return null;
        }
        List<String> list = new ArrayList<>();
        while(true) {
            if(urls.hasMoreElements()) {
                URL url = urls.nextElement();
                InputStream inputStream = null;
                BufferedReader br = null;
                try {
                    inputStream = url.openStream();
                    br = new BufferedReader(new InputStreamReader(inputStream));
                    while(true) {
                        String line = br.readLine();
                        if(StringUtils.isBlank(line)){
                            break;
                        }
                        list.add(line);
                    }
                } catch (IOException e) {
                    throw new IllegalStateException(e);
                }finally {
                    try {
                        if(null != br) br.close();
                        if(null != inputStream) inputStream.close();
                    } catch (IOException e) {
                    }
                }
            } else {
                break;
            }
        }
        return list.stream().filter(s -> !StringUtils.startsWith(s, LemonConstants.PROPERTY_IGNORE_START_CHAR))
            .map(s -> s.split(LemonConstants.PROPERTY_KEY_VALUE_SEPARATOR)).map(a -> KVPair.instance(StringUtils.trim(a[0]), ReflectionUtils.forName(StringUtils.trim(a[1])) ))
            .collect(Collectors.toMap(KVPair::getK, KVPair::getV));
    }
}
