package com.hisun.lemon.gateway.merchant.signature;

import javax.servlet.http.HttpServletRequest;

import com.hisun.lemon.common.utils.JudgeUtils;
import com.hisun.lemon.gateway.bo.SecurityBO;
import com.hisun.lemon.gateway.common.config.ZuulExtensionProperties.ZuulRoute;
import com.hisun.lemon.gateway.zuul.ZuulHelper;
import com.hisun.lemon.gateway.zuul.signature.AbstractZuulSignatureResolver;



/**
 * 商户接入签名
 * @author yuzhou
 * @date 2017年10月11日
 * @time 下午4:05:01
 *
 */
public class MerchantSignatureResolver extends AbstractZuulSignatureResolver {
    public static final String MERCHANT_NO_HEADER = "x-mer-no";
    public static final String MERCHANT_INTERFACE_HEADER = "x-mer-itf";
    public static final String MERCHANT_INTERFACE_VERSION_HEADER = "x-mer-itfver";

    public MerchantSignatureResolver(ZuulHelper zuulHelper) {
        super(zuulHelper);
    }

    @Override
    public boolean doShouldVerify() {
        ZuulRoute zuulRoute = this.zuulHelper.getCurrentZuulRoute();
        return JudgeUtils.isTrue(zuulRoute.getMercSignatured(), false);
    }

    @Override
    public boolean verify() {
        HttpServletRequest request  = this.zuulHelper.getHttpServletRequest();
        if(JudgeUtils.isBlankAny(request.getHeader(MERCHANT_NO_HEADER), request.getHeader(MERCHANT_INTERFACE_HEADER), 
            request.getHeader(MERCHANT_INTERFACE_VERSION_HEADER))) {
            if(logger.isWarnEnabled()) {
                logger.warn("Failed to verify because of \"merchant no\" or \"merchant interface name\" or \"interface version\" empty. request uri \"{}\" ", request.getRequestURI());
            }
            return false;
        }
        String verifyMsg = getVerifyString(request);//待签名
        if(JudgeUtils.isBlank(verifyMsg)) {
            if(logger.isDebugEnabled()) {
                logger.debug("Failed to verify because waiting verify data is blank, request uri \"{}\".", request.getRequestURI());
            }
            return false;
        }
            
        String signedMsg = getSignedString(request);
        if(JudgeUtils.isBlank(signedMsg)) {
            if(logger.isDebugEnabled()) {
                logger.debug("Failed to verify because signed data does not exist in http header \"{}\", request uri \"{}\". ", REQUEST_HEADER_SIGN, request.getRequestURI());
            }
            return false;
        }
        
        SecurityBO secureBO = this.zuulHelper.findMerchantSecurityBO(request.getHeader(MERCHANT_NO_HEADER), 
            request.getHeader(MERCHANT_INTERFACE_HEADER), request.getHeader(MERCHANT_INTERFACE_VERSION_HEADER));
        if(JudgeUtils.isNull(secureBO)) {
            if(logger.isWarnEnabled()) {
                logger.warn("Failed to verify because merchant securityBO could not found with merchat no \"{}\" and interface name \"{}\" and interface version \"{}\".",
                    request.getHeader(MERCHANT_NO_HEADER), request.getHeader(MERCHANT_INTERFACE_HEADER), request.getHeader(MERCHANT_INTERFACE_VERSION_HEADER));
            }
            return false;
        }
        return doVerify(secureBO.getAlgorithm(), secureBO.getSecureKey(), verifyMsg, signedMsg);
        
    }

}
