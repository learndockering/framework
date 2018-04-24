package com.hisun.lemon.framework.idgenerate.auto;

/**
 * Id生成策略
 * @author yuzhou
 * @date 2017年6月19日
 * @time 下午5:28:16
 *
 */
public interface IdGenStrategy {
    /**
     * 生成Id
     * @param key redis key
     * @param prefix ID前缀
     * @return
     */
    String genId(String key, String prefix);
}
