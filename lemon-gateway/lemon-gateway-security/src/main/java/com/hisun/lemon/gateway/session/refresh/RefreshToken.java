package com.hisun.lemon.gateway.session.refresh;

import java.util.UUID;

/**
 * @author yuzhou
 * @date 2017年9月13日
 * @time 上午11:18:42
 *
 */
public interface RefreshToken {
    String getValue();
    
    default String getRefreshTokenId() {
        return UUID.randomUUID().toString();
    }
}
