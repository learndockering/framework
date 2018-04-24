package com.hisun.lemon.common.exception;

/**
 * SPI
 * @author yuzhou
 * @date 2017年9月14日
 * @time 下午8:29:20
 *
 */
public interface MsgCodeResolver {
    boolean support(Throwable throwable);
    ErrorMsgCode resolve(Throwable throwable);
}
