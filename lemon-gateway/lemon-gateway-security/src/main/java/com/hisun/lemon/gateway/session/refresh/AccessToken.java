package com.hisun.lemon.gateway.session.refresh;


/**
 * @author yuzhou
 * @date 2017年9月13日
 * @time 上午11:38:28
 *
 */
public interface AccessToken {
    String getValue();
    RefreshToken getRefreshToken();
}
