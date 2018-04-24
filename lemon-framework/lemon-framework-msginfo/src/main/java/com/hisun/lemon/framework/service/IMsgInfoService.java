package com.hisun.lemon.framework.service;

import java.util.List;

import com.hisun.lemon.framework.entity.MsgInfoDO;

/**
 * 消息码服务接口
 * @author yuzhou
 * @date 2017年6月27日
 * @time 下午2:04:16
 *
 */
public interface IMsgInfoService {
    /**
     * 根据消息码和语言获取消息码信息
     * @param msgCd
     * @param language
     * @return
     */
    public List<MsgInfoDO> findMsgInfos(String msgCd, String language);
}
