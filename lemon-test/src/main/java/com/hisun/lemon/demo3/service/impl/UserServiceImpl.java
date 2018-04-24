package com.hisun.lemon.demo3.service.impl;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.hisun.lemon.common.utils.ResourceUtils;
import com.hisun.lemon.demo3.dao.IUserDao;
import com.hisun.lemon.demo3.entity.UserDO;
import com.hisun.lemon.demo3.service.IUserService;
import com.hisun.lemon.framework.cache.redis.RedisCacheable;

@Transactional
@Service
public class UserServiceImpl implements IUserService {
    @Resource
    private IUserDao userDao;
    
    @Override
    public void addUser(UserDO userDO) {
       /* UserDO userDO2 = new UserDO();
        BeanUtils.copyProperties(userDO2, userDO);
        userDO2.setName(userDO2.getName()+"00");
        //逻辑xxxxx
        this.userDao.insert(userDO2);*/
        this.userDao.insert(userDO);
        //LemonException.throwLemonException();
    }
    
    @RedisCacheable(cacheNames="user")
    @Transactional(propagation= Propagation.SUPPORTS,readOnly=true)
    @Override
    public UserDO findUser(String userId) {
        return this.userDao.findByUserId(userId);
    }
    
    @Override
    @Transactional(propagation= Propagation.SUPPORTS,readOnly=true)
    public List<UserDO> findUser(UserDO userDo) {
        return this.userDao.find(userDo);
    }

    public IUserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(IUserDao userDao) {
        this.userDao = userDao;
    }
    public static void main(String[] args) throws IOException {
        System.out.println(ResourceUtils.getFileContent("lua/IdGen.lua"));
    }
}
