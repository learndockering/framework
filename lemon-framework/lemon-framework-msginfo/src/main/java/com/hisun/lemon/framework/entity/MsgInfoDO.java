package com.hisun.lemon.framework.entity;

import java.io.Serializable;

import com.hisun.lemon.framework.data.BaseDO;

/**
 * 消息码信息
 * 
 * @author yuzhou
 * @date 2017年6月27日
 * @time 下午1:27:40
 *
 */
public class MsgInfoDO extends BaseDO implements Serializable{
    private static final long serialVersionUID = 5299564182533606103L;
    private String msgCd;
    private String language;
    private String scenario;
    private String msgInfo;
    
    public String getMsgCd() {
        return msgCd;
    }
    public void setMsgCd(String msgCd) {
        this.msgCd = msgCd;
    }
    public String getMsgInfo() {
        return msgInfo;
    }
    public void setMsgInfo(String msgInfo) {
        this.msgInfo = msgInfo;
    }
    public String getLanguage() {
        return language;
    }
    public void setLanguage(String language) {
        this.language = language;
    }
    public String getScenario() {
        return scenario;
    }
    public void setScenario(String scenario) {
        this.scenario = scenario;
    }
    
}
