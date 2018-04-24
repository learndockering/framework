package com.hisun.lemon.demo3.service;

import java.util.List;

import com.hisun.lemon.demo3.entity.UserDO;


/**
 * @author yuzhou
 * @date 2017年6月7日
 * @time 下午2:13:58
 *
 */
public interface IUserService {
    public void addUser(UserDO userDO);
    
    public UserDO findUser(String id);
    
    public List<UserDO> findUser(UserDO userDo);
}
