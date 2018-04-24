package com.hisun.lemon.framework.data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Locale;

/**
 * lemon data
 * @author yuzhou
 * @date 2017年6月30日
 * @time 下午4:11:38
 *
 */
public class LemonData {
    /**
     * 请求流水号
     */
    private String requestId;
    /**
     * 交易流水号
     */
    private String msgId;
    /**
     * 记账日期
     */
    private LocalDate accDate;
    /**
     * 交易发起时间
     */
    private LocalDateTime startDateTime;
    /**
     * 区域
     */
    private Locale locale;
    /**
     * 路径信息，用|隔开
     */
    private String routeInfo;
    
    /**
     * 登录用户ID
     */
    private String userId;
    
    private String mblNo;
    
    private String channel;
    
    private String clientIp;
    
    private String source;
    
    private String business;
    
    private String uri;
    
    private String token;
    
    private String loginName;
    
    public String getRequestId() {
        return requestId;
    }
    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
    public String getMsgId() {
        return msgId;
    }
    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }
    public LocalDate getAccDate() {
        return accDate;
    }
    public void setAccDate(LocalDate accDate) {
        this.accDate = accDate;
    }
    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }
    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }
    public Locale getLocale() {
        return locale;
    }
    public void setLocale(Locale locale) {
        this.locale = locale;
    }
    public String getRouteInfo() {
        return routeInfo;
    }
    public void setRouteInfo(String routeInfo) {
        this.routeInfo = routeInfo;
    }
    
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    
    public String getChannel() {
        return channel;
    }
    public void setChannel(String channel) {
        this.channel = channel;
    }
    public String getClientIp() {
        return clientIp;
    }
    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }
    public String getSource() {
        return source;
    }
    public void setSource(String source) {
        this.source = source;
    }
    
    public String getMblNo() {
        return mblNo;
    }
    public void setMblNo(String mblNo) {
        this.mblNo = mblNo;
    }
    
    public String getBusiness() {
        return business;
    }
    public void setBusiness(String business) {
        this.business = business;
    }
    public String getUri() {
        return uri;
    }
    public void setUri(String uri) {
        this.uri = uri;
    }
    
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    
    public String getLoginName() {
        return loginName;
    }
    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }
    @Override
    public String toString() {
        return "LemonData [requestId=" + requestId + ", msgId=" + msgId
                + ", accDate=" + accDate + ", startDateTime=" + startDateTime
                + ", locale=" + locale + ", routeInfo=" + routeInfo
                + ", userId=" + userId + ", mblNo=" + mblNo + ", channel="
                + channel + ", clientIp=" + clientIp + ", source=" + source
                + ", business=" + business + ", uri=" + uri + ", token=" + token+ ", loginName=" + loginName+ "]";
    }
    
}
