package com.hisun.lemon.session;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.session.Session;
import org.springframework.session.web.http.CookieHttpSessionStrategy;
import org.springframework.session.web.http.HeaderHttpSessionStrategy;
import org.springframework.session.web.http.HttpSessionStrategy;

import com.hisun.lemon.session.match.RequestHeaderExistMatcher;
import com.hisun.lemon.session.match.RequestMatcher;

/**
 * 同时支持header和cookie
 * @author yuzhou
 * @date 2017年7月27日
 * @time 下午3:17:03
 *
 */
public class CookieAndHeaderHttpSessionStrategy implements HttpSessionStrategy {
    
    private HttpSessionStrategy cookieHttpSessionStrategy; 
    private HttpSessionStrategy headerHttpSessionStrategy; 
    private RequestMatcher headerStrategyMatch;
    
    public CookieAndHeaderHttpSessionStrategy() {
        this(new CookieHttpSessionStrategy(), new HeaderHttpSessionStrategy());
    }
    
    public CookieAndHeaderHttpSessionStrategy(HttpSessionStrategy cookieHttpSessionStrategy,
            HttpSessionStrategy headerHttpSessionStrategy) {
        this.cookieHttpSessionStrategy = cookieHttpSessionStrategy;
        this.headerHttpSessionStrategy = headerHttpSessionStrategy;
        if(this.headerHttpSessionStrategy instanceof HeaderHttpSessionStrategy) {
            ((HeaderHttpSessionStrategy)this.headerHttpSessionStrategy).setHeaderName(SessionConfiguration.AUTH_HEAD_NAME);
        }
        this.headerStrategyMatch = new RequestHeaderExistMatcher(SessionConfiguration.AUTH_HEAD_NAME);
    }
    @Override
    public String getRequestedSessionId(HttpServletRequest request) {
        return this.getStrategy(request).getRequestedSessionId(request);
    }
    @Override
    public void onNewSession(Session session, HttpServletRequest request,
            HttpServletResponse response) {
        this.getStrategy(request).onNewSession(session, request, response);
    }
    @Override
    public void onInvalidateSession(HttpServletRequest request,
            HttpServletResponse response) {
        this.getStrategy(request).onInvalidateSession(request, response);
    }
    
    /**
     * 获取恰当的策略
     * @param request
     * @return
     */
    public HttpSessionStrategy getStrategy(HttpServletRequest request) {
        return headerStrategyMatch.matches(request) ? this.headerHttpSessionStrategy : this.cookieHttpSessionStrategy;
    }
}
