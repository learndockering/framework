package com.hisun.lemon.gateway.session.refresh;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.session.data.redis.RedisOperationsSessionRepository;

import com.hisun.lemon.gateway.common.response.ResponseMessageResolver;
import com.hisun.lemon.gateway.security.LemonAuthenticationSuccessHandler;
import com.hisun.lemon.gateway.service.ISecurityService;
import com.hisun.lemon.gateway.service.IUserService;
import com.hisun.lemon.gateway.session.refresh.redis.RedisTokenStore;

/**
 * @author yuzhou
 * @date 2017年9月13日
 * @time 下午3:54:52
 *
 */
@Configuration
public class SessionRefreshTokenConfiguration {
    
    @Value("${lemon.session.refreshTokenExpiration:30}")
    private Integer refreshTokenExpiration;
    
    @Bean
    public TokenStore tokenStore(RedisConnectionFactory connectionFactory) {
        return new RedisTokenStore(connectionFactory);
    }
    
    @Bean
    public TokenService defaultTokenService(TokenStore tokenStore, RedisOperationsSessionRepository sessionRepository) {
        return new DefaultTokenService(tokenStore, sessionRepository, refreshTokenExpiration);
    }
    
    @Bean
    public RefreshTokenAuthenticationProvider refreshTokenAuthenticationProvider(TokenService tokenService, IUserService userServiceImpl, ISecurityService securityServiceImpl) {
        return new RefreshTokenAuthenticationProvider(userServiceImpl, securityServiceImpl, tokenService);
    }
    
    @Bean
    public LemonAuthenticationSuccessHandler nonRefreshTokenLemonAuthenticationSuccessHandler(ResponseMessageResolver responseMessageResolver, TokenService tokenService) {
        return new LemonAuthenticationSuccessHandler(responseMessageResolver, tokenService, false);
    }
}
