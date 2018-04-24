package com.hisun.lemon.gateway.service;

import com.hisun.lemon.gateway.dto.AppInfoDTO;

/**
 * @author yuzhou
 * @date 2017年8月14日
 * @time 上午11:32:56
 *
 */
public interface IAppInfoService {
    AppInfoDTO logAppInfo(AppInfoDTO appInfoBO);
}
