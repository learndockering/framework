package com.hisun.lemon.gateway.zuul.filter;

import com.netflix.zuul.ZuulFilter;

/**
 * @author yuzhou
 * @date 2017年8月9日
 * @time 下午2:10:33
 *
 */
public class LemonPostZuulFilter extends ZuulFilter {

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String filterType() {
        return FilterType.POST.lowerCaseName();
    }

    @Override
    public int filterOrder() {
        return 0;
    }

}
