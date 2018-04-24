package com.hisun.lemon.framework.lock;

import com.hisun.lemon.common.exception.ErrorMsgCode;

/**
 * 不能获取锁
 * @author yuzhou
 * @date 2017年7月19日
 * @time 下午7:31:39
 *
 */
public class UnableToAquireLockException extends Exception {

    private static final long serialVersionUID = 4121125716772813963L;
    public UnableToAquireLockException() {
        super(ErrorMsgCode.UNABLE_ACQUIRE_DISTRUBUTED_LOCK.getMsgCd());
    }

    public UnableToAquireLockException(String message, Throwable cause) {
        super(ErrorMsgCode.UNABLE_ACQUIRE_DISTRUBUTED_LOCK.getMsgCd(), cause);
    }
}
