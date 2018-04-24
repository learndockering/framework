package com.hisun.lemon.gateway.common.signature;


/**
 * @author yuzhou
 * @date 2017年9月11日
 * @time 下午7:23:04
 *
 */
public interface SignatureResolver {
    
    public boolean shouldVerify();
    
    public boolean verify();
}
