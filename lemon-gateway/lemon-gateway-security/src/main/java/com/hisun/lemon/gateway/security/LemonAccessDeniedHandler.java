package com.hisun.lemon.gateway.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import com.hisun.lemon.common.exception.ErrorMsgCode;
import com.hisun.lemon.gateway.common.response.ResponseMessageResolver;

public class LemonAccessDeniedHandler implements AccessDeniedHandler {
    private ResponseMessageResolver responseMessageResolver;
    
    public LemonAccessDeniedHandler(ResponseMessageResolver responseMessageResolver) {
        this.responseMessageResolver = responseMessageResolver;
    }
    @Override
    public void handle(HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException accessDeniedException) throws IOException,
            ServletException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        this.responseMessageResolver.resolve(request, response, ErrorMsgCode.NO_AUTH_ERROR);
    }

}
