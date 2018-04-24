package com.hisun.lemon.framework.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.hisun.lemon.framework.service.IMsgInfoProcessor;
import com.hisun.lemon.framework.service.IMsgInfoService;
import com.hisun.lemon.framework.service.impl.MsgInfoProcessor;

/**
 * @author yuzhou
 * @date 2017年9月5日
 * @time 下午4:32:10
 *
 */
@Configuration
public class MsgInfoConfiguration {
    
    @Bean
    public IMsgInfoProcessor msgInfoProcessor(IMsgInfoService msgInfoServiceImpl) {
        return new MsgInfoProcessor(msgInfoServiceImpl);
    }
}
