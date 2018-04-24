package com.hisun.lemon.gateway.session.refresh;

import org.springframework.security.authentication.AbstractAuthenticationToken;

/**
 * @author yuzhou
 * @date 2017年9月13日
 * @time 下午5:12:59
 *
 */
public class RefreshTokenAuthenticationToken extends AbstractAuthenticationToken {
    private static final long serialVersionUID = -611230686173603990L;
    private String refreshToken;
    private String principal;
    
    public RefreshTokenAuthenticationToken(String refreshToken) {
        super(null);
        this.refreshToken = refreshToken;
    }

    @Override
    public Object getCredentials() {
        return refreshToken;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    void setPrincipal(String principal) {
        this.principal = principal;
    }

}
