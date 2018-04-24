package com.hisun.lemon.session;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.RedisFlushMode;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.http.HttpSessionStrategy;

import com.hisun.lemon.common.LemonConstants;

/**
 * @author yuzhou
 * @date 2017年7月27日
 * @time 下午4:14:29
 *
 */
@Configuration
@EnableRedisHttpSession(maxInactiveIntervalInSeconds=1800, redisNamespace="LMS", redisFlushMode = RedisFlushMode.ON_SAVE)
public class SessionConfiguration {
    public static final String AUTH_HEAD_NAME = LemonConstants.HTTP_HEADER_TOKEN;
    
    @Bean
    @ConditionalOnProperty(value="session.strategy.cookieAndHeader", matchIfMissing=true)
    public HttpSessionStrategy cookieAndHeaderHttpSessionStrategy() {
        return new CookieAndHeaderHttpSessionStrategy();
    }
}
