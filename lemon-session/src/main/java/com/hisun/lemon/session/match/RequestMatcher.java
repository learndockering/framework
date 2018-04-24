package com.hisun.lemon.session.match;

import javax.servlet.http.HttpServletRequest;

/**
 * 
 * @author yuzhou
 * @date 2017年7月27日
 * @time 下午3:47:31
 *
 */
public interface RequestMatcher {
    /**
     * @param request
     * @return
     */
    boolean matches(HttpServletRequest request);
}
