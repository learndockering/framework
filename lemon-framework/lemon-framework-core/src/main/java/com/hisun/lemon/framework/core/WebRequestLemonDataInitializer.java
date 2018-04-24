package com.hisun.lemon.framework.core;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hisun.lemon.common.LemonConstants;
import com.hisun.lemon.common.utils.DateTimeUtils;
import com.hisun.lemon.framework.data.LemonData;
import com.hisun.lemon.framework.data.LemonHolder;
import com.hisun.lemon.framework.utils.IdGenUtils;
import com.hisun.lemon.framework.utils.LemonUtils;
import com.hisun.lemon.framework.utils.WebUtils;

/**
 * @author yuzhou
 * @date 2017年9月26日
 * @time 下午5:59:19
 *
 */
public class WebRequestLemonDataInitializer implements LemonDataInitializer {
    private static final Logger logger = LoggerFactory.getLogger(WebRequestLemonDataInitializer.class);
    
    @Override
    public void initLemonData() {
        initLemonData(WebUtils.getHttpServletRequest());
    }

    private LemonData initLemonData(HttpServletRequest request) {
        LemonData lemonData = LemonHolder.getLemonData();
        lemonData.setRequestId(WebUtils.resolveRequestId(request));
        lemonData.setMsgId(IdGenUtils.generateMsgId());
        lemonData.setAccDate(DateTimeUtils.getCurrentLocalDate());
        lemonData.setLocale(WebUtils.resolveLocale(request));
        lemonData.setStartDateTime(DateTimeUtils.getCurrentLocalDateTime());
        lemonData.setRouteInfo(LemonUtils.getApplicationName());
        lemonData.setUserId(request.getHeader(LemonConstants.HTTP_HEADER_USERID));
        lemonData.setChannel(request.getHeader(LemonConstants.HTTP_HEADER_CHANNL));
        lemonData.setClientIp(WebUtils.resolveClientIP(request));
        lemonData.setSource(request.getHeader(LemonConstants.HTTP_HEADER_SOURCE));
        lemonData.setBusiness(request.getHeader(LemonConstants.HTTP_HEADER_BUSINESS));
        lemonData.setUri(request.getHeader(LemonConstants.HTTP_HEADER_URI));
        lemonData.setToken(request.getHeader(LemonConstants.HTTP_HEADER_TOKEN));
        lemonData.setLoginName(request.getHeader(LemonConstants.HTTP_HEADER_LOGINNAME));
        if(logger.isDebugEnabled()) {
            logger.debug("init lemon data {}", lemonData);
        }
        return lemonData;
    }
}
