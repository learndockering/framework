package com.hisun.lemon.gateway.common;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hisun.lemon.common.LemonConstants;
import com.hisun.lemon.common.utils.StringUtils;
import com.hisun.lemon.framework.data.GenericRspDTO;
import com.hisun.lemon.framework.utils.LemonUtils;

/**
 * 网关日志
 * @author yuzhou
 * @date 2017年8月15日
 * @time 上午11:07:51
 *
 */
public class GatewayAccessLogger {
    public static final Logger logger = LoggerFactory.getLogger(GatewayAccessLogger.class);
    public static final String REQUEST_KEY_ALREADY_PRINT_RESPONSE_LOG = "REQUEST_KEY_ALREADY_PRINT_RESPONSE_LOG";
    public static final String REQUEST_KEY_ALREADY_PRINT_REQUEST_LOG = "REQUEST_KEY_ALREADY_PRINT_REQUEST_LOG";
    public static final String REQUEST_VAL_ALREADY_PRINT_LOG = "Y";
    public static final String PARAMETER_SEPARATOR = "&";
    public static final String PARAMETER_KV_SEPARATOR = "=";
    
    public static void printRequestLog(HttpServletRequest request, String userId, Map<String,String[]> parameters, String body) {
        if(logger.isInfoEnabled()) {
            if(alreadyPrintRequestLog(request)) {
                return;
            }
            try{
                String parametersStr = Optional.ofNullable(parameters).map(p -> p.entrySet().stream().map(e -> e.getKey().concat(PARAMETER_KV_SEPARATOR).concat(Arrays.toString(e.getValue())))
                    .collect(Collectors.joining(PARAMETER_SEPARATOR))).orElse(null);
                logger.info("{\"action\":\"request\", \"uri\":\"{}\",\"requestId\":\"{}\", \"method\":\"{}\", \"remoteIp\":\"{}\", \"application\":\"{}\", "
                        + " \"userId\":\"{}\", \"sessionId\":\"{}\", \"parameters\":\"{}\", \"body\":{}}",
                        request.getRequestURI(), GatewayHelper.getRequestId(request), request.getMethod(), StringUtils.getDefaultIfEmpty(request.getHeader(LemonConstants.HTTP_HEADER_FOR), request.getRemoteAddr()), LemonUtils.getApplicationName(), 
                        userId, Optional.ofNullable(request.getSession(false)).map(s -> s.getId()).orElse(null), parametersStr, StringUtils.getDefaultIfEmpty(body, "null"));
                request.setAttribute(REQUEST_KEY_ALREADY_PRINT_REQUEST_LOG, REQUEST_VAL_ALREADY_PRINT_LOG);
            }catch(Throwable t){logger.error("durning print request log occur error.", t);}
        }
    }
    
    public static void printResponseLog(HttpServletRequest request, HttpServletResponse response, String body) {
        if(logger.isInfoEnabled()) {
            if(alreadyPrintResponseLog(request)) {
                return;
            }
            try{
                logger.info("{\"action\":\"response\", \"uri\":\"{}\", \"requestId\":\"{}\", \"rspcode\":\"{}\", \"body\":{}}", 
                    request.getRequestURI(), GatewayHelper.getRequestId(request), response.getStatus(), StringUtils.getDefaultIfEmpty(body, "UNKNOW"));
                request.setAttribute(REQUEST_KEY_ALREADY_PRINT_RESPONSE_LOG, REQUEST_VAL_ALREADY_PRINT_LOG);
            }catch(Throwable t){logger.error("durning print response log occur error.", t);}
        }
    }
    
    public static void printResponseLog(HttpServletRequest request, HttpServletResponse response, GenericRspDTO<?> rspDTO, ObjectMapper objectMapper) {
        if(logger.isInfoEnabled()) {
            if(alreadyPrintResponseLog(request)) {
                return;
            }
            try {
                printResponseLog(request, response, objectMapper.writeValueAsString(rspDTO));
            } catch (JsonProcessingException e) { logger.error("durning print response log occur error.", e);}
        }
    }
    
    public static boolean alreadyPrintResponseLog(HttpServletRequest request){
        return Optional.ofNullable(request.getAttribute(REQUEST_KEY_ALREADY_PRINT_RESPONSE_LOG)).map(c -> true).orElse(false);
    }
    
    public static boolean alreadyPrintRequestLog(HttpServletRequest request){
        return Optional.ofNullable(request.getAttribute(REQUEST_KEY_ALREADY_PRINT_REQUEST_LOG)).map(c -> true).orElse(false);
    }
    
}
