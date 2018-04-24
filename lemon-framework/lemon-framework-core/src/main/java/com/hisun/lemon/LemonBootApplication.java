package com.hisun.lemon;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

import com.hisun.lemon.framework.springcloud.fegin.LemonFeignClientsConfiguration;

/**
 * 
 * @author yuzhou
 * @date 2017年10月23日
 * @time 上午12:53:19
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(defaultConfiguration=LemonFeignClientsConfiguration.class)
public @interface LemonBootApplication {

}
