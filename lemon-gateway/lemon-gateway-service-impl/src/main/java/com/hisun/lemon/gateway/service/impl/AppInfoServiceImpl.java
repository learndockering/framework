package com.hisun.lemon.gateway.service.impl;

import com.hisun.lemon.framework.stream.MultiOutput;
import com.hisun.lemon.framework.stream.producer.Producer;
import com.hisun.lemon.framework.stream.producer.Producers;
import com.hisun.lemon.gateway.dto.AppInfoDTO;
import com.hisun.lemon.gateway.service.IAppInfoService;

/**
 * @author yuzhou
 * @date 2017年8月14日
 * @time 上午11:33:35
 *
 */
public class AppInfoServiceImpl implements IAppInfoService {

    @Override
    @Producers({@Producer(beanName="logAppInfoHandler", channelName=MultiOutput.OUTPUT_DEFAULT)})
    public AppInfoDTO logAppInfo(AppInfoDTO appInfoBO) {
        return appInfoBO;
    }

}
