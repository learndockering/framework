package com.hisun.lemon.gateway.session.refresh;

import javax.servlet.http.HttpSession;


/**
 * token service
 * @author yuzhou
 * @date 2017年9月13日
 * @time 下午2:09:33
 *
 */
public interface TokenService {
    AccessToken createAccessToken(HttpSession session);
    
    AccessToken getAccessToken(HttpSession session);
    
    AccessToken refreshAccessToken(HttpSession session, String refreshTokenValue);
    
    void revokeToken(HttpSession session, String accessTokenValue);
}
