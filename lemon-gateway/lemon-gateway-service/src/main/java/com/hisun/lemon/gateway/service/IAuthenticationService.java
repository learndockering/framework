package com.hisun.lemon.gateway.service;

import com.hisun.lemon.framework.data.GenericRspDTO;
import com.hisun.lemon.gateway.bo.AuthenticationBO;
import com.hisun.lemon.gateway.bo.UserInfoBase;

/**
 * 用户认证接口
 * 
 * @author yuzhou
 * @date 2017年7月25日
 * @time 下午1:50:06
 *
 */
public interface IAuthenticationService {
    
    /**
     * 登录
     * @param loginReqDTO
     * @return
     */
    GenericRspDTO<? extends UserInfoBase> authentication(AuthenticationBO loginReqDTO);
    
    
}
