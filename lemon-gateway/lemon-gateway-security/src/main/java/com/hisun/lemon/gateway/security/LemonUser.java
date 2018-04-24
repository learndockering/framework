package com.hisun.lemon.gateway.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

/**
 * lemon 用户信息
 * @author yuzhou
 * @date 2017年7月29日
 * @time 上午10:58:04
 *
 * @param <T>
 */
public class LemonUser<T> extends User{
    private static final long serialVersionUID = 8957124042836925579L;
    
    /**
     * 登录channel
     */
    private String loginChannel;
    /**
     * 详细用户信息
     */
    private T userInfo;
    
    public LemonUser(String username, T userInfo, String loginChannel, boolean enabled,
            boolean accountNonExpired, boolean credentialsNonExpired,
            boolean accountNonLocked,
            Collection<? extends GrantedAuthority> authorities) {
        super(username, "", enabled, accountNonExpired, credentialsNonExpired,
                accountNonLocked, authorities);
        this.userInfo = userInfo;
        this.loginChannel = loginChannel;
    }

    /**
     * 获取用户详情
     * @return
     */
    public T getUserInfo() {
        return this.userInfo;
    }
    
    /**
     * 登录认证的channel
     * @return
     */
    public String getLoginChannel() {
        return this.loginChannel;
    }
}
