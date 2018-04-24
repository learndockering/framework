package com.hisun.lemon.gateway.security;


/**
 * 与登录用户剔除机制有关
 * 分商户和用户,UI,其他
 * @author yuzhou
 * @date 2017年9月15日
 * @time 下午4:40:48
 *
 */
public enum Channel {
    MER, USR, UI, OTHER;
    
    public static Channel parse(String channelName) {
        String toUpperChannelName = channelName.toUpperCase();
        if(toUpperChannelName.startsWith(MER.toString())) {
            return MER;
        }
        if(toUpperChannelName.startsWith(USR.toString())) {
            return USR;
        }
        if(toUpperChannelName.startsWith(UI.toString())) {
            return UI;
        }
        return OTHER;
    }
}
