package com.hisun.lemon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

import com.hisun.lemon.common.LemonFramework;
import com.hisun.lemon.framework.springcloud.fegin.LemonFeignClientsConfiguration;
import com.hisun.lemon.gateway.feign.GatewayFeignClientConfiguration;

/**
 * API gateway application
 * 
 * @author yuzhou
 * @date 2017年7月21日
 * @time 下午2:40:59
 *
 */
@SpringBootApplication
@EnableFeignClients(defaultConfiguration={GatewayFeignClientConfiguration.class, LemonFeignClientsConfiguration.class})
public class GatewayApplication {
    
    public static void main(String[] args) {
        LemonFramework.processApplicationArgs(args);
        SpringApplication.run(GatewayApplication.class, args);
    }
    
}
