package com.hisun.lemon.gateway.session.refresh;

/**
 * @author yuzhou
 * @date 2017年9月13日
 * @time 上午11:35:49
 *
 */
public interface TokenStore {
    void storeRefreshToken(RefreshToken refreshToken);
    
    void storeAccessToken(AccessToken accessToken);
    
    void removeRefreshToken(RefreshToken refreshToken);
    
    RefreshToken readRefreshToken(String refreshTokenValue);
    
    String removeAccessTokenUsingRefreshToken(String refreshTokenValue);
    
    void removeAccessToken(String tokenValue);
    
    AccessToken readAccessToken(String tokenValue);
}
