package com.hisun.lemon.gateway.security;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.session.FindByIndexNameSessionRepository;

import com.hisun.lemon.common.extension.Inject;
import com.hisun.lemon.common.extension.Inject.Type;
import com.hisun.lemon.common.utils.BeanUtils;
import com.hisun.lemon.common.utils.JudgeUtils;
import com.hisun.lemon.framework.data.DataHelper;
import com.hisun.lemon.framework.data.GenericRspDTO;
import com.hisun.lemon.framework.data.LemonHolder;
import com.hisun.lemon.gateway.common.GatewayHelper;
import com.hisun.lemon.gateway.common.extension.LoginSuccessProcessor;
import com.hisun.lemon.gateway.common.response.ResponseMessageResolver;
import com.hisun.lemon.gateway.session.refresh.TokenService;

/**
 * auth success
 * @author yuzhou
 * @date 2017年8月4日
 * @time 下午3:36:07
 *
 */
public class LemonAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    private static final Logger logger = LoggerFactory.getLogger(LemonAuthenticationSuccessHandler.class);

    private ResponseMessageResolver responseMessageResolver;
    private Boolean refreshToken;
    private TokenService tokenService;
    @Inject(type=Type.LIST)
    private List<LoginSuccessProcessor> loginSuccessProcessors;

    public LemonAuthenticationSuccessHandler(ResponseMessageResolver responseMessageResolver,
        TokenService tokenService, boolean refreshToken) {
        this.responseMessageResolver = responseMessageResolver;
        this.refreshToken = refreshToken;
        this.tokenService = tokenService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        request.getSession();//创建session
        if(JudgeUtils.isTrue(refreshToken, true)) {
            resolveRefreshToken(request);
        }
        response.setStatus(HttpServletResponse.SC_OK);
        LemonHolder.getLemonData().setToken(request.getSession().getId());
        if(JudgeUtils.isNotEmpty(loginSuccessProcessors)) {
            loginSuccessProcessors.stream().forEach(p -> p.process());
        }
        //用户以userId为维度控制登录用户终端数量，商户以loginName维度控制
        Optional.ofNullable(request.getSession()).map(s -> {s.setAttribute(FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME, EliminateSessionHelper.eliminateSessionDimension(authentication.getPrincipal())); return true;}).orElse(false);
        GenericRspDTO<LoginUser> loginRspDTO = createResponseDTO(request);
        if(logger.isDebugEnabled()) {
            logger.debug("authentication success {}, response DTO {}", authentication.getPrincipal(), loginRspDTO);
        }
        this.responseMessageResolver.resolve(request, response, loginRspDTO);
    }
    
    private void resolveRefreshToken(HttpServletRequest request) {
        this.tokenService.createAccessToken(request.getSession());
    }

    private GenericRspDTO<LoginUser> createResponseDTO(HttpServletRequest request) {
        LoginUser loginUser = new LoginUser();
        loginUser.setSessionId(request.getSession().getId());
        loginUser.setRefreshToken(tokenService.getAccessToken(request.getSession()).getRefreshToken().getValue());
        BeanUtils.copyProperties(loginUser, SecurityUtils.getLoginUser());
        GenericRspDTO<LoginUser> rspDTO = GenericRspDTO.newSuccessInstance(loginUser);
        DataHelper.setRequestId(rspDTO, GatewayHelper.getRequestId(request));
        return rspDTO;
        
    }
    
    /**
     * @author yuzhou
     * @date 2017年8月11日
     * @time 下午12:06:44
     *
     */
    public static class LoginUser extends UserInfo {
        private static final long serialVersionUID = 6714710359532177968L;
        private String sessionId;
        private String refreshToken;
        
        public String getSessionId() {
            return sessionId;
        }
        public void setSessionId(String sessionId) {
            this.sessionId = sessionId;
        }
        public String getRefreshToken() {
            return refreshToken;
        }
        public void setRefreshToken(String refreshToken) {
            this.refreshToken = refreshToken;
        }
        
    }
}
