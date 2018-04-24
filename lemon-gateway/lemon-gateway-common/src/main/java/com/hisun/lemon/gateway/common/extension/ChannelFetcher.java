package com.hisun.lemon.gateway.common.extension;

import com.hisun.lemon.common.extension.SPI;

/**
 * channel fetcher
 * @author yuzhou
 * @date 2017年9月25日
 * @time 上午10:40:36
 *
 */
@SPI
public interface ChannelFetcher {
    String fetchChannel();
}
