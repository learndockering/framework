package com.hisun.lemon.framework.utils;

import java.util.Locale;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.hisun.lemon.common.LemonConstants;
import com.hisun.lemon.common.exception.ErrorMsgCode;
import com.hisun.lemon.common.exception.LemonException;
import com.hisun.lemon.common.utils.JudgeUtils;
import com.hisun.lemon.common.utils.StringUtils;
import com.hisun.lemon.framework.data.GenericDTO;

/**
 * @author yuzhou
 * @date 2017年8月9日
 * @time 上午11:32:33
 *
 */
public class WebUtils {
    private static final Logger logger = LoggerFactory.getLogger(WebUtils.class);
    /**
     * @return  HttpServletRequest
     */
    public static HttpServletRequest getHttpServletRequest() {
        return Optional.ofNullable(RequestContextHolder.getRequestAttributes()).map(a -> (ServletRequestAttributes)a).map(s -> s.getRequest()).orElse(null);
    }
    
    /**
     * 校验请求数据
     * @param requestDTOs
     * @param request
     * @return
     */
    public static void validateRequestData(GenericDTO<?> requestDTO, HttpServletRequest request) {
        String userIdFromHeader = request.getHeader(LemonConstants.HTTP_HEADER_USERID);
        String usrLoginNameHeader = request.getHeader(LemonConstants.HTTP_HEADER_LOGINNAME);
        if(JudgeUtils.isBlankAll(userIdFromHeader, usrLoginNameHeader)) {
            return;
        }
        
        if(JudgeUtils.isNotBlankAll(userIdFromHeader, requestDTO.getUserId()) && JudgeUtils.notEquals(requestDTO.getUserId(), userIdFromHeader)) {
            LemonException.throwLemonException(ErrorMsgCode.ILLEGAL_PARAMETER.getMsgCd());
        }
        
        if(JudgeUtils.isNotBlankAll(usrLoginNameHeader, requestDTO.getLoginName()) && JudgeUtils.notEquals(requestDTO.getLoginName(), usrLoginNameHeader)) {
            LemonException.throwLemonException(ErrorMsgCode.ILLEGAL_PARAMETER.getMsgCd());
        }
    }
    
    /**
     * http request header validation
     * @param request
     */
    public static void validateRequestHeaderForEntryTx(HttpServletRequest request) {
        if(JudgeUtils.isBlank(request.getHeader(LemonConstants.HTTP_HEADER_FOR))) {
            if(logger.isWarnEnabled()) {
                logger.warn("Failed to validate request header \"{}\".", LemonConstants.HTTP_HEADER_FOR );
            }
           // LemonException.throwLemonException(ErrorMsgCode.ILLEGAL_HTTP_REQUEST_HEADER.getMsgCd());
        }
        if(JudgeUtils.isBlank(request.getHeader(LemonConstants.HTTP_HEADER_LOCALE))) {
            if(logger.isWarnEnabled()) {
                logger.warn("Failed to validate request header \"{}\".", LemonConstants.HTTP_HEADER_LOCALE );
            }
           // LemonException.throwLemonException(ErrorMsgCode.ILLEGAL_HTTP_REQUEST_HEADER.getMsgCd());
        }
        if(JudgeUtils.isBlank(request.getHeader(LemonConstants.HTTP_HEADER_SOURCE))) {
            if(logger.isWarnEnabled()) {
                logger.warn("Failed to validate request header \"{}\".", LemonConstants.HTTP_HEADER_SOURCE );
            }
           // LemonException.throwLemonException(ErrorMsgCode.ILLEGAL_HTTP_REQUEST_HEADER.getMsgCd());
        }
    }
    
    /**
     * 获取Ip
     * @param request
     * @return
     */
    public static String resolveClientIP(HttpServletRequest request) {
        String remoteIp = Optional.ofNullable(request).map(r -> r.getHeader(LemonConstants.HTTP_HEADER_FOR))
            .filter(StringUtils::isNotBlank).map(x -> x.split(",")).map(a -> a[0]).orElse(null);
        return StringUtils.getDefaultIfEmpty(remoteIp, request.getRemoteAddr());
    }
    
    /**
     * 获取Locale
     * @param request
     * @return
     */
    public static Locale resolveLocale(HttpServletRequest request) {
        return Optional.ofNullable(request).map(r -> r.getHeader(LemonConstants.HTTP_HEADER_LOCALE)).map(l -> l.split("_"))
            .map(ls -> new Locale(ls[0], ls.length == 2 ? ls[1] : "" )).orElseGet(() -> LemonUtils.getLemonEnvironment().getDefaultLocale() );
    }
    
    /**
     * 获取requestId
     * @param request
     * @param create requestId为null时，是否创建
     * @return
     */
    public static String resolveRequestId(HttpServletRequest request, boolean create) {
        if(JudgeUtils.isNotNull(request.getHeader(LemonConstants.HTTP_HEADER_REQUESTID))) {
            return request.getHeader(LemonConstants.HTTP_HEADER_REQUESTID);
        }
        if(create) {
            return IdGenUtils.generateRequestId();
        }
        return null;
    }
    
    /**
     * 获取requestId，当requestId为null时创建
     * @param request
     * @return
     */
    public static String resolveRequestId(HttpServletRequest request) {
        return resolveRequestId(request, true);
    }
}
