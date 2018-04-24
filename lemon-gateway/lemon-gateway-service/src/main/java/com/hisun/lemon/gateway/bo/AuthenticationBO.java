package com.hisun.lemon.gateway.bo;

/**
 * 认证对象
 * @author yuzhou
 * @date 2017年7月29日
 * @time 上午11:47:59
 *
 */
public class AuthenticationBO {
    private String userName;
    private String password;
    private String random;
    
    public AuthenticationBO(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }
    public AuthenticationBO(String userName, String password, String random) {
        this.userName = userName;
        this.password = password;
        this.random = random;
    }
    
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getRandom() {
        return random;
    }
    public void setRandom(String random) {
        this.random = random;
    }
    
}
