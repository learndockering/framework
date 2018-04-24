package com.hisun.lemon.gateway.service.impl;

import com.hisun.lemon.common.utils.BeanUtils;
import com.hisun.lemon.common.utils.JudgeUtils;
import com.hisun.lemon.framework.data.DataHelper;
import com.hisun.lemon.framework.data.GenericDTO;
import com.hisun.lemon.framework.data.GenericRspDTO;
import com.hisun.lemon.gateway.bo.AuthenticationBO;
import com.hisun.lemon.gateway.bo.UserBasicInfoBO;
import com.hisun.lemon.gateway.bo.UserInfoBase;
import com.hisun.lemon.gateway.service.IAuthenticationService;

/**
 * 
 * 认证服务实现
 * @author yuzhou
 * @date 2017年7月27日
 * @time 上午10:53:07
 *
 */
public class AuthenticationServiceImpl implements IAuthenticationService {


    public AuthenticationServiceImpl(){}

    /* 
     * 认证
     * @see com.hisun.lemon.gateway.service.IAuthenticationService#authentication(com.hisun.lemon.gateway.dto.LoginReqDTO)
     */
    @Override
    public GenericRspDTO<? extends UserInfoBase> authentication(AuthenticationBO authenticationBo) {
        return null;
    }

}
