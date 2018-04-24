package com.hisun.lemon.gateway.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import com.hisun.lemon.common.exception.ErrorMsgCode;
import com.hisun.lemon.gateway.common.response.ResponseMessageResolver;

/**
 * 未认证的请求
 * @author yuzhou
 * @date 2017年8月4日
 * @time 下午5:32:07
 *
 */
public class LemonAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private ResponseMessageResolver responseMessageResolver;

    public LemonAuthenticationEntryPoint(ResponseMessageResolver responseMessageResolver) {
        this.responseMessageResolver = responseMessageResolver;
    }
    
    @Override
    public void commence(HttpServletRequest request,
            HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        this.responseMessageResolver.resolve(request, response, ErrorMsgCode.NO_AUTH_ERROR);
    }

}
