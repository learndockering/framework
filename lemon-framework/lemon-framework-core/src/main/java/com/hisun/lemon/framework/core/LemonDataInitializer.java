package com.hisun.lemon.framework.core;

import com.hisun.lemon.common.extension.SPI;

/**
 * @author yuzhou
 * @date 2017年9月8日
 * @time 下午2:19:48
 *
 */
@SPI
public interface LemonDataInitializer {
    public void initLemonData();
}
