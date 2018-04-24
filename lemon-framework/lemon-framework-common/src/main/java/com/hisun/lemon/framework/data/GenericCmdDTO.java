package com.hisun.lemon.framework.data;

import java.time.LocalDateTime;

import com.hisun.lemon.common.utils.DateTimeUtils;
import com.hisun.lemon.framework.utils.LemonUtils;

/**
 * 可执行消息
 * @author yuzhou
 * @date 2017年8月10日
 * @time 下午8:08:53
 *
 * @param <T>
 */
public class GenericCmdDTO<T> extends GenericDTO<T> {
    
    public GenericCmdDTO(String beanName) {
        this.beanName = beanName;
        this.sendDateTime = DateTimeUtils.getCurrentLocalDateTime();
        this.sender = LemonUtils.getApplicationName();
    }
    
    private String beanName;
    
    private String sender;
    
    private LocalDateTime sendDateTime;

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }
    
    public LocalDateTime getSendDateTime() {
        return sendDateTime;
    }

    public void setSendDateTime(LocalDateTime sendDateTime) {
        this.sendDateTime = sendDateTime;
    }
    
    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public static <K> GenericCmdDTO<K> newCmdInstance(String beanName, K body) {
        GenericCmdDTO<K> cmd = new GenericCmdDTO<>(beanName);
        cmd.setBody(body);
        return cmd;
    }

    @Override
    public String toString() {
        return "GenericCmdDTO [beanName=" + beanName + ", sender=" + sender
                + ", sendDateTime=" + sendDateTime + ", getMsgId()="
                + getMsgId() + ", getLocale()=" + getLocale()
                + ", getAccDate()=" + getAccDate() + ", getStartDateTime()="
                + getStartDateTime() + ", getRouteInfo()=" + getRouteInfo()
                + ", getRequestId()=" + getRequestId() + ", getUserId()="
                + getUserId() + ", getBody()=" + getBody() + ", getClientIp()="
                + getClientIp() + ", getSource()=" + getSource()
                + ", getChannel()=" + getChannel() + ", getBusiness()="
                + getBusiness() + ", getUri()=" + getUri() + ", getToken()="
                + getToken() + ", getLoginName()=" + getLoginName() + "]";
    }


}
