package com.hisun.lemon.gateway.merchant;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.hisun.lemon.gateway.common.response.ResponseMessageResolver;
import com.hisun.lemon.gateway.common.signature.SignatureResolver;
import com.hisun.lemon.gateway.merchant.signature.MerchantSignatureResolver;
import com.hisun.lemon.gateway.zuul.ZuulHelper;
import com.hisun.lemon.gateway.zuul.filter.SignatureZuulFilter;

/**
 * @author yuzhou
 * @date 2017年10月12日
 * @time 下午12:22:34
 *
 */
@Configuration
public class MerchantGatewayConfiguration {
    
    @Bean
    public SignatureResolver merchantSignatureResolver(ZuulHelper zuulHelper) {
        return new MerchantSignatureResolver(zuulHelper);
    }
    
    @Bean
    public SignatureZuulFilter merchantSignatureZuulFilter(SignatureResolver merchantSignatureResolver, ResponseMessageResolver responseMessageResolver) {
        SignatureZuulFilter merchantSignatureZuulFilter = new SignatureZuulFilter(merchantSignatureResolver, responseMessageResolver) {
            @Override
            public int filterOrder() {
                return SIGNATURE_ORDER - 1;
            }
        };
        return merchantSignatureZuulFilter;
    }
}
