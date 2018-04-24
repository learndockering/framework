package com.hisun.lemon.gateway.common.extension;

import com.hisun.lemon.common.extension.SPI;

/**
 * 网关交易初始化操作
 * @author yuzhou
 * @date 2017年9月22日
 * @time 下午5:02:39
 *
 */
@SPI
public interface GatewayTransactionInitializer {
    void init();
}
