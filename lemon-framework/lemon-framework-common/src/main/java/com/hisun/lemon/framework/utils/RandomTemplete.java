package com.hisun.lemon.framework.utils;

import com.hisun.lemon.common.utils.RandomUtils;

/**
 * 随机数操作模版
 * @author yuzhou
 * @date 2017年9月8日
 * @time 下午4:58:57
 *
 */
public interface RandomTemplete {
    /**
     * 申请随机数
     * @param key       申请随机数对应的key
     * @param leaseTime 自动失效时间,单位毫秒
     * @param randomType
     * @param length
     * @return
     */
    String apply(String key, int leaseTime, RandomType randomType, int length);
    
    /**
     * 获取随机数，只能获取一次
     * @param key
     * @return
     */
    String acquireOnce(String key);
    
    /**
     * 验证一次失效
     * @param key
     * @param random
     * @return
     */
    boolean validateOnce(String key, String random);
    
    enum RandomType {
        NUMERIC, LETTER, ASCII, NUMERIC_LETTER
    }
    
    default String genRandom(RandomType randomType, int length) {
        String random = null;
        switch (randomType) {
        case NUMERIC:
            random = RandomUtils.randomNumeric(length);
            break;
        case LETTER:
            random = RandomUtils.randomLetterFixLength(length);
            break;
        case ASCII:
            random = RandomUtils.randomAsciiFixLength(length);
            break;
        case NUMERIC_LETTER:
            random = RandomUtils.randomStringFixLength(length);
            break;
        default:
            break;
        }
        return random;
    }
}
