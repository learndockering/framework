package com.hisun.lemon.framework.service;

import com.hisun.lemon.framework.data.GenericRspDTO;

/**
 * @author yuzhou
 * @date 2017年9月5日
 * @time 下午4:14:55
 *
 */
public interface IMsgInfoProcessor {
    <T> GenericRspDTO<T> processMsgInfo(GenericRspDTO<T> genericRspDTO);
}
