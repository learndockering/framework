package com.hisun.lemon.gateway.security;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.hisun.lemon.common.utils.JudgeUtils;
import com.hisun.lemon.framework.data.GenericRspDTO;
import com.hisun.lemon.framework.data.LemonHolder;
import com.hisun.lemon.framework.utils.LemonUtils;
import com.hisun.lemon.gateway.bo.AuthenticationBO;
import com.hisun.lemon.gateway.bo.SecurityBO;
import com.hisun.lemon.gateway.bo.UserInfoBase;
import com.hisun.lemon.gateway.common.GatewayHelper;
import com.hisun.lemon.gateway.service.IAuthenticationService;
import com.hisun.lemon.gateway.service.ISecurityService;

/**
 * 认证
 * @author yuzhou
 * @date 2017年7月29日
 * @time 上午11:02:37
 *
 */
public class LemonAuthenticationProvider implements AuthenticationProvider {

    private IAuthenticationService authenticationServiceImpl;
    private ISecurityService securityServiceImpl;
    
    public LemonAuthenticationProvider(IAuthenticationService authenticationServiceImpl, ISecurityService securityServiceImpl) {
        this.authenticationServiceImpl = authenticationServiceImpl;
        this.securityServiceImpl = securityServiceImpl;
    }
    
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        LemonUsernamePasswordAuthenticationToken lemonUsernamePasswordAuthenticationToken = null;
        if (authentication instanceof LemonUsernamePasswordAuthenticationToken) {
            lemonUsernamePasswordAuthenticationToken = (LemonUsernamePasswordAuthenticationToken) authentication;
        } else {
            if(authentication instanceof UsernamePasswordAuthenticationToken) {
                String random = "";
                Object details = authentication.getDetails();
                if(details instanceof Map) {
                    random = (String)((Map<?, ?>) details).get(LemonAuthenticationFilter.RANDOM_PARAM_NAME);
                }
                lemonUsernamePasswordAuthenticationToken = new LemonUsernamePasswordAuthenticationToken(authentication.getPrincipal(), authentication.getCredentials(), random);
            }
        }
        if(null == lemonUsernamePasswordAuthenticationToken) {
            throw new LemonAuthenticationException("only support UsernamePasswordAuthenticationToken token.");
        }
        UserDetails authUser = null;
        try {
            authUser = findAuthentictionUser(lemonUsernamePasswordAuthenticationToken);
        } catch (AuthenticationException e) {
            throw e;
        } catch (Exception e) {
            throw new InternalAuthenticationServiceException(e.getMessage(), e);
        }
        return createSuccessAuthentication(authUser, authentication, authUser);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }
    
    public UserDetails findAuthentictionUser(UsernamePasswordAuthenticationToken authentication) {
        String userName = (String)authentication.getPrincipal();
        AuthenticationBO authenticationBO = new AuthenticationBO(userName, String.valueOf(authentication.getCredentials()), ((LemonUsernamePasswordAuthenticationToken)authentication).getRandom());
        if(JudgeUtils.isBlankAny(authenticationBO.getUserName(), authenticationBO.getPassword())) {
            throw new LemonAuthenticationException("Username or password is null.");
        }
        GenericRspDTO<? extends UserInfoBase> authRspDTO = authenticationServiceImpl.authentication(authenticationBO);
       // GenericRspDTO<? extends UserInfoBase> authRspDTO = ExtensionLoader.getSpringBean("userServiceImpl", IUserService.class).queryUserInfoByLoginId("13874815109");
        if(JudgeUtils.isNotSuccess(authRspDTO.getMsgCd()) || JudgeUtils.isNull(authRspDTO.getBody())) {
            throw new LemonAuthenticationException(authRspDTO);
        }
        UserInfo userInfo = new UserInfo();
        userInfo.setLoginName(userName);
        userInfo.setUserId(authRspDTO.getBody().getUserId());
        userInfo.setUserBasicInfo(authRspDTO.getBody());
        doAfterLogin(userInfo);
        String channel = Optional.ofNullable(LemonUtils.getChannel()) .orElseGet(() -> Optional.ofNullable(findSecurityBO()).map(s -> s.getChannel()).orElse("") );
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

}
