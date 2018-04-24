package com.hisun.lemon.gateway.filter;

import java.io.IOException;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.LocaleResolver;

import com.hisun.lemon.common.extension.Inject;
import com.hisun.lemon.common.utils.JudgeUtils;
import com.hisun.lemon.gateway.common.GatewayConstants;
import com.hisun.lemon.gateway.common.GatewayHelper;
import com.hisun.lemon.gateway.common.extension.GatewayTransactionInitializer;

/**
 * @author yuzhou
 * @date 2017年8月15日
 * @time 下午1:00:06
 *
 */
public class GatewayEntryFilter extends OncePerRequestFilter {
    
    private LocaleResolver localeResolver;
    
    @Inject
    private List<GatewayTransactionInitializer> gatewayInitalizers;
    
    public GatewayEntryFilter(LocaleResolver localeResolver) {
        this.localeResolver = localeResolver;
    }
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {
        try {
            GatewayHelper.beforeProcess(request);
            request.setAttribute(GatewayConstants.REQUEST_LEMON_LOCALE_RESOLVER, localeResolver);
            doEntryInit();
            filterChain.doFilter(request, response);
        } finally {
            GatewayHelper.afterProcess();
        }
    }

    private void doEntryInit() {
        if(JudgeUtils.isNotEmpty(gatewayInitalizers)) {
            gatewayInitalizers.stream().forEachOrdered(GatewayTransactionInitializer::init);
        }
    }
    
}
