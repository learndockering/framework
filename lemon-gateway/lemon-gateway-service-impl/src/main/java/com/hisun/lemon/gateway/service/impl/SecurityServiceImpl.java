package com.hisun.lemon.gateway.service.impl;

import com.hisun.lemon.framework.cache.jcache.JCacheCacheable;
import com.hisun.lemon.gateway.bo.SecurityBO;
import com.hisun.lemon.gateway.service.ISecurityService;

/**
 * 
 * @author yuzhou
 * @date 2017年8月5日
 * @time 上午11:28:20
 *
 */
public class SecurityServiceImpl implements ISecurityService {

    @Override
    @JCacheCacheable(value="lemonSecure", unless = "#result == null")
    public SecurityBO querySecurity(String secureIndex) {
        SecurityBO secureBO =  null;
        return secureBO;
    }

    public SecurityBO queryMerchantSecurity(String merchantNo, String interfaceName, String interfaceVersion) {
        return null;
    }

}
