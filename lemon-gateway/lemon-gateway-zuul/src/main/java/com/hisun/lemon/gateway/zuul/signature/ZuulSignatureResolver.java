package com.hisun.lemon.gateway.zuul.signature;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import com.hisun.lemon.common.context.LemonContext;
import com.hisun.lemon.common.utils.JudgeUtils;
import com.hisun.lemon.gateway.bo.SecurityBO;
import com.hisun.lemon.gateway.common.GatewayConstants;
import com.hisun.lemon.gateway.common.GatewayHelper;
import com.hisun.lemon.gateway.common.config.ZuulExtensionProperties.ZuulRoute;
import com.hisun.lemon.gateway.zuul.ZuulHelper;
import com.netflix.zuul.context.RequestContext;

/**
 * @author yuzhou
 * @date 2017年8月5日
 * @time 下午3:40:09
 *
 */
public class ZuulSignatureResolver extends AbstractZuulSignatureResolver{

    public static final String REQUEST_HEADER_SECURE_INDEX = GatewayHelper.REQUEST_HEADER_SECURE_INDEX;
    
    public ZuulSignatureResolver(ZuulHelper zuulHelper){
        super(zuulHelper);
    }
    
    @Override
    public boolean doShouldVerify() {
        ZuulRoute zuulRoute = this.zuulHelper.getCurrentZuulRoute();
        return JudgeUtils.isTrue(zuulRoute.getSignatured(), true);
    }
    
    @Override
    public boolean verify() {
        try {
            return doVerify();
        } finally {
            if(LemonContext.getCurrentContext().getBooleanOrDefault(GatewayConstants.REQUIRE_CLEAN_ZUUL_CONTEXT, Boolean.FALSE)) {
                RequestContext.getCurrentContext().clear();
            }
        }
    }
    
    public boolean doVerify() {
        HttpServletRequest request  = Optional.ofNullable(RequestContext.getCurrentContext().getRequest()).orElseGet(() -> GatewayHelper.getHttpServletRequest());
        String verifyMsg = getVerifyString(request);//待签名
        if(JudgeUtils.isBlank(verifyMsg)) {
            if(logger.isErrorEnabled()) {
                logger.error("Failed to verify because of waiting verify string is blank, request uri \"{}\".", request.getRequestURI());
            }
            return false;
        }
        
        String secureIndex = request.getHeader(REQUEST_HEADER_SECURE_INDEX);
        if(JudgeUtils.isBlank(secureIndex)) {
            if(logger.isErrorEnabled()) {
                logger.error("Failed to verify because of secure key does not exist in http header \"{}\", request uri \"{}\". ", REQUEST_HEADER_SECURE_INDEX, request.getRequestURI());
            }
            return false;
        }
        
        String signedMsg = getSignedString(request);
        if(JudgeUtils.isBlank(signedMsg)) {
            if(logger.isErrorEnabled()) {
                logger.error("Failed to verify because of signed string does not exist in http header \"{}\", request uri \"{}\". ", REQUEST_HEADER_SIGN, request.getRequestURI());
            }
            return false;
        }
        
        SecurityBO secureBO = this.zuulHelper.findSecurityBO(secureIndex);
        if(JudgeUtils.isNull(secureBO)) {
            if(logger.isErrorEnabled()) {
                logger.error("Failed to verify because of secure is null with secure index \""+secureIndex+"\"");
            }
            return false;
        }
        return doVerify(secureBO.getAlgorithm(), secureBO.getSecureKey(), verifyMsg, signedMsg);
    }

}
