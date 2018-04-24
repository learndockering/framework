package com.hisun.lemon.gateway.session.refresh;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hisun.lemon.common.exception.ErrorMsgCode;
import com.hisun.lemon.common.exception.LemonException;
import com.hisun.lemon.common.utils.JudgeUtils;
import com.hisun.lemon.gateway.common.GatewayAccessLogger;
import com.hisun.lemon.gateway.common.GatewayHelper;
import com.hisun.lemon.gateway.common.signature.SignatureResolver;

public class LemonRefreshTokenAuthenticationProcessingFilter extends AbstractAuthenticationProcessingFilter {
    public static final String DEFAULT_REFRESH_TOKEN_PROCESSES_URL = "/security/refresh";
    public static final String DEFAULT_REFRESH_TOKEN_PARAMETER_KEY = "refreshToken";
    
    private boolean postOnly = true;
    private ObjectMapper objectMapper;
    private SignatureResolver loginSignatureResolver;
    protected AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource = new WebAuthenticationDetailsSource();
    
    public LemonRefreshTokenAuthenticationProcessingFilter(String defaultFilterProcessesUrl, ObjectMapper objectMapper, SignatureResolver loginSignatureResolver) {
        super(defaultFilterProcessesUrl);
        this.objectMapper = objectMapper;
        this.loginSignatureResolver = loginSignatureResolver;
    }
    
    public LemonRefreshTokenAuthenticationProcessingFilter(ObjectMapper objectMapper, SignatureResolver loginSignatureResolver) {
        super(DEFAULT_REFRESH_TOKEN_PROCESSES_URL);
        this.objectMapper = objectMapper;
        this.loginSignatureResolver = loginSignatureResolver;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) 
        throws AuthenticationException, IOException, ServletException {
        
        GatewayAccessLogger.printRequestLog(request, null, null, "PROTECTED");
        if (postOnly && !request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Refresh token method not supported: " + request.getMethod());
        }
        
        if(loginSignatureResolver.shouldVerify() && ! loginSignatureResolver.verify()) {
            throw new InternalAuthenticationServiceException("signature error", LemonException.create(ErrorMsgCode.SIGNATURE_EXCEPTION.getMsgCd()));
        }
        
        String refreshToken = "";
        
        Map<String, String> map = null;
        String body = GatewayHelper.getRequestBody(request);
        if(JudgeUtils.isNotBlank(body)) {
            try {
                map = this.objectMapper.readValue(body, Map.class);
            } catch (IOException e) {
            }
        }
        
        if (JudgeUtils.isNotEmpty(map)) {
            refreshToken = map.get(this.getRefreshTokenParameter());
        } else {
            refreshToken = obtainRefreshToken(request);
        }

        if (JudgeUtils.isBlank(refreshToken)) {
            throw new AuthenticationServiceException("refresh token is blank.");
        }

        RefreshTokenAuthenticationToken authRequest = new RefreshTokenAuthenticationToken(refreshToken);

        // Allow subclasses to set the "details" property
        setDetails(request, authRequest);

        return this.getAuthenticationManager().authenticate(authRequest);
    }
    
    protected String getRefreshTokenParameter() {
        return DEFAULT_REFRESH_TOKEN_PARAMETER_KEY;
    }
    
    protected String obtainRefreshToken(HttpServletRequest request) {
        return request.getParameter(getRefreshTokenParameter());
    }
    
    protected void setDetails(HttpServletRequest request, RefreshTokenAuthenticationToken authRequest) {
        authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
    }
}
