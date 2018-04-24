package com.hisun.lemon.framework.data;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import org.springframework.core.convert.Property;
import org.springframework.core.convert.TypeDescriptor;

import com.hisun.lemon.common.exception.LemonException;
import com.hisun.lemon.common.utils.JudgeUtils;
import com.hisun.lemon.common.utils.ReflectionUtils;
import com.hisun.lemon.framework.utils.LemonUtils;

/**
 * 数据辅助类
 * 此类必须放在此包，对GenericData进行权限控制
 * @author yuzhou
 * @date 2017年7月4日
 * @time 上午11:36:18
 *
 */
public class DataHelper {
    private static Map<String, Method> genericDTOPropertySetMethods = null;
    private static final Map<String, TypeDescriptor> genericDTOPropertyTypeDescriptor = new HashMap<>();
    
    static {
        genericDTOPropertySetMethods = ReflectionUtils.getAllDeclareWriteMethods(GenericDTO.class);
        Stream.of(ReflectionUtils.getPropertyDescriptors(GenericDTO.class)).filter(p -> GenericDTOFields.isGenericDTOField(p.getName()))
            .forEach(p -> {genericDTOPropertyTypeDescriptor.put(p.getName(), toTypeDescriptor(p, GenericDTO.class));});
    }
    
    /**
     * lemondata --> genericDto
     * @param lemonData
     * @param genericDTO
     */
    public static void copyLemonDataToGenericDTO(LemonData lemonData, GenericDTO<?> genericDTO) {
        if(JudgeUtils.isNull(lemonData)) {
            return;
        }
        genericDTO.setRequestId(lemonData.getRequestId());
        genericDTO.setMsgId(lemonData.getMsgId());
        genericDTO.setAccDate(lemonData.getAccDate());
        genericDTO.setLocale(lemonData.getLocale());
        genericDTO.setStartDateTime(lemonData.getStartDateTime());
        genericDTO.setRouteInfo(lemonData.getRouteInfo());
        genericDTO.setUserId(lemonData.getUserId());
        genericDTO.setClientIp(lemonData.getClientIp());
        genericDTO.setLocale(lemonData.getLocale());
        genericDTO.setSource(lemonData.getSource());
        genericDTO.setChannel(lemonData.getChannel());
        genericDTO.setBusiness(lemonData.getBusiness());
        genericDTO.setUri(lemonData.getUri());
        genericDTO.setToken(lemonData.getToken());
        genericDTO.setLoginName(lemonData.getLoginName());
    }
    
    /**
     * 生成新的msgID
     * lemondata --> genericDto
     * @param lemonData
     * @param genericDTO
     * @param msgId IdGenUtils.generateMsgId()
     */
    public static void copyLemonDataToGenericDTOAndUpdateMsgId(LemonData lemonData, GenericDTO<?> genericDTO, String msgId) {
        if(JudgeUtils.isNull(lemonData)) {
            return;
        }
        genericDTO.setRequestId(lemonData.getRequestId());
        genericDTO.setMsgId(msgId);
        genericDTO.setAccDate(lemonData.getAccDate());
        genericDTO.setLocale(lemonData.getLocale());
        genericDTO.setStartDateTime(lemonData.getStartDateTime());
        genericDTO.setRouteInfo(lemonData.getRouteInfo());
        genericDTO.setUserId(lemonData.getUserId());
        genericDTO.setClientIp(lemonData.getClientIp());
        genericDTO.setLocale(lemonData.getLocale());
        genericDTO.setSource(lemonData.getSource());
        genericDTO.setChannel(lemonData.getChannel());
        genericDTO.setBusiness(lemonData.getBusiness());
        genericDTO.setUri(lemonData.getUri());
        genericDTO.setToken(lemonData.getToken());
        genericDTO.setLoginName(lemonData.getLoginName());
    }
    
    /**
     * genericDTO --> lemonData
     * @param genericDTO
     * @param lemonData
     */
    public static void copyGenericDTOToLemonData(GenericDTO<?> genericDTO, LemonData lemonData) {
        if(JudgeUtils.isNullAny(genericDTO, lemonData)) {
            return ;
        }
        lemonData.setRequestId(genericDTO.getRequestId());
        lemonData.setMsgId(genericDTO.getMsgId());
        lemonData.setAccDate(genericDTO.getAccDate());
        lemonData.setLocale(genericDTO.getLocale());
        lemonData.setStartDateTime(genericDTO.getStartDateTime());
        lemonData.setRouteInfo(genericDTO.getRouteInfo());
        lemonData.setUserId(genericDTO.getUserId());
        lemonData.setClientIp(genericDTO.getClientIp());
        lemonData.setLocale(genericDTO.getLocale());
        lemonData.setSource(genericDTO.getSource());
        lemonData.setChannel(genericDTO.getChannel());
        lemonData.setBusiness(genericDTO.getBusiness());
        lemonData.setUri(genericDTO.getUri());
        lemonData.setToken(genericDTO.getToken());
        lemonData.setLoginName(genericDTO.getLoginName());
    }
    
