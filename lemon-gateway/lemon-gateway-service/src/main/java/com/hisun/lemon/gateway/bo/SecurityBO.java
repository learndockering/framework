package com.hisun.lemon.gateway.bo;

import com.hisun.lemon.common.utils.StringUtils;
import com.hisun.lemon.gateway.common.signature.Algorithm;

/**
 * secure
 * @author yuzhou
 * @date 2017年8月5日
 * @time 下午6:55:39
 *
 */
public class SecurityBO {
    private static final String DEFAULT_ALGORITHM = "MD5";
    private String secureIndex;
    private String secureKey;
    private Algorithm algorithm;
    private String channel;
    
    public SecurityBO(String secureIndex, String secureKey, String algorithm) {
        this.secureIndex = secureIndex;
        this.secureKey = secureKey;
        this.algorithm = Algorithm.valueOf(StringUtils.getDefaultIfEmpty(algorithm, DEFAULT_ALGORITHM));
    }
    
    public SecurityBO(String secureIndex, String secureKey, Algorithm algorithm) {
        this.secureIndex = secureIndex;
        this.secureKey = secureKey;
        this.algorithm = algorithm;
    }
    public String getSecureIndex() {
        return secureIndex;
    }
    public void setSecureIndex(String secureIndex) {
        this.secureIndex = secureIndex;
    }
    public String getSecureKey() {
        return secureKey;
    }
    public void setSecureKey(String secureKey) {
        this.secureKey = secureKey;
    }
    public Algorithm getAlgorithm() {
        return algorithm;
    }
    public void setAlgorithm(Algorithm algorithm) {
        this.algorithm = algorithm;
    }
    
    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }
    
}
