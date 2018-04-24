package com.hisun.lemon.gateway.security;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hisun.lemon.common.exception.ErrorMsgCode;
import com.hisun.lemon.common.exception.LemonException;
import com.hisun.lemon.common.utils.JudgeUtils;
import com.hisun.lemon.gateway.common.GatewayAccessLogger;
import com.hisun.lemon.gateway.common.GatewayHelper;
import com.hisun.lemon.gateway.common.signature.SignatureResolver;

/**
 * authentication filter
 * 
 * @author yuzhou
 * @date 2017年8月11日
 * @time 下午4:56:02
 *
 */
public class LemonAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    public static final String RANDOM_PARAM_NAME = "random";
    private boolean postOnly = true;
    private ObjectMapper objectMapper;
    private SignatureResolver loginSignatureResolver;
    private String random = RANDOM_PARAM_NAME;
    
    public LemonAuthenticationFilter(ObjectMapper objectMapper, SignatureResolver loginSignatureResolver) {
        super();
        this.objectMapper = objectMapper;
        this.loginSignatureResolver = loginSignatureResolver;
    }
    
    @SuppressWarnings("unchecked")
    public Authentication attemptAuthentication(HttpServletRequest request,
            HttpServletResponse response) throws AuthenticationException {
        GatewayAccessLogger.printRequestLog(request, null, null, "PROTECTED");
        if (postOnly && !request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException(
                    "Authentication method not supported: " + request.getMethod());
        }
        
        if(loginSignatureResolver.shouldVerify() && ! loginSignatureResolver.verify()) {
            throw new InternalAuthenticationServiceException("signature error", LemonException.create(ErrorMsgCode.SIGNATURE_EXCEPTION.getMsgCd()));
        }
        
        String username = "";
        String password = "";
        String random = "";
        
        Map<String, String> map = null;
        String body = GatewayHelper.getRequestBody(request);
        if(JudgeUtils.isNotBlank(body)) {
            try {
                map = this.objectMapper.readValue(body, Map.class);
            } catch (IOException e) {
            }
        }
        
        if (JudgeUtils.isNotEmpty(map)) {
            username = map.get(this.getUsernameParameter());
            password = map.get(this.getPasswordParameter());
            random = map.get(this.getRandom());
        } else {
            username = obtainUsername(request);
            password = obtainPassword(request);
            random = obtainRandom(request);
        }

        if (username == null) {
            username = "";
        }

        if (password == null) {
            password = "";
        }
        
        if(random == null) {
            random = "";
        }

        username = username.trim();
        
        LemonUsernamePasswordAuthenticationToken authRequest = new LemonUsernamePasswordAuthenticationToken(
            username, password, random);

        // Allow subclasses to set the "details" property
        setDetails(request, authRequest);

        return this.getAuthenticationManager().authenticate(authRequest);
    }
    
    public String getRandom() {
        return this.random;
    }
    
    protected String obtainRandom(HttpServletRequest request) {
        return request.getParameter(getRandom());
    }
}
