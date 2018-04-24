package com.hisun.lemon.gateway.session.refresh;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.ReflectionUtils;

import com.hisun.lemon.common.utils.JudgeUtils;
import com.hisun.lemon.framework.data.GenericRspDTO;
import com.hisun.lemon.framework.data.LemonHolder;
import com.hisun.lemon.gateway.bo.SecurityBO;
import com.hisun.lemon.gateway.bo.UserInfoBase;
import com.hisun.lemon.gateway.common.GatewayHelper;
import com.hisun.lemon.gateway.security.LemonAuthenticationException;
import com.hisun.lemon.gateway.security.LemonUser;
import com.hisun.lemon.gateway.security.SecurityUtils;
import com.hisun.lemon.gateway.security.UserInfo;
import com.hisun.lemon.gateway.service.ISecurityService;
import com.hisun.lemon.gateway.service.IUserService;

/**
 * @author yuzhou
 * @date 2017年9月13日
 * @time 下午5:18:19
 *
 */
public class RefreshTokenAuthenticationProvider implements AuthenticationProvider {

    private IUserService userServiceImpl;
    private ISecurityService securityServiceImpl;
    private TokenService tokenService;
    private final Method changeSessionIdMethod;
    
    public RefreshTokenAuthenticationProvider(IUserService userServiceImpl, ISecurityService securityServiceImpl,
        TokenService tokenService) {
        this.userServiceImpl = userServiceImpl;
        this.securityServiceImpl = securityServiceImpl;
        this.tokenService = tokenService;
        
        Method changeSessionIdMethod = ReflectionUtils
                .findMethod(HttpServletRequest.class, "changeSessionId");
        if (changeSessionIdMethod == null) {
            throw new IllegalStateException(
                    "HttpServletRequest.changeSessionId is undefined. Are you using a Servlet 3.1+ environment?");
        }
        this.changeSessionIdMethod = changeSessionIdMethod;
    }
    
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UserDetails authUser = null;
        try {
            refreshToken(resolveHttpSession(), authentication);
            authUser = findAuthentictionUser(authentication);
        } catch (AuthenticationException e) {
            throw e;
        } catch (Exception e) {
            throw new InternalAuthenticationServiceException(e.getMessage(), e);
        }
        return createSuccessAuthentication(authUser, authentication, authUser);
    }
    
    private HttpSession resolveHttpSession() {
        return applySessionFixation(GatewayHelper.getHttpServletRequest());
    }
    
    private void refreshToken(HttpSession session, Authentication authentication) {
        AccessToken accessToken = tokenService.refreshAccessToken(session, (String)authentication.getCredentials());
        if (authentication instanceof RefreshTokenAuthenticationToken) {
            if (accessToken instanceof LemonAccessToken) {
                LemonRefreshToken refreshToken = (LemonRefreshToken)((LemonAccessToken)accessToken).getRefreshToken();
                ((RefreshTokenAuthenticationToken )authentication).setPrincipal(refreshToken.getUserId());
            }
        }
    }

    public UserDetails findAuthentictionUser(Authentication authentication) {
        String userName = (String)authentication.getPrincipal();
        if(JudgeUtils.isBlankAny(userName)) {
            throw new LemonAuthenticationException("Refresh token is blank.");
        }
        GenericRspDTO<? extends UserInfoBase> authRspDTO = this.userServiceImpl.queryUserInfoByLoginId(userName);
        if(JudgeUtils.isNotSuccess(authRspDTO.getMsgCd()) || JudgeUtils.isNull(authRspDTO.getBody())) {
            throw new LemonAuthenticationException(authRspDTO);
        }
        UserInfo userInfo = new UserInfo();
        userInfo.setLoginName(userName);
        userInfo.setUserId(authRspDTO.getBody().getUserId());
        userInfo.setUserBasicInfo(authRspDTO.getBody());
        doAfterLogin(userInfo);
        String channel = Optional.ofNullable(findSecurityBO()).map(s -> s.getChannel()).orElse("");
        return new LemonUser<>(userName, userInfo, channel, true, true, true, true, grantedAuthority());
    }
    
    public SecurityBO findSecurityBO() {
       return securityServiceImpl.querySecurity(GatewayHelper.getSecureIndex());
    }
    
    public void doAfterLogin(UserInfo userInfo) {
        SecurityUtils.setLoginUserAfterLoginRequest(userInfo);
        LemonHolder.getLemonData().setLoginName(userInfo.getLoginName());
        LemonHolder.getLemonData().setUserId(userInfo.getUserId());
    }
    
    protected List<GrantedAuthority> grantedAuthority() {
        List<GrantedAuthority> grantedAuthoritys = new ArrayList<GrantedAuthority>();
        grantedAuthoritys.add(new SimpleGrantedAuthority("Generic"));
        return grantedAuthoritys;
    }
    
    protected Authentication createSuccessAuthentication(Object principal, Authentication authentication, UserDetails user) {
        UsernamePasswordAuthenticationToken result = new UsernamePasswordAuthenticationToken(
            principal, authentication.getCredentials(), user.getAuthorities());
        result.setDetails(authentication.getDetails());
        return result;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (RefreshTokenAuthenticationToken.class.isAssignableFrom(authentication));
    }
    
    private HttpSession applySessionFixation(HttpServletRequest request) {
        if(JudgeUtils.isNotNull(request.getSession(false))) {
            ReflectionUtils.invokeMethod(this.changeSessionIdMethod, request);
        }
        return request.getSession();
    }

}
