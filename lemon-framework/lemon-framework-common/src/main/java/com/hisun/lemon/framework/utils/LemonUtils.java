package com.hisun.lemon.framework.utils;

import java.time.LocalDate;
import java.util.Locale;
import java.util.Optional;

import com.hisun.lemon.common.Env;
import com.hisun.lemon.common.LemonConstants;
import com.hisun.lemon.common.LemonFramework;
import com.hisun.lemon.common.extension.ExtensionLoader;
import com.hisun.lemon.common.utils.JudgeUtils;
import com.hisun.lemon.framework.config.LemonEnvironment;
import com.hisun.lemon.framework.data.DataHelper;
import com.hisun.lemon.framework.data.GenericDTO;
import com.hisun.lemon.framework.data.LemonHolder;

/**
 * Lemon 框架服务工具类
 * 
 * @author yuzhou
 * @date 2017年6月14日
 * @time 上午10:21:06
 *
 */
public class LemonUtils {
    private static final String LEMON_HOME = LemonConstants.LEMON_HOME;
    private static final String CURRENT_ENV = LemonConstants.CURRENT_ENV;
    private static final String BATCH_ENV = LemonFramework.BATCH_ENV;
    private static final String LEMON_ENVIRONMENT_BEAN_NAME = "lemonEnvironment";
    
    private static LemonEnvironment lemonEnvironment = null;
    
    /**
     * 应用名
     * @return
     */
    public static String getApplicationName() {
        return getLemonEnvironment().getApplicationName();
    }
    
    /**
     * 获取当前环境配置
     * @return
     */
    public static LemonEnvironment getLemonEnvironment() {
        if (null == lemonEnvironment) {
            lemonEnvironment = ExtensionLoader.getSpringBean(LEMON_ENVIRONMENT_BEAN_NAME, LemonEnvironment.class);
        }
        return lemonEnvironment;
    }
    
    /**
     * 获取当前环境的属性值
     * @param key
     * @return
     */
    public static String getProperty(String key) {
        return getLemonEnvironment().getProperty(key);
    }
    
    /**
     * 获取当前环境的属性值
     * @param key
     * @param targetType
     * @return
     */
    public static <T> T getProperty(String key, Class<T> targetType) {
        return getLemonEnvironment().getProperty(key, targetType);
    }
    
    /**
     * 获取Home目录
     * @return
     */
    public static String getLemonHome() {
        return getProperty(LEMON_HOME);
    }
    
    /**
     * 获取当前环境
     * @return
     */
    public static Env getEnv() {
        return Env.valueOf(getProperty(CURRENT_ENV).toUpperCase());
    }
    
    /**
     * 批量环境
     * @return
     */
    public static boolean isBatchEnv() {
        return Boolean.valueOf(getProperty(BATCH_ENV));
    }
    
    public static String[] getGateways() {
        return getLemonEnvironment().getLemonConfig().getGateways();
    }
    
    /**
     * 框架自带属性的copy
     * @param srcDTO
     * @param destDTO
     */
    public static boolean copyGenericDTO(GenericDTO<?> srcDTO, GenericDTO<?> destDTO) {
        return DataHelper.copyGenericDTO(srcDTO, destDTO);
    }
    
    /**
     * 获取流水号
     * @return
     */
    public static String getRequestId() {
        return Optional.ofNullable(LemonHolder.getLemonData()).map(d -> d.getRequestId()).orElse(null);
    }
    
    /**
     * 获取消息Id
     * @return
     */
    public static String getMsgId() {
        return Optional.ofNullable(LemonHolder.getLemonData()).map(d -> d.getMsgId()).orElse(null);
    }
    
    /**
     * 获取记账日期
     * @return
     */
    public static LocalDate getAccDate() {
        return Optional.ofNullable(LemonHolder.getLemonData()).map(d -> d.getAccDate()).orElse(null);
    }
    
    /**
     * 获取区域信息
     * @return
     */
    public static Locale getLocale() {
        Locale locale = Optional.ofNullable(LemonHolder.getLemonData()).map(m -> m.getLocale()).orElseGet(
            () -> LemonUtils.getLemonEnvironment().getDefaultLocale());
        if(JudgeUtils.isNotNull(locale)) {
            return locale;
        }
        return LemonUtils.getLemonEnvironment().getDefaultLocale();
    }
    
    /**
     * 获取登录用户ID
     * @return
     */
    public static String getUserId() {
        return Optional.ofNullable(LemonHolder.getLemonData()).map(d -> d.getUserId()).orElse(null);
    }
    
    /**
     * 客户端IP
     * @return
     */
    public static String getClientIp() {
        return Optional.ofNullable(LemonHolder.getLemonData()).map(d -> d.getClientIp()).orElse(null);
    }
    
    /**
     * 获取请求源
     * @return
     */
    public static String getSource() {
        return Optional.ofNullable(LemonHolder.getLemonData()).map(d -> d.getSource()).orElse(null);
    }
    
    /**
     * 获取渠道
     * @return
     */
    public static String getChannel() {
        return Optional.ofNullable(LemonHolder.getLemonData()).map(d -> d.getChannel()).orElse(null);
    }
    
    /**
     * 客户端请求URI
     * @return
     */
    public static String getURI() {
        return Optional.ofNullable(LemonHolder.getLemonData()).map(d -> d.getUri()).orElse(null);
    }
    
    /**
     * 网关配置的业务
     * @return
     */
    public static String getBusiness() {
        return Optional.ofNullable(LemonHolder.getLemonData()).map(d -> d.getBusiness()).orElse(null);
    }
    
    /**
     * 获取登录用户绑定token
     * @return
     */
    public static String getToken() {
        return Optional.ofNullable(LemonHolder.getLemonData()).map(d -> d.getToken()).orElse(null);
    }
    
    /**
     * 登录名
     * @return
     */
    public static String getLoginName() {
        return Optional.ofNullable(LemonHolder.getLemonData()).map(d -> d.getLoginName()).orElse(null);
    }
    
    /**
     * 判断是否已登录
     * @return
     */
    public static boolean isUserLogin() {
        if(null == LemonHolder.getLemonData()) {
            return false;
        }
        if(JudgeUtils.isNotBlank(LemonHolder.getLemonData().getUserId())) {
            return true;
        }
        return false;
    }
    
    /**
     * 获取成功消息码
     * @return
     */
    public static String getSuccessMsgCd() {
        return LemonUtils.getApplicationName() + JudgeUtils.successfulMsgCode;
    }
    
}
