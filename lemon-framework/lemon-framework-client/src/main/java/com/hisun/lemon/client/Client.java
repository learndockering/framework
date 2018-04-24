package com.hisun.lemon.client;

/**
 * 客户端
 * @author yuzhou
 * @date 2017年7月28日
 * @time 上午9:24:37
 *
 */
public interface Client {
    public void send(byte[] bytes);
    
    public void send(Object object);
}
