package com.hisun.lemon.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

/**
 * spring config server
 * 
 * @author yuzhou
 * @date 2017年6月2日
 * @time 上午10:26:00
 *
 */
@EnableConfigServer
@SpringBootApplication
public class ConfigServerApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(ConfigServerApplication.class, args);
    }
}
