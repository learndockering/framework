package com.hisun.lemon.gateway.extension;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import com.hisun.lemon.common.extension.Activate;
import com.hisun.lemon.common.extension.ExtensionLoader;
import com.hisun.lemon.common.utils.JudgeUtils;
import com.hisun.lemon.gateway.common.GatewayHelper;
import com.hisun.lemon.gateway.common.extension.ChannelFetcher;
import com.hisun.lemon.gateway.service.ISecurityService;

@Activate
public class DefaultChannelFetcher implements ChannelFetcher {
    public static final String IGNORE_SET_CHANNEL = GatewayHelper.IGNORE_SET_CHANNEL;
    
    private ISecurityService securityServiceImpl = ExtensionLoader.getSpringBean(ISecurityService.class);
    
    @Override
    public String fetchChannel() {
        HttpServletRequest request = GatewayHelper.getHttpServletRequest();
        if(isIgnoreSetChannel(request)) {    //防止死循环
            return null;
        }
        return Optional.ofNullable(request).map(GatewayHelper::getSecureIndex).map(securityServiceImpl::querySecurity).map(s -> s.getChannel()).orElse(null);
    }
    
    public boolean isIgnoreSetChannel(HttpServletRequest httpRequest) {
        if(JudgeUtils.isNotNull(Optional.ofNullable(httpRequest).map(r -> r.getAttribute(IGNORE_SET_CHANNEL)).orElse(null))) {
            return true;
        }
        return false;
    }

}
