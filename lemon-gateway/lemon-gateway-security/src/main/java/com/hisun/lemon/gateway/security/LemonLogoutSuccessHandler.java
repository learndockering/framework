package com.hisun.lemon.gateway.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import com.hisun.lemon.common.utils.JudgeUtils;
import com.hisun.lemon.framework.data.GenericRspDTO;
import com.hisun.lemon.gateway.common.response.ResponseMessageResolver;
import com.hisun.lemon.gateway.session.refresh.TokenService;

/**
 * 登出成功
 * @author yuzhou
 * @date 2017年8月3日
 * @time 上午9:39:27
 *
 */
public class LemonLogoutSuccessHandler implements LogoutSuccessHandler {
    public static final String REQUEST_ATTRIBUTE_KEY_SESSION_ID = "REQUEST_ATTRIBUTE_KEY_SESSION_ID";
    private ResponseMessageResolver responseMessageResolver;
    private TokenService tokenService;
    
    public LemonLogoutSuccessHandler(ResponseMessageResolver responseMessageResolver, TokenService tokenService) {
        this.responseMessageResolver = responseMessageResolver;
        this.tokenService = tokenService;
    }
    @Override
    public void onLogoutSuccess(HttpServletRequest request,
            HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_OK);
        if(JudgeUtils.isNotNull(this.tokenService)) {
           this.tokenService.revokeToken(request.getSession(), String.valueOf(request.getAttribute(REQUEST_ATTRIBUTE_KEY_SESSION_ID)));
        }
        this.responseMessageResolver.resolve(request, response, GenericRspDTO.newSuccessInstance());
    }

}
