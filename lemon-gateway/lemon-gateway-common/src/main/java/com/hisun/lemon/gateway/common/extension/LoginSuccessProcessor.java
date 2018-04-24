package com.hisun.lemon.gateway.common.extension;

import com.hisun.lemon.common.extension.SPI;

/**
 * 登录成功操作
 * @author yuzhou
 * @date 2017年9月26日
 * @time 下午2:23:45
 *
 */
@SPI
public interface LoginSuccessProcessor {
    void process();
}
