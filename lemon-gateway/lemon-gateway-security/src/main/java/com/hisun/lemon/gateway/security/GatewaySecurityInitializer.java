package com.hisun.lemon.gateway.security;

import java.util.Optional;

import com.hisun.lemon.common.extension.ExtensionLoader;
import com.hisun.lemon.common.utils.JudgeUtils;
import com.hisun.lemon.framework.data.LemonData;
import com.hisun.lemon.framework.data.LemonHolder;
import com.hisun.lemon.gateway.bo.SecurityBO;
import com.hisun.lemon.gateway.common.GatewayHelper;
import com.hisun.lemon.gateway.common.extension.GatewayTransactionInitializer;
import com.hisun.lemon.gateway.service.ISecurityService;

/**
 * SPI
 * @author yuzhou
 * @date 2017年9月22日
 * @time 下午5:17:27
 *
 */
public class GatewaySecurityInitializer implements GatewayTransactionInitializer {
    public static final String SECURITY_SERVICE_BEAN_NAME = "decoratorSecurityService";
    private ISecurityService securityServiceImpl = ExtensionLoader.getSpringBean(SECURITY_SERVICE_BEAN_NAME, ISecurityService.class);
    
    @Override
    public void init() {
        LemonData lemonData = LemonHolder.getLemonData();
        String secureIndex = GatewayHelper.getSecureIndex();
        if(JudgeUtils.isNotBlank(secureIndex)) {
            lemonData.setChannel(Optional.ofNullable(this.securityServiceImpl.querySecurity(secureIndex)).map(SecurityBO::getChannel).orElse(null));
        }
    }

}
