package com.hisun.lemon.gateway.zuul.filter;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hisun.lemon.common.Callback;
import com.hisun.lemon.gateway.common.GatewayAccessLogger;
import com.hisun.lemon.gateway.common.response.ResponseMessageResolver;
import com.hisun.lemon.gateway.common.signature.SignatureResolver;
import com.hisun.lemon.gateway.security.SecurityUtils;
import com.hisun.lemon.gateway.zuul.ZuulHelper;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

/**
 * zuul fillter configuration
 * @author yuzhou
 * @date 2017年8月5日
 * @time 下午12:51:28
 *
 */
@Configuration
public class ZuulFilterConfiguration {
    
    @Bean
    public ZuulFilter requestLogZuulFilter(ZuulHelper zuulHelper) {
        return createZuulFilter(true, FilterType.PRE.lowerCaseName(), FilterConstants.ORDER_REQUEST_LOG, 
            () -> {
                HttpServletRequest request = RequestContext.getCurrentContext().getRequest();
                GatewayAccessLogger.printRequestLog(request, SecurityUtils.getLoginUserId(), zuulHelper.getParameterMap(request), zuulHelper.getCurrentRequestBody());
                return null;
            });
    }
    
    @Bean
    public ZuulFilter signatureZuulFilter(SignatureResolver signatureResolver, ResponseMessageResolver responseMessageResolver) {
        return new SignatureZuulFilter(signatureResolver, responseMessageResolver);
    }
    
    @Bean
    public ZuulFilter lemonPreZuulFilter(ResponseMessageResolver responseMessageResolver, ZuulHelper zuulHelper) {
        return new LemonPreZuulFilter(responseMessageResolver, zuulHelper);
    }
    
    @Bean
    public ZuulFilter responseLogZuulFilter() {
        return createZuulFilter(() -> {return ! GatewayAccessLogger.alreadyPrintResponseLog(RequestContext.getCurrentContext().getRequest()) && RequestContext.getCurrentContext().getThrowable() == null;}, 
            FilterType.POST.lowerCaseName(), FilterConstants.ORDER_POST_LOG, 
            () -> {
                RequestContext ctx = RequestContext.getCurrentContext();
                if(ctx.getThrowable() == null) {
                    GatewayAccessLogger.printResponseLog(ctx.getRequest(), ctx.getResponse(), ctx.getResponseBody());
                }
                return null;
            });
    }
    
    @Bean
    public ZuulFilter errorLogZuulFilter(ObjectMapper objectMapper) {
        return createZuulFilter(() -> {return ! GatewayAccessLogger.alreadyPrintResponseLog(RequestContext.getCurrentContext().getRequest()) && RequestContext.getCurrentContext().getThrowable() != null;}, 
            FilterType.ERROR.lowerCaseName(), FilterConstants.ORDER_ERROR_LOG, 
            () -> {
                RequestContext ctx = RequestContext.getCurrentContext();
                GatewayAccessLogger.printResponseLog(ctx.getRequest(), ctx.getResponse(), ctx.getResponseBody());
                return null;
            });
    }
    
    private ZuulFilter createZuulFilter(boolean shouldFilter, String filterType, int filterOrder, Callback<Object> callback) {
        return createZuulFilter(() -> {return shouldFilter;}, filterType, filterOrder, callback);
    }
    
    private ZuulFilter createZuulFilter(Callback<Boolean> shouldFilterCallback, String filterType, int filterOrder, Callback<Object> callback) {
        return new ZuulFilter(){
            @Override
            public boolean shouldFilter() {
                return shouldFilterCallback.callback();
            }

            @Override
            public Object run() {
                return callback.callback();
            }

            @Override
            public String filterType() {
                return filterType;
            }

            @Override
            public int filterOrder() {
                return filterOrder;
            }
        };
    }
    
}
