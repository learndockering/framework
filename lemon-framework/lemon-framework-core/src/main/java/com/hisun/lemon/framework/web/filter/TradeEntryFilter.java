package com.hisun.lemon.framework.web.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;

import com.hisun.lemon.common.context.LemonContext;
import com.hisun.lemon.common.utils.JudgeUtils;
import com.hisun.lemon.framework.data.LemonHolder;
import com.hisun.lemon.framework.log.logback.MDCUtil;
import com.hisun.lemon.framework.utils.WebUtils;

/**
 * @author yuzhou
 * @date 2017年8月15日
 * @time 下午1:00:06
 *
 */
public class TradeEntryFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String requestId = WebUtils.resolveRequestId(request, false);
            if(JudgeUtils.isNotNull(requestId)) {
                MDCUtil.putMDCKey(requestId);
                LemonContext.setAlreadyPutMDC();
            }
            filterChain.doFilter(request, response);
        } finally {
            LemonHolder.clear();
            LemonContext.clearCurrentContext();
            MDCUtil.removeMDCKey();
        }
    }
    
}
