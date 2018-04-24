package com.hisun.lemon.gateway.session.refresh;

import java.time.LocalDateTime;

import javax.servlet.http.HttpSession;

import org.springframework.session.data.redis.RedisOperationsSessionRepository;

import com.hisun.lemon.common.exception.ErrorMsgCode;
import com.hisun.lemon.common.exception.LemonException;
import com.hisun.lemon.common.utils.DateTimeUtils;
import com.hisun.lemon.common.utils.JudgeUtils;
import com.hisun.lemon.gateway.security.SecurityUtils;

/**
 * @author yuzhou
 * @date 2017年9月13日
 * @time 下午2:09:44
 *
 */
public class DefaultTokenService implements TokenService {
    private static final String SESSION_ATTRIBUTE_KEY_ACCESS_TOKEN = "SESSION_ATTRIBUTE_KEY_ACCESS_TOKEN";
    
    private TokenStore tokenStore;
    private int refreshTokenExpiration;
    private boolean reuseRefreshToken = false;
    private RedisOperationsSessionRepository sessionRepository;
    
    public DefaultTokenService(TokenStore tokenStore, RedisOperationsSessionRepository sessionRepository, int refreshTokenExpiration) {
        this.tokenStore = tokenStore;
        this.refreshTokenExpiration = refreshTokenExpiration;
        this.sessionRepository = sessionRepository;
    }
    
    @Override
    public AccessToken createAccessToken(HttpSession session) {
        RefreshToken refreshToken = createRefreshToken();
        this.tokenStore.storeRefreshToken(refreshToken);
        AccessToken accessToken  = createAccessToken(session, refreshToken);
        session.setAttribute(SESSION_ATTRIBUTE_KEY_ACCESS_TOKEN, accessToken);
        this.tokenStore.storeAccessToken(accessToken);
        return accessToken;
    }
    
    /**
     * @return
     */
    private RefreshToken createRefreshToken() {
        return createRefreshToken(SecurityUtils.getLoginUser().getLoginName());
    }
    
    /**
     * @return
     */
    private RefreshToken createRefreshToken(String userId) {
        LocalDateTime expire = DateTimeUtils.getCurrentLocalDateTime().plusDays(refreshTokenExpiration);
        LemonRefreshToken refreshToken = new LemonRefreshToken(expire);
        refreshToken.setUserId(userId);
        return refreshToken;
    }

    private AccessToken createAccessToken(HttpSession session, RefreshToken refreshToken) {
        LemonAccessToken token = new LemonAccessToken(session.getId());
        token.setRefreshToken(refreshToken);
        return token;
    }

    @Override
    public AccessToken getAccessToken(HttpSession session) {
        return (AccessToken)session.getAttribute(SESSION_ATTRIBUTE_KEY_ACCESS_TOKEN);
    }

    @Override
    public AccessToken refreshAccessToken(HttpSession session, String refreshTokenValue) {
        LemonAccessToken accessToken = new LemonAccessToken(session.getId());
        RefreshToken refreshToken = this.tokenStore.readRefreshToken(refreshTokenValue);
        if(JudgeUtils.isNull(refreshToken)) {
            throw new InvalidRefreshToken("Invalid refresh token: " + refreshTokenValue);
        }
        
        //删除accessToken,不一定是原session
        session.removeAttribute(SESSION_ATTRIBUTE_KEY_ACCESS_TOKEN);
        String accessTokenValue = this.tokenStore.removeAccessTokenUsingRefreshToken(refreshTokenValue);
        if(JudgeUtils.isNotBlank(accessTokenValue)) {
            sessionRepository.delete(accessTokenValue);
        }
        
        if(isExpired(refreshToken)) {
            throw new InvalidRefreshToken("Invalid refresh token (expire): " + refreshTokenValue);
        }
        
        if(! reuseRefreshToken) {
            String userId = null;
            if(refreshToken instanceof LemonRefreshToken) {
                userId = ((LemonRefreshToken) refreshToken).getUserId();
            }
            this.tokenStore.removeRefreshToken(refreshToken);
            refreshToken = createRefreshToken(userId);
            this.tokenStore.storeRefreshToken(refreshToken);
        }
        
        accessToken.setRefreshToken(refreshToken);
        this.tokenStore.storeAccessToken(accessToken);
        session.setAttribute(SESSION_ATTRIBUTE_KEY_ACCESS_TOKEN, accessToken);
        return accessToken;
    }
    
    protected boolean isExpired(RefreshToken refreshToken) {
        if (refreshToken instanceof LemonRefreshToken) {
            LemonRefreshToken expiringToken = (LemonRefreshToken) refreshToken;
            return expiringToken.getExpiration() == null
                    || System.currentTimeMillis() > DateTimeUtils.toEpochMilli(expiringToken.getExpiration());
        }
        return false;
    }

    /* 
     * session 有可能为null
     * @see com.hisun.lemon.gateway.session.refresh.TokenService#revokeToken(javax.servlet.http.HttpSession, java.lang.String)
     */
    @Override
    public void revokeToken(HttpSession session, String accessTokenValue) {
        AccessToken accessToken = this.tokenStore.readAccessToken(accessTokenValue);
        this.tokenStore.removeAccessToken(accessTokenValue);
        if(JudgeUtils.isNotNull(accessToken.getRefreshToken())) {
            this.tokenStore.removeRefreshToken(accessToken.getRefreshToken());
        }
        if(JudgeUtils.isNotNull(session)) {
            session.removeAttribute(SESSION_ATTRIBUTE_KEY_ACCESS_TOKEN);
        }
    }
    
    public static class InvalidRefreshToken extends LemonException {
        private static final long serialVersionUID = -2429093621333533895L;
        public InvalidRefreshToken(String msgInfo) {
            super(ErrorMsgCode.NO_AUTH_ERROR.getMsgCd(), msgInfo);
        }
    }

}
