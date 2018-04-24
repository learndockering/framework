package com.hisun.lemon.gateway.security;

import org.springframework.security.core.AuthenticationException;

import com.hisun.lemon.framework.data.GenericRspDTO;

/**
 * 认证失败
 * @author yuzhou
 * @date 2017年7月29日
 * @time 上午11:38:11
 *
 */
public class LemonAuthenticationException extends AuthenticationException {
    private static final long serialVersionUID = -3089720146026816187L;
    private GenericRspDTO<?> genericRspDTO;
    
    public LemonAuthenticationException(String msgInfo) {
        super(msgInfo);
    }
    
    public LemonAuthenticationException(GenericRspDTO<?> genericRspDTO) {
        super(genericRspDTO.getMsgCd()+" : "+genericRspDTO.getMsgInfo());
        this.genericRspDTO = genericRspDTO;
    }
    
    public GenericRspDTO<?> getGenericRspDTO () {
        return this.genericRspDTO;
    }

}
