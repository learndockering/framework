package com.hisun.lemon.gateway.service;

import com.hisun.lemon.gateway.bo.SecurityBO;

/**
 * 安全相关Service
 * @author yuzhou
 * @date 2017年8月5日
 * @time 上午11:25:28
 *
 */
public interface ISecurityService {
    /**
     * 查询安全相关信息
     * @param secureIndex
     * @return
     */
    SecurityBO querySecurity(String secureIndex);

}
