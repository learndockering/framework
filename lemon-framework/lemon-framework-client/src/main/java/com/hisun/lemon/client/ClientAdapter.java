package com.hisun.lemon.client;

import com.hisun.lemon.common.exception.ErrorMsgCode;
import com.hisun.lemon.common.exception.LemonException;

/**
 * 客户端适配器
 * @author yuzhou
 * @date 2017年7月28日
 * @time 上午9:33:44
 *
 */
public abstract class ClientAdapter implements Client {

    @Override
    public void send(byte[] bytes) {
        throw new UnsupportClientMethodException(this.getClass().getName(), "send");
    }
    
    @Override
    public void send(Object object) {
        throw new UnsupportClientMethodException(this.getClass().getName(), "send");
    }
    
    public static class UnsupportClientMethodException extends LemonException {
        private static final long serialVersionUID = 1L;

        public UnsupportClientMethodException(String className, String methodName) {
            super(ErrorMsgCode.SYS_ERROR.getMsgCd(), "The method \""+className+"."+methodName + "\" is not support.");
        }
    }

}
