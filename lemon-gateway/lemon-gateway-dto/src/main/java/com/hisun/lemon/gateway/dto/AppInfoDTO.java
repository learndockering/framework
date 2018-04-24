package com.hisun.lemon.gateway.dto;

/**
 * @author yuzhou
 * @date 2017年8月14日
 * @time 上午11:28:21
 *
 */
public class AppInfoDTO {
    private String version;
    private String downloadChannel;
    private String osVersion;
    private String termId;
    private String termType;
    private String userType;
    private String userId;
    
    public String getVersion() {
        return version;
    }
    public void setVersion(String version) {
        this.version = version;
    }
    public String getDownloadChannel() {
        return downloadChannel;
    }
    public void setDownloadChannel(String downloadChannel) {
        this.downloadChannel = downloadChannel;
    }
    public String getOsVersion() {
        return osVersion;
    }
    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }
    public String getTermId() {
        return termId;
    }
    public void setTermId(String termId) {
        this.termId = termId;
    }
    public String getTermType() {
        return termType;
    }
    public void setTermType(String termType) {
        this.termType = termType;
    }
    public String getUserType() {
        return userType;
    }
    public void setUserType(String userType) {
        this.userType = userType;
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    
}
