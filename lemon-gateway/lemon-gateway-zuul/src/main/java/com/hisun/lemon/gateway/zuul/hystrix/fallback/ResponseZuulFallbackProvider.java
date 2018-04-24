package com.hisun.lemon.gateway.zuul.hystrix.fallback;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.cloud.netflix.zuul.filters.route.ZuulFallbackProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;

import com.hisun.lemon.common.exception.ErrorMsgCode;
import com.hisun.lemon.gateway.common.response.ResponseMessageResolver;

/**
 * @author yuzhou
 * @date 2017年9月8日
 * @time 下午9:10:53
 *
 */
public class ResponseZuulFallbackProvider implements ZuulFallbackProvider {

    private ResponseMessageResolver responseMessageResolver;
    
    public ResponseZuulFallbackProvider(ResponseMessageResolver responseMessageResolver) {
        this.responseMessageResolver = responseMessageResolver;
    }
    @Override
    public String getRoute() {
        return null;
    }

    @Override
    public ClientHttpResponse fallbackResponse() {
        return new ClientHttpResponse() {

            @Override
            public InputStream getBody() throws IOException {
                byte[] bytes = responseMessageResolver.generateBytes(ErrorMsgCode.SERVER_NOT_AVAILABLE);
                return new ByteArrayInputStream(bytes);
            }

            @Override
            public HttpHeaders getHeaders() {
                return new HttpHeaders();
            }

            @Override
            public HttpStatus getStatusCode() throws IOException {
                return HttpStatus.OK;
            }

            @Override
            public int getRawStatusCode() throws IOException {
                return 200;
            }

            @Override
            public String getStatusText() throws IOException {
                return "Server not available.";
            }

            @Override
            public void close() {
                
            }
            
        };
    }

}
