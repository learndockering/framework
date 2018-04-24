package com.hisun.lemon.framework.stream;

import com.hisun.lemon.framework.data.GenericCmdDTO;

/**
 * 消息handler接口
 * @author yuzhou
 * @date 2017年8月10日
 * @time 下午4:12:10
 *
 */
public interface MessageHandler<T> {
    void onMessageReceive(GenericCmdDTO<T> genericCmdDTO);
    
}
