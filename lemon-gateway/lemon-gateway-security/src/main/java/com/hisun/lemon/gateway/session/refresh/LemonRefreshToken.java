package com.hisun.lemon.gateway.session.refresh;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author yuzhou
 * @date 2017年9月13日
 * @time 上午11:25:23
 *
 */
public class LemonRefreshToken implements RefreshToken, Serializable{
    private static final long serialVersionUID = -6581683999356822725L;
    private String value;
    private LocalDateTime expiration;
    private String userId;
    
    public LemonRefreshToken(LocalDateTime expiration) {
        this.value = getRefreshTokenId();
        this.expiration = expiration;
    }
    
    @Override
    public String getValue() {
        return this.value;
    }

    public LocalDateTime getExpiration() {
        return expiration;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

}
