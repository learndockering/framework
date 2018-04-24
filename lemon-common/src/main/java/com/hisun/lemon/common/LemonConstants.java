package com.hisun.lemon.common;

/**
 * @author yuzhou
 * @date 2017年8月8日
 * @time 下午5:13:33
 *
 */
public interface LemonConstants {
    //以下头信息是传送给Controller
    String HTTP_HEADER_CLIENTIP = "x-lemon-clientip";
    String HTTP_HEADER_LOCALE = "x-lemon-locale";
    String HTTP_HEADER_USERID = "x-lemon-usrid";
    String HTTP_HEADER_REQUESTID = "x-lemon-reqid";
    String HTTP_HEADER_CHANNL = "x-lemon-channel";
    String HTTP_HEADER_FOR = "X-Forwarded-For";
    String HTTP_HEADER_SOURCE = "x-lemon-source";
    String HTTP_HEADER_URI = "x-lemon-uri";
    String HTTP_HEADER_BUSINESS = "x-lemon-business";
    String HTTP_HEADER_TOKEN = "x-auth-token";
    String HTTP_HEADER_LOGINNAME = "x-lemon-loginnm";
    
    //senstiveParameterNames, senstiveHeaderNames 不能由前端输入，否则抛出非法参数（网关检查）
    String[] SENSTIVE_PARAMETER_NAMES = new String[]{"userId", "routeInfo", "requestId", "msgId", "loginName"};
    String[] SENSTIVE_HEADER_NAMES = new String[]{HTTP_HEADER_USERID, HTTP_HEADER_REQUESTID, HTTP_HEADER_CLIENTIP, HTTP_HEADER_SOURCE, HTTP_HEADER_BUSINESS, HTTP_HEADER_URI, HTTP_HEADER_LOGINNAME};
    
    String DEFAULT_PROJECT_PATH_PREFIX = "com.hisun.";
    
    String CURRENT_ENV = "spring.profiles.active";
    String LEMON_HOME = "lemon.home";
    String APPLICATION_NAME = "spring.application.name";
    
    //通用常量
    String PROPERTY_IGNORE_START_CHAR = "#";
    String PROPERTY_KEY_VALUE_SEPARATOR = "=";
}
