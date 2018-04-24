package com.hisun.lemon.gateway.service;

import com.hisun.lemon.framework.data.GenericRspDTO;
import com.hisun.lemon.gateway.bo.UserInfoBase;

/**
 * @author yuzhou
 * @date 2017年9月13日
 * @time 下午5:53:10
 *
 */
public interface IUserService {
    GenericRspDTO<? extends UserInfoBase> queryUserInfoByLoginId(String loginId);
}
