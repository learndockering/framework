package com.hisun.lemon.gateway.zuul.filter;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import com.hisun.lemon.common.LemonConstants;
import com.hisun.lemon.common.exception.ErrorMsgCode;
import com.hisun.lemon.common.utils.JudgeUtils;
import com.hisun.lemon.framework.utils.LemonUtils;
import com.hisun.lemon.gateway.bo.SecurityBO;
import com.hisun.lemon.gateway.common.GatewayHelper;
import com.hisun.lemon.gateway.common.response.ResponseMessageResolver;
import com.hisun.lemon.gateway.security.SecurityUtils;
import com.hisun.lemon.gateway.zuul.ZuulHelper;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

/**
 * @author yuzhou
 * @date 2017年8月7日
 * @time 下午7:58:09
 *
 */
public class LemonPreZuulFilter extends ZuulFilter {
    private ResponseMessageResolver responseMessageResolver;
    private ZuulHelper zuulHelper;
    
    public LemonPreZuulFilter(ResponseMessageResolver responseMessageResolver, ZuulHelper zuulHelper) {
        this.responseMessageResolver = responseMessageResolver;
        this.zuulHelper = zuulHelper;
    }
    
    @Override
    public boolean shouldFilter() {
        return RequestContext.getCurrentContext().sendZuulResponse();
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        if(JudgeUtils.not(this.zuulHelper.validateInputParams(request))) {
            ctx.setSendZuulResponse(false);
            ctx.setResponseBody(this.responseMessageResolver.generateString(ErrorMsgCode.ILLEGAL_PARAMETER));
            return null;
        }
        Locale locale = GatewayHelper.resolveLocale(request);
        ctx.addZuulRequestHeader(LemonConstants.HTTP_HEADER_LOCALE, locale.toString());
        ctx.addZuulRequestHeader(LemonConstants.HTTP_HEADER_USERID, SecurityUtils.getLoginUserId());
        ctx.addZuulRequestHeader(LemonConstants.HTTP_HEADER_LOGINNAME, SecurityUtils.getLoginName());
        SecurityBO secureBO = this.zuulHelper.findSecurityBO(request);
        if (JudgeUtils.isNotNull(secureBO)) {
            ctx.addZuulRequestHeader(LemonConstants.HTTP_HEADER_CHANNL, secureBO.getChannel());
        } else {
            ctx.addZuulRequestHeader(LemonConstants.HTTP_HEADER_CHANNL, request.getHeader(LemonConstants.HTTP_HEADER_CHANNL));
        }
        ctx.addZuulRequestHeader(LemonConstants.HTTP_HEADER_SOURCE, LemonUtils.getApplicationName());
        ctx.addZuulRequestHeader(LemonConstants.HTTP_HEADER_REQUESTID, GatewayHelper.getRequestId(request));
        ctx.addZuulRequestHeader(LemonConstants.HTTP_HEADER_BUSINESS, this.zuulHelper.getCurrentZuulRoute().getId());
        ctx.addZuulRequestHeader(LemonConstants.HTTP_HEADER_URI, this.zuulHelper.getCurrentRequestURI());
        return null;
    }

    @Override
    public String filterType() {
        return FilterType.PRE.lowerCaseName();
    }

    @Override
    public int filterOrder() {
        return FilterConstants.ORDER_LEMON_PRE;
    }

}
