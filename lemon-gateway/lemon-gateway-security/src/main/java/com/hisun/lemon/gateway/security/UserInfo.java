package com.hisun.lemon.gateway.security;

import java.io.Serializable;

import com.hisun.lemon.gateway.bo.UserInfoBase;

/**
 * 用户信息
 * 
 * @author yuzhou
 * @date 2017年7月29日
 * @time 上午11:26:06
 *
 */
public class UserInfo extends LoginInfo implements UserInfoBase, Serializable{
    private static final long serialVersionUID = 9057025567945640190L;
    
    private String userId;
    
    private Object userBasicInfo;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Object getUserBasicInfo() {
        return userBasicInfo;
    }

    public void setUserBasicInfo(Object userBasicInfo) {
        this.userBasicInfo = userBasicInfo;
    }

}
