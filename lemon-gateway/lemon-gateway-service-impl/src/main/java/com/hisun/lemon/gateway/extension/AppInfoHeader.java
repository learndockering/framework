package com.hisun.lemon.gateway.extension;

/**
 * @author yuzhou
 * @date 2017年8月14日
 * @time 上午11:14:07
 *
 */
public interface AppInfoHeader {
    
    String HEADER_APP_APP_VERSION = "x-lemon-appver";
    String HEADER_APP_DOWNLOAD_CHANNEL = "x-lemon-downcnl";
    String HEADER_APP_OS_VERSION = "x-lemon-osver";
    String HEADER_APP_TERM_ID = "x-lemon-termid";
    String HEADER_APP_TERM_TYPE = "x-lemon-termtyp";
    String HEADER_APP_USER_TYPE = "x-lemon-usrtyp";
}