    /**
     * genericDTO --> genericDTO exclude body
     * @param srcDTO
     * @param destDTO
     * @return
     */
    public static boolean copyGenericDTO(GenericDTO<?> srcDTO, GenericDTO<?> destDTO) {
        if(JudgeUtils.isNullAny(srcDTO, destDTO)) {
            return false;
        }
        destDTO.setRequestId(srcDTO.getRequestId());
        destDTO.setMsgId(srcDTO.getMsgId());
        destDTO.setLocale(srcDTO.getLocale());
        destDTO.setStartDateTime(srcDTO.getStartDateTime());
        destDTO.setAccDate(srcDTO.getAccDate());
        destDTO.setUserId(srcDTO.getUserId());
        destDTO.setClientIp(srcDTO.getClientIp());
        destDTO.setLocale(srcDTO.getLocale());
        destDTO.setSource(srcDTO.getSource());
        destDTO.setChannel(srcDTO.getChannel());
        destDTO.setBusiness(srcDTO.getBusiness());
        destDTO.setUri(srcDTO.getUri());
        destDTO.setToken(srcDTO.getToken());
        destDTO.setLoginName(srcDTO.getLoginName());
        return true;
    }
    
    /**
     * copy response DTO exclude body
     * @param srcRspDTO
     * @param destRspDTO
     * @return
     */
    public static boolean copyGenericRspDTO(GenericRspDTO<?> srcRspDTO, GenericRspDTO<?> destRspDTO) {
        if(JudgeUtils.isNullAny(srcRspDTO, destRspDTO)) {
            return false;
        }
        destRspDTO.setMsgCd(srcRspDTO.getMsgCd());
        destRspDTO.setMsgInfo(srcRspDTO.getMsgInfo());
        return copyGenericDTO(srcRspDTO, destRspDTO);
    }
    
    /**
     * @param genericDTO
     * @return
     */
    public static LemonData setLemonDataAndAppendRouteInfo(GenericDTO<?> genericDTO) {
        LemonData lemonData = LemonHolder.getLemonData();
        DataHelper.copyGenericDTOToLemonData(genericDTO, lemonData);
        lemonData.setRouteInfo(lemonData.getRouteInfo()+"|"+LemonUtils.getApplicationName());
        return lemonData;
    }
    
    /**
     * set routeInfo
     * @param genericDTO
     * @param routeInfo
     */
    public static void setRouteInfo(GenericDTO<?> genericDTO, String routeInfo) {
        genericDTO.setRouteInfo(routeInfo);
    }
    
    /**
     * set request id
     * @param genericDTO
     * @param requestId
     */
    public static void setRequestId(GenericDTO<?> genericDTO, String requestId) {
        genericDTO.setRequestId(requestId);
    }
    
    /**
     * @param genericDTO
     * @param channel
     */
    public static void setChannel(GenericDTO<?> genericDTO, String channel) {
        genericDTO.setChannel(channel);
    }
    
    /**
     * @param genericDTO
     * @param propertyName
     * @param propertyValue
     */
    public static void setGenericDTOPropertyValue(GenericDTO<?> genericDTO, String propertyName, Object propertyValue) {
        try {
            genericDTOPropertySetMethods.get(propertyName).invoke(genericDTO, propertyValue);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw LemonException.create(e);
        }
    }
    
    /**
     * 清洗响应数据
     * clean response DTO
     * @param genericDTO
     * @return
     */
    public static <T> GenericRspDTO<T> cleanRspGenericDTO(GenericRspDTO<T> genericDTO) {
        genericDTO.setAccDate(null);
        //genericDTO.setRouteInfo(null);
        genericDTO.setUserId(null);
        genericDTO.setChannel(null);
        genericDTO.setSource(null);
        genericDTO.setToken(null);
        genericDTO.setLoginName(null);
        return genericDTO;
    }
    
    /**
     * clean request DTO
     * @param genericDTO
     * @return
     */
    public static <T> GenericDTO<T> cleanReqGenericDTO(GenericDTO<T> genericDTO) {
        genericDTO.setAccDate(null);
        genericDTO.setRouteInfo(null);
        genericDTO.setUserId(null);
        genericDTO.setClientIp(null);
        genericDTO.setSource(null);
        genericDTO.setToken(null);
        genericDTO.setStartDateTime(null);
        genericDTO.setLoginName(null);
        return genericDTO;
    }

    private static TypeDescriptor toTypeDescriptor(PropertyDescriptor pd, Class<?> beanClass) {
        return new TypeDescriptor(new Property(beanClass, pd.getReadMethod(), ReflectionUtils.getWriteMethod(pd, beanClass), pd.getName()));
    }
    
    public static TypeDescriptor getPropertyTypeDescriptor(String propertyName) {
       return genericDTOPropertyTypeDescriptor.get(propertyName);
    }
}
