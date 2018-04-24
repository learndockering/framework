package com.hisun.lemon.framework.actuator.filter;

import java.io.IOException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hisun.lemon.common.exception.ErrorMsgCode;
import com.hisun.lemon.common.exception.LemonException;
import com.hisun.lemon.common.utils.JudgeUtils;

/**
 * 优雅关机过滤器
 * @author yuzhou
 * @date 2017年9月21日
 * @time 下午3:00:11
 *
 */
public class ShutdownFilter implements Filter{
    private static final Logger logger = LoggerFactory.getLogger(ShutdownFilter.class);
    
    private static final String ALREADY_FILTERED_SUFFIX = ".FILTERED";
    public static final String SUPPORT_SHUTDOWN_IPS = "SUPPORT_SHUTDOWN_IPS";
    public static final String THROW_EXCEPTION_WHEN_FORBIDDEN_OPERATION = "THROW_EXCEPTION_WHEN_FORBIDDEN_OPERATION";
    public static final String SEPARATOR_FOR_IPS = ",";
    private String alreadyFilteredAttributeName = getClass().getName().concat(ALREADY_FILTERED_SUFFIX);
    
    private String[] supportShutdownIps = null;
    private boolean throwExceptionWhenForbiddenOpertation = false;
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        String throwExceptionStr = filterConfig.getInitParameter(THROW_EXCEPTION_WHEN_FORBIDDEN_OPERATION);
        if(JudgeUtils.isNotBlank(throwExceptionStr)) {
            this.throwExceptionWhenForbiddenOpertation = true;
        }
        String ipStr = filterConfig.getInitParameter(SUPPORT_SHUTDOWN_IPS);
        if (ipStr != null && ipStr.length() > 0) {
            this.supportShutdownIps = ipStr.split(SEPARATOR_FOR_IPS);
        } else {
            this.supportShutdownIps = new String[2];
            this.supportShutdownIps[0] = "localhost";
            this.supportShutdownIps[1] = "127.0.0.1";
        }
        if(logger.isInfoEnabled()) {
            String ips = Stream.of(this.supportShutdownIps).collect(Collectors.joining(SEPARATOR_FOR_IPS));
            logger.info("system shutdown operation support ip {}", ips);
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        if(request.getAttribute(this.alreadyFilteredAttributeName) == null) {
            request.setAttribute(alreadyFilteredAttributeName, Boolean.TRUE);
            if(logger.isInfoEnabled()) {
                logger.info("Receive a shutdown request. {}", request.getRemoteAddr());
            }
            if (JudgeUtils.not(doShutdownCheck(request))) {
                if (this.throwExceptionWhenForbiddenOpertation) {
                    LemonException.throwLemonException(ErrorMsgCode.FORBIDDEN_OPERATION.getMsgCd());
                } else {
                    response.getWriter().write(errorMsg());
                }
            } else {
                chain.doFilter(request, response);
            }
        }
    }
    
    private String errorMsg() {
        StringBuilder msg = new StringBuilder("{");
        msg.append("\"msgCd\"").append(":\"").append(ErrorMsgCode.FORBIDDEN_OPERATION.getMsgCd()).append("\",")
        .append("\"msgInfo\"").append(":\"").append("Forbidden operation.").append("\"}");
        return msg.toString();
    }

    private boolean doShutdownCheck(ServletRequest request) {
        String remoteHost = request.getRemoteHost();
        if (JudgeUtils.contain(supportShutdownIps, remoteHost)) {
            return true;
        }
        return false;
    }

    @Override
    public void destroy() {
        this.supportShutdownIps = null;
    }

}
