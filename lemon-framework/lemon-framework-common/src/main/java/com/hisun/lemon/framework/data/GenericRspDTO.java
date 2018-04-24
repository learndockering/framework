package com.hisun.lemon.framework.data;

import com.hisun.lemon.common.utils.BeanUtils;
import com.hisun.lemon.common.utils.JudgeUtils;
import com.hisun.lemon.common.utils.ReflectionUtils;
import com.hisun.lemon.framework.utils.LemonUtils;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 通用响应DTO
 * @author yuzhou
 * @date 2017年7月28日
 * @time 下午5:20:23
 *
 * @param <T>
 */
@ApiModel(value="GenericRspDTO", description="通用响应数据传输对象")
public class GenericRspDTO<T> extends GenericDTO<T> {
    /**
     * 消息码
     */
    @ApiModelProperty(value="消息码", readOnly=true)
    private String msgCd;
    /**
     * 消息
     */
    @ApiModelProperty(value="消息", readOnly=true)
    private String msgInfo;
    
    /**
     * 创建消息码为msgCd的GenericRspDTO<T>， 并设置body
     * @param msgCd
     * @param msgInfo
     * @param body
     * @return
     */
    public static <T> GenericRspDTO<T> newInstance(String msgCd, String msgInfo, T body) {
        GenericRspDTO<T> genericDTO = new GenericRspDTO<>();
        genericDTO.setMsgCd(msgCd);
        genericDTO.setMsgInfo(msgInfo);
        genericDTO.setBody(body);
        return genericDTO;
    }
    
    /**
     * dummy instance
     * @return
     */
    public static GenericRspDTO<NoBody> newInstance() {
         return newInstance((String) null, (String) null, (NoBody)null);
    }
    
    /**
     * 创建消息码为msgCd的GenericRspDTO<T>， 并设置body
     * @param msgCd
     * @param body
     * @return
     */
    public static <T> GenericRspDTO<T> newInstance(String msgCd, T body) {
        return newInstance(msgCd, (String)null, body);
    }
    
    /**
     * 创建GenericRspDTO<NoBody> 实例
     * @param msgCd
     * @param msgInfo
     * @return
     */
    public static GenericRspDTO<NoBody> newInstance(String msgCd, String msgInfo) {
        return newInstance(msgCd, msgInfo, null);
    }
    
    /**
     * 创建GenericRspDTO<NoBody>实例
     * @param msgCd
     * @return
     */
    public static GenericRspDTO<NoBody> newInstance(String msgCd) {
        return newInstance(msgCd, (String)null, null);
    }
    
    /**
     * 创建成功GenericRspDTO<T>实例,并setBody(T)
     * @param clazz GenericDTO 泛型类型
     * @return
     */
    public static <T> GenericRspDTO<T> newSuccessInstance(T body){
        GenericRspDTO<T> genericDTO = new GenericRspDTO<>();
        genericDTO.setMsgCd(LemonUtils.getSuccessMsgCd());
        genericDTO.setBody(body);
        return genericDTO;
    }
    
    /**
     * 创建成功的GenericRspDTO<NoBody>实例
     * @return
     */
    public static GenericRspDTO<NoBody> newSuccessInstance(){
        GenericRspDTO<NoBody> genericDTO = new GenericRspDTO<>();
        genericDTO.setMsgCd(LemonUtils.getSuccessMsgCd());
        return genericDTO;
    }
    
    /**
     * 创建成功的GenericRspDTO<NoBody>的子类,并且将other对象copy到DTO对象
     * 
     * @param clazz
     * @return
     */
    public static <T extends GenericRspDTO<NoBody>, O> T newSuccessInstance(Class<T> clazz, O other) {
        T dto = ReflectionUtils.newInstance(clazz);
        dto.setMsgCd(LemonUtils.getSuccessMsgCd());
        if(JudgeUtils.isNotNull(other)) {
            BeanUtils.copyProperties(dto, other);
        }
        return dto;
    }
    
    /**
     * 创建成功的GenericRspDTO<NoBody>的子类
     * @param clazz
     * @return
     */
    public static <T extends GenericRspDTO<NoBody>> T newSuccessInstance(Class<T> clazz) {
        return newSuccessInstance(clazz, null);
    }
    
    /**
     * 创建GenericRspDTO<NoBody> 的子类
     * @param msgCd
     * @param clazz
     * @param other
     * @return
     */
    public static <T extends GenericRspDTO<NoBody>, O> T newInstance(String msgCd, Class<T> clazz, O other) {
        T dto = ReflectionUtils.newInstance(clazz);
        dto.setMsgCd(msgCd);
        if(JudgeUtils.isNotNull(other)) {
            BeanUtils.copyProperties(dto, other);
        }
        return dto;
    }
    
    /**
     * 创建GenericRspDTO<NoBody> 的子类
     * @param msgCd
     * @param clazz
     * @return
     */
    public static <T extends GenericRspDTO<NoBody>, O> T newInstance(String msgCd, Class<T> clazz) {
        return newInstance(msgCd, clazz, null);
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

    @Override
    public String toString() {
        return "GenericRspDTO [msgCd=" + msgCd + ", msgInfo=" + msgInfo + "]";
    }
    
}
