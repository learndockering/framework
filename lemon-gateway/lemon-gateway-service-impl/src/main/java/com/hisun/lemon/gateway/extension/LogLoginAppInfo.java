package com.hisun.lemon.gateway.extension;

import javax.servlet.http.HttpServletRequest;

import com.hisun.lemon.common.extension.ExtensionLoader;
import com.hisun.lemon.common.extension.SPIService;
import com.hisun.lemon.gateway.common.GatewayHelper;
import com.hisun.lemon.gateway.common.extension.LoginSuccessProcessor;
import com.hisun.lemon.gateway.dto.AppInfoDTO;
import com.hisun.lemon.gateway.security.SecurityUtils;
import com.hisun.lemon.gateway.service.IAppInfoService;

/**
 * @author yuzhou
 * @date 2017年9月26日
 * @time 下午2:40:35
 *
 */
@SPIService("logLoginAppInfo")
public class LogLoginAppInfo implements LoginSuccessProcessor {

    private IAppInfoService appInfoService = ExtensionLoader.getSpringBean(IAppInfoService.class);
    
    @Override
    public void process() {
        HttpServletRequest request = GatewayHelper.getHttpServletRequest();
        AppInfoDTO appInfoBO = new AppInfoDTO();
        appInfoBO.setDownloadChannel(request.getHeader(AppInfoHeader.HEADER_APP_DOWNLOAD_CHANNEL));
        appInfoBO.setOsVersion(request.getHeader(AppInfoHeader.HEADER_APP_OS_VERSION));
        appInfoBO.setTermId(request.getHeader(AppInfoHeader.HEADER_APP_TERM_ID));
        appInfoBO.setTermType(request.getHeader(AppInfoHeader.HEADER_APP_TERM_TYPE));
        appInfoBO.setUserType(request.getHeader(AppInfoHeader.HEADER_APP_USER_TYPE));
        appInfoBO.setVersion(request.getHeader(AppInfoHeader.HEADER_APP_APP_VERSION));
        appInfoBO.setUserId(SecurityUtils.getLoginUserId());
        appInfoService.logAppInfo(appInfoBO);
    }

}
