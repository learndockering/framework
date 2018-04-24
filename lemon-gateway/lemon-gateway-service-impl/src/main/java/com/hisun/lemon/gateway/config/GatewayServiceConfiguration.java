package com.hisun.lemon.gateway.config;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.hisun.lemon.gateway.bo.SecurityBO;
import com.hisun.lemon.gateway.common.GatewayHelper;
import com.hisun.lemon.gateway.service.IAppInfoService;
import com.hisun.lemon.gateway.service.IAuthenticationService;
import com.hisun.lemon.gateway.service.ISecurityService;
import com.hisun.lemon.gateway.service.IUserService;
import com.hisun.lemon.gateway.service.impl.AppInfoServiceImpl;
import com.hisun.lemon.gateway.service.impl.AuthenticationServiceImpl;
import com.hisun.lemon.gateway.service.impl.SecurityServiceImpl;

/**
 * @author yuzhou
 * @date 2017年8月31日
 * @time 下午1:34:39
 *
 */
@Configuration
public class GatewayServiceConfiguration {
    public static final String IGNORE_SET_CHANNEL = GatewayHelper.IGNORE_SET_CHANNEL;
    
    @Bean
    public IAppInfoService appInfoServiceImpl() {
        return new AppInfoServiceImpl();
    }
    
    @Bean
    public IAuthenticationService authenticationServiceImpl() {
        return new AuthenticationServiceImpl();
    }
    
    @Bean
    public IUserService userServiceImpl() {
        return null;
    }
    
    @Bean
    public ISecurityService securityServiceImpl() {
        SecurityServiceImpl securityServiceImpl = new SecurityServiceImpl();
        return securityServiceImpl;
    }
    
    @Bean
    @Primary
    @ConditionalOnClass(HttpServletRequest.class)
    public ISecurityService decoratorSecurityService(ISecurityService securityServiceImpl) {
        return new ISecurityService() {
            @Override
            public SecurityBO querySecurity(String secureIndex) {
                HttpServletRequest request = GatewayHelper.getHttpServletRequest();
                try{
                    Optional.ofNullable(request).map(r -> { r.setAttribute(IGNORE_SET_CHANNEL, IGNORE_SET_CHANNEL); return IGNORE_SET_CHANNEL; }).orElse(null);
                    return securityServiceImpl.querySecurity(secureIndex);
                } finally {
                    Optional.ofNullable(request).map(r -> {r.removeAttribute(IGNORE_SET_CHANNEL); return IGNORE_SET_CHANNEL;}).orElse(null);
                }
            }

            public SecurityBO queryMerchantSecurity(String merchantNo, String interfaceName, String interfaceVersion) {
                return null;
            }
        };
    }
}
