package com.hisun.lemon.framework.data;

import com.hisun.lemon.common.utils.JudgeUtils;

/**
 * @author yuzhou
 * @date 2017年8月16日
 * @time 下午9:30:32
 *
 */
public interface GenericDTOFields {
    
    String REQUEST_ID = "requestId";
    String MSG_ID = "msgId";
    String ACC_DATE = "accDate";
    String START_DATE_TIME = "startDateTime";
    String LOCALE ="locale";
    String ROUTE_INFO = "routeInfo";
    String USER_ID = "userId";
    String CLIENT_IP = "clientIp";
    String SOURCE = "source";
    String CHANNEL = "channel";
    String BUSINESS = "business";
    String URI = "uri";
    String TOKEN = "token";
    String LOGIN_NAME = "loginName";
    
    String[] LEMON_FIELDS = new String[]{REQUEST_ID, MSG_ID, ACC_DATE, START_DATE_TIME, LOCALE, ROUTE_INFO, USER_ID, CLIENT_IP, SOURCE, CHANNEL, BUSINESS, URI, TOKEN, LOGIN_NAME};
    
    static boolean isGenericDTOField(String fieldName) {
        boolean flag = false;
        for(String n : LEMON_FIELDS) {
            if(JudgeUtils.equals(n, fieldName)) {
                flag = true;
                break;
            }
        }
        return flag;
    }
}
