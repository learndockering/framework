package com.hisun.lemon.gateway.security;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.session.ExpiringSession;
import org.springframework.session.SessionRepository;

import com.hisun.lemon.common.utils.JudgeUtils;
import com.hisun.lemon.gateway.session.refresh.TokenService;

/**
 * 解决多终端登录、多session共存策略
 * @author yuzhou
 * @date 2017年8月30日
 * @time 下午9:01:17
 *
 */
public class LemonSessionAuthenticationStrategy implements SessionAuthenticationStrategy {
    private static final Logger logger = LoggerFactory.getLogger(LemonSessionAuthenticationStrategy.class);
    
    private String springSecurityContextKey = HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;
    
    private SessionRegistry sessionRegistry;
    private SessionRepository<ExpiringSession> sessionRepository;
    private TokenService tokenService;
    
    public LemonSessionAuthenticationStrategy( SessionRegistry sessionRegistry, SessionRepository<ExpiringSession> sessionRepository, TokenService tokenService) {
        this.sessionRegistry = sessionRegistry;
        this.sessionRepository = sessionRepository;
        this.tokenService = tokenService;
    }
    @Override
    public void onAuthentication(Authentication authentication, HttpServletRequest request, HttpServletResponse response)
        throws SessionAuthenticationException {
        Object principal = resolvePrincipal(authentication.getPrincipal());
        Channel currentUsrLoginChannel = SecurityUtils.getLoginChannelByPrincipal(authentication.getPrincipal());
        if(JudgeUtils.isNotNull(principal) && JudgeUtils.isNotNull(currentUsrLoginChannel)) {
            List<SessionInformation> sessionInfos =this.sessionRegistry.getAllSessions(principal, false);
            if(JudgeUtils.isNotEmpty(sessionInfos)) {
                String curSessionId = request.getSession().getId();
                sessionInfos.stream().filter(si -> JudgeUtils.notEquals(curSessionId, si.getSessionId())).filter(si -> requireExpire(si, currentUsrLoginChannel)).forEach(si -> expireNow(si));
            }
        } else {
            if(logger.isWarnEnabled()) {
                logger.warn("Principal or current user login channel is null during expire othen login user session, may be is bug.");
                logger.warn("principal is {}, current user login channel is {}", principal, currentUsrLoginChannel);
            }
            return ;
        }
    }

    public boolean requireExpire(SessionInformation sessionInformation, Channel currentUserLoginChannel) {
        String sessionId = sessionInformation.getSessionId();
        ExpiringSession eSession = this.sessionRepository.getSession(sessionId);
        if(null == eSession || eSession.isExpired()) {
            return false;
        }
        SecurityContext securityContext = readSecurityContextFromSession(eSession);
        if(null == securityContext) {
            return false;
        }
        Authentication authentication = securityContext.getAuthentication();
        Object principal = authentication.getPrincipal();
        if(null == principal) {
            return false;
        }
        Channel loginChannel = SecurityUtils.getLoginChannelByPrincipal(principal);
        //同一类型终端
        if(currentUserLoginChannel.equals(loginChannel)) {
            if(logger.isDebugEnabled()) {
                logger.debug("expire sesion {}, login user type {}", sessionId, loginChannel);
            }
            return true;
        }
        return false;
    }
    
    public void expireNow(SessionInformation sessionInformation) {
        sessionInformation.expireNow();
        tokenService.revokeToken(null, sessionInformation.getSessionId());
    }
    
   /* @SuppressWarnings("unchecked")
    public String resolvePrincipal(Object principal) {
        if(JudgeUtils.isNull(principal)) return null;
        if(principal instanceof LemonUser) {
            return ((LemonUser<UserInfo>)principal).getUserInfo().getUserId();
        }
        return null;
    }*/
    
    public String resolvePrincipal(Object principal) {
        return EliminateSessionHelper.eliminateSessionDimension(principal);
    }
    
    public void setSpringSecurityContextKey(String springSecurityContextKey) {
        this.springSecurityContextKey =  springSecurityContextKey;
    }
    
    private SecurityContext readSecurityContextFromSession(ExpiringSession httpSession) {
        final boolean debug = logger.isDebugEnabled();

        if (httpSession == null) {
            if (debug) {
                logger.debug("No HttpSession currently exists");
            }

            return null;
        }

        // Session exists, so try to obtain a context from it.

        Object contextFromSession = httpSession.getAttribute(springSecurityContextKey);

        if (contextFromSession == null) {
            if (debug) {
                logger.debug("HttpSession returned null object for SPRING_SECURITY_CONTEXT");
            }

            return null;
        }

        // We now have the security context object from the session.
        if (!(contextFromSession instanceof SecurityContext)) {
            if (logger.isWarnEnabled()) {
                logger.warn(springSecurityContextKey
                        + " did not contain a SecurityContext but contained: '"
                        + contextFromSession
                        + "'; are you improperly modifying the HttpSession directly "
                        + "(you should always use SecurityContextHolder) or using the HttpSession attribute "
                        + "reserved for this class?");
            }

            return null;
        }

        if (debug) {
            logger.debug("Obtained a valid SecurityContext from "
                    + springSecurityContextKey + ": '" + contextFromSession + "'");
        }

        // Everything OK. The only non-null return from this method.

        return (SecurityContext) contextFromSession;
    }
}
