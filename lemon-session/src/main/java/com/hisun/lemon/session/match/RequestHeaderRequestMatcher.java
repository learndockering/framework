package com.hisun.lemon.session.match;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.Assert;

/**
 * @author yuzhou
 * @date 2017年7月27日
 * @time 下午3:49:23
 *
 */
public class RequestHeaderRequestMatcher implements RequestMatcher {
    private final String expectedHeaderName;
    private final String expectedHeaderValue;

    public RequestHeaderRequestMatcher(String expectedHeaderName) {
        this(expectedHeaderName, null);
    }

    public RequestHeaderRequestMatcher(String expectedHeaderName,
            String expectedHeaderValue) {
        Assert.notNull(expectedHeaderName, "headerName cannot be null");
        this.expectedHeaderName = expectedHeaderName;
        this.expectedHeaderValue = expectedHeaderValue;
    }

    public boolean matches(HttpServletRequest request) {
        String actualHeaderValue = request.getHeader(expectedHeaderName);
        if (expectedHeaderValue == null) {
            return actualHeaderValue != null;
        }

        return expectedHeaderValue.equals(actualHeaderValue);
    }

    @Override
    public String toString() {
        return "RequestHeaderRequestMatcher [expectedHeaderName=" + expectedHeaderName
                + ", expectedHeaderValue=" + expectedHeaderValue + "]";
    }

}
