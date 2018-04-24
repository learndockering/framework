package com.hisun.lemon;

import com.hisun.lemon.common.LemonFramework;

/**
 * spring boot 启动
 * @author yuzhou
 * @date 2017年6月6日
 * @time 上午9:47:21
 *
 */

@LemonBootApplication
public class Application {
    
    public static void main(String[] args) {
        LemonFramework.processApplicationArgs(Application.class, args);
    }
}
