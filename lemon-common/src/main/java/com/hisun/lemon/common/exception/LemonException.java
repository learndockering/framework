package com.hisun.lemon.common.exception;

import com.hisun.lemon.common.utils.JudgeUtils;

/**
 * lemon framework 统一异常
 * 抛出该异常会回滚事务
 * 消息码会做统一处理返回给调用方
 * 
 * @author yuzhou
 * @date 2017年6月13日
 * @time 下午5:04:08
 *
 */
public class LemonException extends RuntimeException {
    private static final long serialVersionUID = 5539431043384054654L;
    //系统异常消息码
    public static final String SYS_ERROR_MSGCD = ErrorMsgCode.SYS_ERROR.getMsgCd();
    
    private String msgCd;
    private String msgInfo;
    private Throwable cause;
    //是否业务异常，业务代码中只能抛出业务异常
    private boolean businessException = false;
    
    /**
     * @param msgCd
     * @param msgInfo
     * @param cause
     */
    public LemonException(String msgCd, String msgInfo, Throwable cause) {
        super(msgCd+" : "+msgInfo, cause);
        this.msgCd = msgCd;
        this.msgInfo = msgInfo;
        this.cause = cause;
    }
    
    /**
     * @param msgCd
     * @param msgInfo
     */
    public LemonException(String msgCd, String msgInfo) {
        super(msgCd + " :  " + msgInfo);
        this.msgCd = msgCd;
        this.msgInfo = msgInfo;
    }
    
    /**
     * @param errorMsgCode
     */
    public LemonException(ErrorMsgCode errorMsgCode) {
        this(errorMsgCode.getMsgCd(), errorMsgCode.getMsgInfo());
    }
    
    /**
     * @param errorMsgCode
     * @param cause
     */
    public LemonException(ErrorMsgCode errorMsgCode, Throwable cause) {
        this(errorMsgCode.getMsgCd(), errorMsgCode.getMsgInfo(), cause);
    }
    
    /**
     * @param msgCd
     */
    public LemonException(String msgCd) {
        super(msgCd);
        this.msgCd = msgCd;
    }
    
    public LemonException(String msgCd, boolean businessException) {
        super(msgCd);
        this.msgCd = msgCd;
        this.businessException = businessException;
    }
    
    /**
     * @param cause
     */
    public LemonException(Throwable cause) {
        super(SYS_ERROR_MSGCD, cause);
        this.cause = cause;
        this.msgCd = SYS_ERROR_MSGCD;
    }
    
    /**
     * @param msgInfo
     * @param cause
     */
    public LemonException(String msgInfo, Throwable cause) {
        super(SYS_ERROR_MSGCD + " : " + msgInfo);
        this.msgCd = SYS_ERROR_MSGCD;
        this.msgInfo = msgInfo;
        this.cause = cause;
    }
    
    public LemonException() {
        super(SYS_ERROR_MSGCD);
        this.msgCd = SYS_ERROR_MSGCD;
    }
    
    public static void throwBusinessException(String msgCd) {
        throw new LemonException(msgCd, true);
    }
    
    public static void throwLemonException() {
        throw new LemonException();
    }
    
    /**
     * @param msgCd
     */
    public static void throwLemonException(String msgCd) {
        throw new LemonException(msgCd);
    }
    
    /**
     * @param t
     */
    public static void throwLemonException(Throwable t) {
        if(t instanceof LemonException) {
            throw (LemonException)t;
        }
        throw new LemonException(t);
    }
    
    /**
     * @param msgInfo
     * @param t
     */
    public static void throwLemonException(String msgInfo, Throwable t) {
        throw new LemonException(msgInfo, t);
    }
    
    /**
     * @param msgCd
     * @param msgInfo
     */
    public static void throwLemonException(String msgCd, String msgInfo) {
        throw new LemonException(msgCd, msgInfo);
    }
    
    /**
     * @param msgCd
     * @param msgInfo
     * @param throwable
     */
    public static void throwLemonException(String msgCd, String msgInfo, Throwable throwable) {
        throw new LemonException(msgCd, msgInfo, throwable);
    }
    
    /**
     * @param errorMsgCode
     */
    public static void throwLemonException(ErrorMsgCode errorMsgCode) {
        throw new LemonException(errorMsgCode.getMsgCd(), errorMsgCode.getMsgInfo());
    }
    
    /**
     * @param errorMsgCode
     * @param throwable
     */
    public static void throwLemonException(ErrorMsgCode errorMsgCode, Throwable throwable) {
        throw new LemonException(errorMsgCode.getMsgCd(), errorMsgCode.getMsgInfo(), throwable);
    }
    
    /**
     * 创建LemonException
     * 
     * @param t
     * @return
     */
    public static LemonException create(Throwable t) {
        if(isLemonException(t)) {
            return (LemonException) t;
        }
        return new LemonException(t);
    }
    
    public static LemonException create(String msgCd) {
        return new LemonException(msgCd);
    }
    
    public static LemonException create(String msgCd, String msgInfo) {
        return new LemonException(msgCd, msgInfo);
    }
    
    public static LemonException create(ErrorMsgCode errorMsgCode) {
        return new LemonException(errorMsgCode.getMsgCd(), errorMsgCode.getMsgInfo());
    }
    
    public static LemonException create(ErrorMsgCode errorMsgCode, Throwable throwable) {
        return new LemonException(errorMsgCode.getMsgCd(), errorMsgCode.getMsgInfo(), throwable);
    }
    
    public static boolean isLemonException(Throwable throwable) {
        if(JudgeUtils.isNotNull(throwable) && throwable instanceof LemonException) {
            return true;
        }
        return false;
    }
    
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

    public Throwable getCause() {
        return cause;
    }

    public void setCause(Throwable cause) {
        this.cause = cause;
    }

    public boolean isBusinessException() {
        return businessException;
    }

    public void setBusinessException(boolean businessException) {
        this.businessException = businessException;
    }
    
}
