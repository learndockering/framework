package com.hisun.lemon.demo3.clientinterface;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * fegin 调用PRD服务的客户端接口
 *
 * @author yuzhou
 * @date 2017年6月5日
 * @time 上午10:59:54
 *
 */
@FeignClient("PRD")
public interface ComputeClient {
    @RequestMapping(method = RequestMethod.GET, value = "/add")
    Integer add(@RequestParam(value = "a") Integer a, @RequestParam(value = "b") Integer b);
}
