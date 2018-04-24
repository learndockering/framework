package com.hisun.lemon.common.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 网络相关
 * @author yuzhou
 * @date 2017年9月8日
 * @time 下午2:48:17
 *
 */
public class NetUtils {
    public static final String DEFALUT_LOCALE_IP = "127.0.0.1";
    /**
     * 获取本机Ip
     * @return
     */
    public static String getLocalHostAddress() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            //TODO 暂时不保证正确
            return DEFALUT_LOCALE_IP;
        }
    }
}
