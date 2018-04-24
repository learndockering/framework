package com.hisun.lemon.common.exception;

/**
 * 框架错误码
 * @author yuzhou
 * @date 2017年6月20日
 * @time 下午6:12:34
 *
 */
public enum ErrorMsgCode {
    SYS_ERROR("SYS00001"),                                      //系统异常消息码
    ACCESS_DATABASE_ERROR("SYS00002"),                          //访问数据库异常
    SIGNATURE_EXCEPTION("SYS00003"),                            //签名异常
    NO_HANDLER_FOUND_ERROR("SYS00404"),                         //404异常
    NO_AUTH_ERROR("SYS00401"),                                  //401异常
    FORBIDDEN_OPERATION("SYS00403","FORBIDDEN_OPERATION"),      //禁止操作
    THREAD_POOL_FULL_ERROR("SYS00004"),                         //线程池满异常
    SCHEDULE_TASK_EXCEPTION("SYS00005"),                        //task schedule exception
    SERVER_RESOURCE_NOT_FOUND("SYS00006"),                      //服务端404错误
    SERVER_NOT_AVAILABLE("SYS00007"),                           //服务不可用
    UNABLE_ACQUIRE_DISTRUBUTED_LOCK("SYS00100"),                //不能获取分布式锁
    CUMULATIVE_ERROR("SYS00101"),                               //累计操作异常
    BEAN_VALIDATION_ERROR("SYS10001"),                          //bean validation exception
    CLIENT_EXCEPTION("SYS20000"),                               //client exception
    CLIENT_EXCEPTION_UNKNOWN_HOST("SYS20001"),                  //UnknownHostException
    CLIENT_TIMEOUT("SYS20002"),                                 //timeout excepiton
    ILLEGAL_PARAMETER("SYS30001"),                              //illegal parameter
    ILLEGAL_HTTP_REQUEST_HEADER("SYS30002"),                    //validate http header exception
    PRODUCER_RABBIT_EXCEPTION("SYS40001"),                      //producer of rabbit exception
    CONSUMER_RABBIT_EXCEPTION("SYS40021"),                      //consumer of rabbit exception
    MSG_CD_NOT_EXISTS("SYS99999"),                              //no msg_cd set 
    CONTINUE_RESOLVE("SYS88888");                               //继续解析
    
    private String msgCd;
    private String msgInfo;
    
    /**
     * @param msgCd
     * @param msgInfo
     */
    ErrorMsgCode(String msgCd, String msgInfo) {
        this.msgCd = msgCd;
        this.msgInfo = msgInfo;
    }
    
    ErrorMsgCode(String msgCd) {
        this.msgCd = msgCd;
    }
    
    public String getMsgCd() {
        return this.msgCd;
    }
    
    public String getMsgInfo() {
        return this.msgInfo;
    }
}
