package com.hisun.lemon.gateway.core;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import com.hisun.lemon.common.utils.DateTimeUtils;
import com.hisun.lemon.framework.data.LemonData;
import com.hisun.lemon.framework.data.LemonHolder;
import com.hisun.lemon.framework.utils.LemonUtils;
import com.hisun.lemon.gateway.common.GatewayHelper;
import com.hisun.lemon.gateway.common.extension.GatewayTransactionInitializer;

/**
 * @author yuzhou
 * @date 2017年9月22日
 * @time 下午5:46:31
 *
 */
public class LemonDataInitializer implements GatewayTransactionInitializer {
    @Override
    public void init() {
        LemonData lemonData = LemonHolder.getLemonData();
        HttpServletRequest request = GatewayHelper.getHttpServletRequest();
        lemonData.setRequestId(GatewayHelper.getRequestId(request));
        lemonData.setLocale(GatewayHelper.resolveLocale(request));
        lemonData.setStartDateTime(DateTimeUtils.getCurrentLocalDateTime());
        lemonData.setClientIp(Optional.ofNullable(GatewayHelper.xforwardedFor()).map(i -> i.split(",")).map(a -> StringUtils.trim(a[0])).orElseGet( () -> request.getRemoteAddr() ));
        lemonData.setSource(LemonUtils.getApplicationName());
        lemonData.setToken(GatewayHelper.getToken(request));
    }

}
