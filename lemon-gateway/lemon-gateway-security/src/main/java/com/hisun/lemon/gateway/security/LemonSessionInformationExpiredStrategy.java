package com.hisun.lemon.gateway.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;

import com.hisun.lemon.common.exception.ErrorMsgCode;
import com.hisun.lemon.gateway.common.response.ResponseMessageResolver;

/**
 * session 过期策略
 * ConcurrentSessionFilter 检测是否过期
 * 同一类型终端多个登录依靠ConcurrentSessionFilter，剔除后进入此策略
 * @author yuzhou
 * @date 2017年9月9日
 * @time 下午1:55:30
 *
 */
public class LemonSessionInformationExpiredStrategy implements SessionInformationExpiredStrategy {
    private ResponseMessageResolver responseMessageResolver;
    
    public LemonSessionInformationExpiredStrategy(ResponseMessageResolver responseMessageResolver) {
        this.responseMessageResolver = responseMessageResolver;
    }

    @Override
    public void onExpiredSessionDetected(SessionInformationExpiredEvent eventØ) throws IOException, ServletException {
        HttpServletResponse response = eventØ.getResponse();
        HttpServletRequest request = eventØ.getRequest();
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        this.responseMessageResolver.resolve(request, response, ErrorMsgCode.NO_AUTH_ERROR);
    }

}
