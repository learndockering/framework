package com.hisun.lemon.demo3.common;


/**
 * 错误码
 * @author yuzhou
 * @date 2017年6月14日
 * @time 下午4:40:05
 *
 */
public enum MsgCd {
    USER_IS_NULL("DM300001", "User is null.");
    
    private String msgCd;
    private String msgInfo;
    private MsgCd(String msgCd, String msgInfo) {
        this.msgCd = msgCd;
        this.msgInfo = msgInfo;
    }
    public String getMsgCd() {
        return msgCd;
    }
    public String getMsgInfo() {
        return msgInfo;
    }
    
}
