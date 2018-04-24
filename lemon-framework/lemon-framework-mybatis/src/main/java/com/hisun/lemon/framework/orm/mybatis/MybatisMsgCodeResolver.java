package com.hisun.lemon.framework.orm.mybatis;

import org.mybatis.spring.MyBatisSystemException;

import com.hisun.lemon.common.exception.ErrorMsgCode;
import com.hisun.lemon.common.exception.MsgCodeResolver;

/**
 * mybatis 异常
 * @author yuzhou
 * @date 2017年9月14日
 * @time 下午9:02:34
 *
 */
public class MybatisMsgCodeResolver implements MsgCodeResolver {

    @Override
    public ErrorMsgCode resolve(Throwable throwable) {
        return ErrorMsgCode.ACCESS_DATABASE_ERROR;
    }

    @Override
    public boolean support(Throwable throwable) {
        if(throwable instanceof MyBatisSystemException) {
            return true;
        }
        return false;
    }

}
