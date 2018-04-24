package com.hisun.lemon.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.context.annotation.Configuration;

import com.hisun.lemon.common.LemonFramework;

/**
 * eureka服务注册中心
 * 
 * @author yuzhou
 * @date 2017年6月2日
 * @time 上午11:37:16
 *
 */
@SpringBootApplication
@Configuration
@EnableEurekaServer
public class EurekaServerApplication {

    public static void main(String[] args) {
        LemonFramework.processApplicationArgs(args);
        SpringApplication.run(EurekaServerApplication.class, args);
    }
}
