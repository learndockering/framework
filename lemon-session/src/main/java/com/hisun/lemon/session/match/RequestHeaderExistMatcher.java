package com.hisun.lemon.session.match;

import javax.servlet.http.HttpServletRequest;

public class RequestHeaderExistMatcher implements RequestMatcher {
    private final String expectedHeaderName;
    
    public RequestHeaderExistMatcher(String expectedHeaderName) {
        this.expectedHeaderName = expectedHeaderName;
    }
    @Override
    public boolean matches(HttpServletRequest request) {
        String actualHeaderValue = request.getHeader(expectedHeaderName);
        if(null == actualHeaderValue) {
            return false;
        }
        return true;
    }

}
