package com.hisun.lemon.gateway.security;

import java.io.Serializable;

/**
 * @author yuzhou
 * @date 2017年9月13日
 * @time 下午3:16:05
 *
 */
public class LoginInfo implements Serializable{
    /**
     * 
     */
    private static final long serialVersionUID = 2717013050391516903L;
    private String loginName;

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }
}
