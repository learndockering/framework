package com.hisun.lemon.gateway.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

/**
 * @author yuzhou
 * @date 2017年9月12日
 * @time 上午10:06:44
 *
 */
public class LemonUsernamePasswordAuthenticationToken extends UsernamePasswordAuthenticationToken {
    private static final long serialVersionUID = 3879132540866188019L;
    private String random;
    
    public LemonUsernamePasswordAuthenticationToken(Object principal, Object credentials, String random) {
        super(principal, credentials);
        this.random = random;
    }

    public String getRandom() {
        return random;
    }

    public void setRandom(String random) {
        this.random = random;
    }
    
}
