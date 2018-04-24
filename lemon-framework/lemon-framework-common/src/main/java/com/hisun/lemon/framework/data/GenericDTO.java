package com.hisun.lemon.framework.data;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Locale;

import javax.validation.Valid;

import com.hisun.lemon.common.utils.BeanUtils;
import com.hisun.lemon.common.utils.JudgeUtils;
import com.hisun.lemon.common.utils.ReflectionUtils;

/**
 * 通用数据传输对象
 * <p>其他的数据传输对象有2种扩展方式：
 * <ul>
 * <li>1，将其他对象(类型为T)set到GenericDTO&ltT>的body属性
 * <li>2，继承GenericDTO&ltNoBody>
 * </ul>
 * <p>建议采用第1种方案。
 * 
 * <p> Feign对外暴露的接口的返回对象，必须是GenericDTO类型; 如果真实返回对象是GenericDTO的子类，也必须写成返回GenericDTO类型；
 * 建议使用第1种扩展方案。
 * <p> Feign对外暴露的接口的输入参数，必须有一个参数是GenericDTO类型或者其子类型
 * 
 * <p> GenericDTO的泛型即Body属性的对象类型，如果对象为空，则泛型为NoBody，即 GenericDTO&ltNoBody>.
 * <p> GenericDTO对象及其子类对象除msgCd属性外，其他所有的属性由平台自行赋值，业务代码只能进行获取操作
 * 
 * <p> GenericDTO对象及其子对象的生成建议采用该类的静态方法创建，例如创建成功的通用Generic&ltNoBody>对象：
 * <p> GenericDTO.newSuccessInstance()
 * <p> 创建Generic<User>对象，并且将User对象设置到body属性:
 * <p> GenericDTO.newSuccessInstance(user);
 * <p> 创建GenericDTO&ltNoBody>子类型对象：
 * <p> UserDTO userDTO = GenericDTO.newSuccessInstance(UserDTO.class);
 * <p> 还有更多的静态方法见代码
 * 
 * @author yuzhou
 * @date 2017年6月13日
 * @time 下午5:39:19
 *
 */
@ApiModel(value="GenericDTO", description="通用数据传输对象")
public class GenericDTO<T> {
    
    /**
     * 请求流水号，同一笔业务，请求所有系统的请求流水号一致
     * 
     */
    @ApiModelProperty(hidden = true)
    private String requestId;
    /**
     * 交易流水号，只代表一次交易，请求和返回DTO对象msgID一致
     */
    @ApiModelProperty(hidden = true)
    private String msgId;
    /**
     * 会计日期
     */
    @ApiModelProperty(hidden = true)
    private LocalDate accDate;
    /**
     * 交易发起时间
     */
    @ApiModelProperty(hidden = true)
    private LocalDateTime startDateTime;
    /**
     * 区域
     */
    @ApiModelProperty(hidden = true)
    private Locale locale;
    
    /**
     * 路径信息，用|隔开
     */
    @ApiModelProperty(hidden = true)
    private String routeInfo;
    
    /**
     * 登录用户ID
     * 
     */
    @ApiModelProperty(hidden = true)
    private String userId;
    
    /**
     * 客户端IP
     */
    @ApiModelProperty(hidden=true)
    private String clientIp;
    
    /**
     * 请求来源
     * API网关之类
     */
    @ApiModelProperty(hidden=true)
    private String source;
    
    /**
     * 渠道
     */
    @ApiModelProperty(hidden=true)
    private String channel;
    
    /**
     * 业务
     */
    @ApiModelProperty(hidden=true)
    private String business;
    
    /**
     * 请求URI
     */
    @ApiModelProperty(hidden=true)
    private String uri;
    
    /**
     * sessionId
     */
    @ApiModelProperty(hidden=true)
    private String token;
    
    /**
     * 登录名
     */
    @ApiModelProperty(hidden=true)
    private String loginName;
    
    /**
     * 消息体
     */
    @Valid
    @ApiModelProperty(value="业务传输对象; 非嵌套业务传输对象，忽略该属性")
    private T body;
    
    /**
     * 创建GenericDTO<T>， 并设置body
     * @param body
     * @return
     */
    public static <T> GenericDTO<T> newInstance(T body) {
        GenericDTO<T> genericDTO = new GenericDTO<>();
        genericDTO.setBody(body);
        return genericDTO;
    }
    
    /**
     * 创建GenericDTO<NoBody>
     * @return
     */
    public static GenericDTO<NoBody> newInstance() {
        GenericDTO<NoBody> genericDTO = new GenericDTO<>();
        return genericDTO;
    }
    
    /**
     * 创建GenericDTO的子类，并将other对象属性拷贝到GenericDTO子类
     * @param clazz
     * @param other
     * @return
     */
    public static <T extends GenericDTO<NoBody>, O> T newInstance(Class<T> clazz, O other) {
        T dto = ReflectionUtils.newInstance(clazz);
        if(JudgeUtils.isNotNull(other)) {
            BeanUtils.copyProperties(dto, other);
        }
        return dto;
    }
    
    /**
     * 创建GenericDTO的子类
     * @param clazz
     * @return
     */
    public static <T extends GenericDTO<NoBody>, O> T newInstance(Class<T> clazz) {
        return newInstance(clazz, null);
    }

    public String getMsgId() {
        return msgId;
    }
    void setMsgId(String msgId) {
        this.msgId = msgId;
    }
    
    public Locale getLocale() {
        return locale;
    }
    void setLocale(Locale locale) {
        this.locale = locale;
    }
    
    public LocalDate getAccDate() {
        return accDate;
    }
    void setAccDate(LocalDate accDate) {
        this.accDate = accDate;
    }
    
    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }
    
    void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }
    
    public String getRouteInfo() {
        return routeInfo;
    }
    void setRouteInfo(String routeInfo) {
        this.routeInfo = routeInfo;
    }
    
    public String getRequestId() {
        return requestId;
    }
    
    void setRequestId(String requestId) {
        this.requestId = requestId;
    }
    
    public String getUserId() {
        return userId;
    }
    void setUserId(String userId) {
        this.userId = userId;
    }
    
    public T getBody() {
        return body;
    }
    public void setBody(T body) {
        this.body = body;
    }

    public String getClientIp() {
        return clientIp;
    }

    void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public String getSource() {
        return source;
    }

    void setSource(String source) {
        this.source = source;
    }

    public String getChannel() {
        return channel;
    }

    void setChannel(String channel) {
        this.channel = channel;
    }

    public String getBusiness() {
        return business;
    }

    void setBusiness(String business) {
        this.business = business;
    }

    public String getUri() {
        return uri;
    }

    void setUri(String uri) {
        this.uri = uri;
    }

    public String getToken() {
        return token;
    }

    void setToken(String token) {
        this.token = token;
    }

    public String getLoginName() {
        return loginName;
    }

    void setLoginName(String loginName) {
        this.loginName = loginName;
    }
    
}
