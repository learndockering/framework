package com.hisun.lemon.framework.springcloud.fegin.httpclient;

import java.util.concurrent.TimeUnit;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import com.hisun.lemon.common.utils.JudgeUtils;

import feign.httpclient.ApacheHttpClient;

/**
 * feign httpclient auto configuration
 * 
 * @author yuzhou
 * @date 2017年7月5日
 * @time 下午2:47:59
 *
 */
@Configuration
@ConditionalOnClass(ApacheHttpClient.class)
@ConditionalOnProperty(value = "feign.httpclient.enabled", matchIfMissing = true)
public class FeignHttpclientConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(FeignHttpclientConfiguration.class);

    @Autowired
    private FeignHttpclientProperties feignHttpclientProperties;
    
    @Bean
    @ConfigurationProperties(prefix="feign.httpclient")
    public FeignHttpclientProperties feignHttpClientProperties() {
        return new FeignHttpclientProperties();
    }

    @Bean
    public PoolingHttpClientConnectionManager poolingHttpClientConnectionManager() {
        PoolingHttpClientConnectionManager cm;
        LayeredConnectionSocketFactory sslsf = SSLConnectionSocketFactory.getSocketFactory();
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("https", sslsf)
                .register("http", PlainConnectionSocketFactory.getSocketFactory())
                .build();
        cm = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        cm.setMaxTotal(this.feignHttpclientProperties.getMaxTotal());
        cm.setDefaultMaxPerRoute(this.feignHttpclientProperties.getMaxPerRoute());
        return cm;
    }

    @Bean
    public HttpClient httpClient(PoolingHttpClientConnectionManager poolingHttpClientConnectionManager) {
        CloseableHttpClient httpClient = HttpClients.custom()
            .setConnectionManager(poolingHttpClientConnectionManager)
            .setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy() {
                @Override
                public long getKeepAliveDuration(HttpResponse response,HttpContext context) {
                    long alive = super.getKeepAliveDuration(response, context);
                    if (alive == -1) {
                        // not set keepalive ,use this value
                        alive = FeignHttpclientConfiguration.this.feignHttpclientProperties.getAlive();
                    }
                    return alive;
                }
            }).build();
        if (logger.isInfoEnabled()) {
            logger.info("Creating shared instance of singleton bean \"httpClient\".");
        }
        return httpClient;
    }
    
    @Autowired 
    private PoolingHttpClientConnectionManager poolingHttpClientConnectionManager;
    
    @Scheduled(fixedRateString = "${feign.httpclient.closedConnectionsRate}", initialDelay = 30000)
    public void closedExpiredAndIdleConnections() {
        if(JudgeUtils.isNotNull(poolingHttpClientConnectionManager)) {
            poolingHttpClientConnectionManager.closeExpiredConnections();
            poolingHttpClientConnectionManager.closeIdleConnections(feignHttpclientProperties.getIdleTimeoutMillis(), TimeUnit.MILLISECONDS);
            if(logger.isDebugEnabled()) {
                logger.debug("closed expired and idle connections from http client pool.");
            }
        }
    }
}
