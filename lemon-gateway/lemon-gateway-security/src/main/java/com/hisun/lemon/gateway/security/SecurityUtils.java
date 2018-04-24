package com.hisun.lemon.gateway.security;

import java.util.Optional;

import org.springframework.security.core.context.SecurityContextHolder;

import com.hisun.lemon.common.utils.JudgeUtils;
import com.hisun.lemon.gateway.common.GatewayHelper;

/**
 * @author yuzhou
 * @date 2017年8月11日
 * @time 下午12:03:15
 *
 */
public class SecurityUtils {
    
    public static final String REQUEST_ATTRIBUTE_KEY_LOGIN_USER_INFO = "REQUEST_ATTRIBUTE_KEY_LOGIN_USER_INFO";
    /**
     * 获取登录用户信息
     * @return
     */
    @SuppressWarnings("unchecked")
    public static UserInfo getLoginUser() {
        Object principal = Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication()).map(a -> a.getPrincipal()).orElse(null);
        return Optional.ofNullable(principal).filter(p -> p instanceof LemonUser).map(pr -> (LemonUser<UserInfo>)pr).map(lu -> lu.getUserInfo())
            .orElseGet(() -> SecurityUtils.getLoginUserAfterLoginRequest());
    }
    
    /**
     * 仅仅在登录请求交易中能取得到登录用户信息
     * @return
     */
    public static UserInfo getLoginUserAfterLoginRequest() {
        return Optional.ofNullable(GatewayHelper.getHttpServletRequest()).map(r -> r.getAttribute(REQUEST_ATTRIBUTE_KEY_LOGIN_USER_INFO))
            .map(UserInfo.class::cast).orElse(null);
    }
    
    /**
     * 用户登录成功完成验证后设置
     * @param userInfo
     */
    public static void setLoginUserAfterLoginRequest(UserInfo userInfo) {
        Optional.ofNullable(GatewayHelper.getHttpServletRequest()).map(r -> {r.setAttribute(REQUEST_ATTRIBUTE_KEY_LOGIN_USER_INFO, userInfo); return true;}).orElse(false);
    }
    
    /**
     * 登录用户ID
     * @return
     */
    public static String getLoginUserId() {
        return Optional.ofNullable(getLoginUser()).map(u -> u.getUserId()).orElse(null);
    }
    
    /**
     * 获取登录名
     * @return
     */
    public static String getLoginName() {
        return Optional.ofNullable(getLoginUser()).map(u -> u.getLoginName()).orElse(null);
    }
    
    /**
     * 根据Principal获取登录渠道类型
     * USR用户
     * MER商户
     * @param principal
     * @return
     */
    @SuppressWarnings("unchecked")
    public static Channel getLoginChannelByPrincipal(Object principal) {
        if(JudgeUtils.isNull(principal)) return null;
        if(principal instanceof LemonUser) {
            String channel = ((LemonUser<UserInfo>)principal).getLoginChannel();
            return Optional.ofNullable(channel).map(Channel::parse).orElse(null);
        }
        return null;
    }
    
}
