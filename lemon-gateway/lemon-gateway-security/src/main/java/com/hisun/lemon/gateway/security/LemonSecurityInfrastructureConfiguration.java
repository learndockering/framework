package com.hisun.lemon.gateway.security;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.SessionRepository;
import org.springframework.session.data.redis.RedisOperationsSessionRepository;
import org.springframework.session.security.SpringSessionBackedSessionRegistry;

import com.hisun.lemon.gateway.common.response.ResponseMessageResolver;
import com.hisun.lemon.gateway.service.IAuthenticationService;
import com.hisun.lemon.gateway.service.ISecurityService;
import com.hisun.lemon.gateway.session.refresh.TokenService;

/**
 * gateway spring security configuration
 * 
 * @author yuzhou
 * @date 2017年9月9日
 * @time 下午1:59:42
 *
 */
@Configuration
@AutoConfigureBefore(LemonSecurityConfiguration.class)
public class LemonSecurityInfrastructureConfiguration {
    
    @Value("${lemon.session.refreshToken:true}")
    private Boolean refreshToken;
    
    private ResponseMessageResolver responseMessageResolver;
    
    @Autowired
    public LemonSecurityInfrastructureConfiguration(ResponseMessageResolver responseMessageResolver) {
        this.responseMessageResolver = responseMessageResolver;
    }
    
    @Bean
    public AccessDeniedHandler lemonAccessDeniedHandler() {
        return new LemonAccessDeniedHandler(this.responseMessageResolver);
    }
    
    @Bean
    public AuthenticationEntryPoint lemonAuthenticationEntryPoint() {
        return new LemonAuthenticationEntryPoint(this.responseMessageResolver);
    }
    
    @Bean
    public AuthenticationFailureHandler lemonAuthenticationFailureHandler() {
        return new LemonAuthenticationFailureHandler(this.responseMessageResolver);
    }
    
    @Bean
    public AuthenticationSuccessHandler lemonAuthenticationSuccessHandler(TokenService tokenService) {
        return new LemonAuthenticationSuccessHandler(this.responseMessageResolver, tokenService, this.refreshToken);
    }
    
    @Bean
    public LogoutSuccessHandler lemonLogoutSuccessHandler(TokenService tokenService) {
        return new LemonLogoutSuccessHandler(this.responseMessageResolver, tokenService);
    }
    
    @Bean
    public LogoutHandler lemonSaveSessionIdLogoutHandler() {
        return new LogoutHandler() {
            @Override
            public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
                request.setAttribute(LemonLogoutSuccessHandler.REQUEST_ATTRIBUTE_KEY_SESSION_ID, Optional.ofNullable(request.getSession(false)).map(HttpSession::getId).orElse(null));
            }
        };
    }
    
    @Bean
    public LemonAuthenticationProvider lemonAuthenticationProvider(IAuthenticationService authenticationServiceImpl, ISecurityService securityServiceImpl) {
        return new LemonAuthenticationProvider(authenticationServiceImpl, securityServiceImpl);
    }
    
    @Bean
    public SessionInformationExpiredStrategy lemonSessionInformationExpiredStrategy() {
        return new LemonSessionInformationExpiredStrategy(this.responseMessageResolver);
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Bean
    public SessionRegistry sessionRegistry(RedisOperationsSessionRepository sessionRepository) {
        return new SpringSessionBackedSessionRegistry((FindByIndexNameSessionRepository)sessionRepository);
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Bean
    public SessionAuthenticationStrategy sessionAuthenticationStrategy(SessionRegistry sessionRegistry, SessionRepository sessionRepository, TokenService tokenService) {
        return new LemonSessionAuthenticationStrategy(sessionRegistry, sessionRepository, tokenService);
    }
}
