package com.hisun.lemon.gateway.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import com.hisun.lemon.common.exception.ErrorMsgCode;
import com.hisun.lemon.common.exception.LemonException;
import com.hisun.lemon.common.utils.JudgeUtils;
import com.hisun.lemon.common.utils.StringUtils;
import com.hisun.lemon.framework.data.GenericRspDTO;
import com.hisun.lemon.gateway.common.response.ResponseMessageResolver;

/**
 * 认证失败
 * @author yuzhou
 * @date 2017年8月3日
 * @time 上午9:33:05
 *
 */
public class LemonAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    private static final Logger logger = LoggerFactory.getLogger(LemonAuthenticationFailureHandler.class);
    private ResponseMessageResolver responseMessageResolver;
    
    public LemonAuthenticationFailureHandler(ResponseMessageResolver responseMessageResolver) {
        this.responseMessageResolver = responseMessageResolver;
    }
    
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
        AuthenticationException exception) throws IOException, ServletException {
        //response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setStatus(HttpServletResponse.SC_OK);
        if(logger.isErrorEnabled()) {
            logger.error("authentication failure.", exception);
        }
        if(exception instanceof LemonAuthenticationException) {
            LemonAuthenticationException lae = (LemonAuthenticationException) exception;
            GenericRspDTO<?> rspDTO = lae.getGenericRspDTO();
            if (JudgeUtils.isNull(rspDTO)) {
                this.responseMessageResolver.resolve(request, response, ErrorMsgCode.NO_AUTH_ERROR);
            } else {
                if(logger.isInfoEnabled()) {
                    logger.info("authentication failure, response dto {}", rspDTO);
                }
                rspDTO.setBody(null);
                this.responseMessageResolver.resolve(request, response, rspDTO);
            }
        } else {
            String msgCd = ErrorMsgCode.SYS_ERROR.getMsgCd();
            if(JudgeUtils.isNotNull(exception.getCause()) && exception.getCause() instanceof LemonException) {
                LemonException le = (LemonException) exception.getCause();
                msgCd = StringUtils.getDefaultIfEmpty(le.getMsgCd(), ErrorMsgCode.SYS_ERROR.getMsgCd());
            }
            this.responseMessageResolver.resolve(request, response, msgCd);
        }
    }
}
