package com.hisun.lemon.framework.i18n;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * 更改线程上下文中的Locale
 * @author yuzhou
 * @date 2017年6月26日
 * @time 下午2:47:35
 *
 */
public class LocaleChangeInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        return true;
    }
}
