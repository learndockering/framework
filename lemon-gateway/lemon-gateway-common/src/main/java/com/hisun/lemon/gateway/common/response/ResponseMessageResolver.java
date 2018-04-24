package com.hisun.lemon.gateway.common.response;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hisun.lemon.common.exception.ErrorMsgCode;
import com.hisun.lemon.framework.data.GenericRspDTO;

/**
 * @author yuzhou
 * @date 2017年9月9日
 * @time 下午12:58:14
 *
 */
public interface ResponseMessageResolver {
    
    void resolve(HttpServletRequest request, HttpServletResponse response, ErrorMsgCode errorMsgCode) throws IOException;
    
    void resolve(HttpServletRequest request, HttpServletResponse response, String msgCode) throws IOException;
    
    void resolve(HttpServletRequest request, HttpServletResponse response, GenericRspDTO<?> genericRspDTO) throws IOException;
    
    byte[] generateBytes(ErrorMsgCode errorMsgCode) throws IOException;
    
    byte[] generateBytes(String msgCode) throws IOException;
    
    String generateString(ErrorMsgCode errorMsgCode);
    
    String generateString(String msgCode);
}
