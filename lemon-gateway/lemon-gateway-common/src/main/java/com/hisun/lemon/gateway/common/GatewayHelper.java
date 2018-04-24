package com.hisun.lemon.gateway.common;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.hisun.lemon.common.LemonConstants;
import com.hisun.lemon.common.context.LemonContext;
import com.hisun.lemon.common.exception.LemonException;
import com.hisun.lemon.common.utils.JudgeUtils;
import com.hisun.lemon.common.utils.StringUtils;
import com.hisun.lemon.framework.data.LemonHolder;
import com.hisun.lemon.framework.log.logback.MDCUtil;
import com.hisun.lemon.framework.utils.IdGenUtils;

/**
 * @author yuzhou
 * @date 2017年8月12日
 * @time 下午2:32:43
 *
 */
public class GatewayHelper {
    public static final String X_FORWARDED_FOR_HEADER = "X-Forwarded-For";
    public static final String REQUEST_ATTRIBUTE_REQUESTID = "REQUEST_ATTRIBUTE_REQUESTID";
    public static final String REQUEST_ATTRIBUTE_BODY = "REQUEST_ATTRIBUTE_BODY";
    public static final String IGNORE_SET_CHANNEL = "IGNORE_SET_CHANNEL";
    public static final String REQUEST_HEADER_SECURE_INDEX = "x-lemon-secure";
    public static final String LEMON_CONTEXT_REQUEST = "LEMON_CONTEXT_REQUEST";
    
    public static void beforeProcess(HttpServletRequest request) {
        MDCUtil.putMDCKey(getRequestId(request));
        LemonContext.getCurrentContext().put(LEMON_CONTEXT_REQUEST, request);
    }
    
    public static void afterProcess() {
        MDCUtil.removeMDCKey();
        LemonHolder.clear();
        LemonContext.clearCurrentContext();
    }
    
    private static LocaleResolver getLocaleResolver(HttpServletRequest request) {
        return (LocaleResolver) request.getAttribute(GatewayConstants.REQUEST_LEMON_LOCALE_RESOLVER);
    }
    
    public static Locale resolveLocale(HttpServletRequest request) {
        if(RequestContextUtils.getLocaleResolver(request) == null) {
            LocaleResolver localeResolver = getLocaleResolver(request);
            return (localeResolver != null ? localeResolver.resolveLocale(request) : request.getLocale());
        }
        return RequestContextUtils.getLocale(request);
    }
    
    public static Locale resolveLocale() {
        return resolveLocale(getHttpServletRequest());
    }
    
    public static String xforwardedFor(){
        HttpServletRequest request = getHttpServletRequest();
        String xforwardedfor = request.getHeader(X_FORWARDED_FOR_HEADER);
        String remoteAddr = request.getRemoteAddr();
        if (xforwardedfor == null) {
            xforwardedfor = remoteAddr;
        }
        else if (!xforwardedfor.contains(remoteAddr)) { // Prevent duplicates
            xforwardedfor += ", " + remoteAddr;
        }
        return xforwardedfor;
    }

    public static String getToken(HttpServletRequest httpRequest) {
        return Optional.ofNullable(httpRequest.getSession(false)).map(s -> s.getId()).orElseGet(() -> httpRequest.getHeader(LemonConstants.HTTP_HEADER_TOKEN));
    }
    
    public static String getToken() {
        return getToken(getHttpServletRequest());
    }

    /**
     * @return  HttpServletRequest
     */
    public static HttpServletRequest getHttpServletRequest() {
        return Optional.ofNullable(RequestContextHolder.getRequestAttributes()).map(s ->(ServletRequestAttributes) s).map(s ->s.getRequest())
            .orElseGet( () -> Optional.ofNullable(LemonContext.getCurrentContext().get(LEMON_CONTEXT_REQUEST)).map(r -> (HttpServletRequest)r).orElse(null) );
    }
    
    public static String getRequestId(HttpServletRequest request) {
        HttpServletRequest request0 = Optional.ofNullable(request).orElseGet(() -> getHttpServletRequest());
        return  Optional.ofNullable(request0.getAttribute(REQUEST_ATTRIBUTE_REQUESTID)).map(StringUtils::toString)
            .orElseGet(() -> {setRequestId(request0); return (String)request0.getAttribute(REQUEST_ATTRIBUTE_REQUESTID);});
    }
    
    public static String getRequestId() {
        return getRequestId(null);
    }
    
    public static void setRequestId(HttpServletRequest request) {
        HttpServletRequest request0 = (null != request) ? request : getHttpServletRequest();
        request0.setAttribute(REQUEST_ATTRIBUTE_REQUESTID, IdGenUtils.generateRequestId());
    }
    
    public static String getSecureIndex(HttpServletRequest request) {
        return request.getHeader(REQUEST_HEADER_SECURE_INDEX);
    }
    
    public static String getSecureIndex() {
        return getSecureIndex(getHttpServletRequest());
    }
    
    public static InputStream getRequestInputStream(HttpServletRequest request) {
        try {
            return request.getInputStream();
        } catch (IOException ex) {
            throw LemonException.create(ex);
        }
    }
    
    public static String getRequestBody(HttpServletRequest request) {
        if(null != request.getAttribute(REQUEST_ATTRIBUTE_BODY)) {
            return (String)request.getAttribute(REQUEST_ATTRIBUTE_BODY);
        }
        InputStream inputStream = getRequestInputStream(request);
        try {
            if (JudgeUtils.isNotNull(inputStream)) {
                String body = IOUtils.toString(inputStream, "UTF-8");
                request.setAttribute(REQUEST_ATTRIBUTE_BODY, body);
                return body;
            }
        } catch (IOException e) {
            throw LemonException.create(e);
        }
        return null;
    }
}
