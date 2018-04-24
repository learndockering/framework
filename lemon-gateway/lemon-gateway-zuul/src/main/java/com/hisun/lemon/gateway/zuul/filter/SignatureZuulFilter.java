package com.hisun.lemon.gateway.zuul.filter;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hisun.lemon.common.exception.ErrorMsgCode;
import com.hisun.lemon.common.exception.LemonException;
import com.hisun.lemon.common.utils.JudgeUtils;
import com.hisun.lemon.gateway.common.response.ResponseMessageResolver;
import com.hisun.lemon.gateway.common.signature.SignatureResolver;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

/**
 * @author yuzhou
 * @date 2017年8月5日
 * @time 下午3:00:32
 *
 */
public class SignatureZuulFilter extends ZuulFilter {
    private static final Logger logger = LoggerFactory.getLogger(SignatureZuulFilter.class);
    
    public static final int SIGNATURE_ORDER = FilterConstants.ORDER_SIGNATURE;
    
    private SignatureResolver signatureResolver;
    
    private ResponseMessageResolver responseMessageResolver;
    
    public SignatureZuulFilter(SignatureResolver signatureResolver, ResponseMessageResolver responseMessageResolver) {
        this.signatureResolver = signatureResolver;
        this.responseMessageResolver = responseMessageResolver;
    }
    
    @Override
    public boolean shouldFilter() {
        return signatureResolver.shouldVerify();
    }

    @Override
    public Object run() {
        try{
            if (JudgeUtils.not(this.signatureResolver.verify())) {
                RequestContext ctx = RequestContext.getCurrentContext();
                ctx.setSendZuulResponse(false);
                ctx.setResponseBody(this.responseMessageResolver.generateString(ErrorMsgCode.SIGNATURE_EXCEPTION));
                return null;
            }
        } catch (LemonException le) {
            if(logger.isErrorEnabled()) {
                logger.error("Failed to verify sign ", le);
            }
            RequestContext ctx = RequestContext.getCurrentContext();
            ctx.setSendZuulResponse(false);
            ctx.setResponseBody(this.responseMessageResolver.generateString(Optional.ofNullable(le.getMsgCd()).orElse(ErrorMsgCode.SYS_ERROR.getMsgCd())));
        } catch (Exception e) {
            if(logger.isErrorEnabled()) {
                logger.error("Failed to verify sign ", e);
            }
            RequestContext ctx = RequestContext.getCurrentContext();
            ctx.setSendZuulResponse(false);
            ctx.setResponseBody(this.responseMessageResolver.generateString(ErrorMsgCode.SYS_ERROR));
        }
        return null;
    }

    @Override
    public String filterType() {
        return FilterType.PRE.lowerCaseName();
    }

    @Override
    public int filterOrder() {
        return SIGNATURE_ORDER;
    }

}
