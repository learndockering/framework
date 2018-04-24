package com.hisun.lemon.gateway.session.refresh.redis;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import com.hisun.lemon.common.utils.DateTimeUtils;
import com.hisun.lemon.common.utils.JudgeUtils;
import com.hisun.lemon.gateway.session.refresh.AccessToken;
import com.hisun.lemon.gateway.session.refresh.LemonAccessToken;
import com.hisun.lemon.gateway.session.refresh.LemonRefreshToken;
import com.hisun.lemon.gateway.session.refresh.RefreshToken;
import com.hisun.lemon.gateway.session.refresh.TokenStore;

/**
 * @author yuzhou
 * @date 2017年9月13日
 * @time 下午12:03:04
 *
 */
public class RedisTokenStore implements TokenStore {
    private static final String REFRESH = "refresh:";
    private static final String ACCESS_TO_REFRESH = "access_to_refresh:";
    private static final String REFRESH_TO_ACCESS = "refresh_to_access:";
    
    private final RedisConnectionFactory connectionFactory;
    private RedisTokenStoreSerializationStrategy serializationStrategy = new JdkSerializationStrategy();
    
    private String prefix = "LMS:";

    public RedisTokenStore(RedisConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }
    
    public void setSerializationStrategy(RedisTokenStoreSerializationStrategy serializationStrategy) {
        this.serializationStrategy = serializationStrategy;
    }
    
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    private RedisConnection getConnection() {
        return connectionFactory.getConnection();
    }

    private byte[] serialize(Object object) {
        return serializationStrategy.serialize(object);
    }

    private byte[] serializeKey(String object) {
        return serialize(prefix + object);
    }
    
    private byte[] serialize(String string) {
        return serializationStrategy.serialize(string);
    }

    private String deserializeString(byte[] bytes) {
        return serializationStrategy.deserializeString(bytes);
    }

    @Override
    public void storeRefreshToken(RefreshToken refreshToken) {
        byte[] refreshKey = serializeKey(REFRESH + refreshToken.getValue());
        byte[] serializedRefreshToken = serialize(refreshToken);
        RedisConnection conn = getConnection();
        try {
            conn.openPipeline();
            conn.set(refreshKey, serializedRefreshToken);
            if (refreshToken instanceof LemonRefreshToken) {
                LemonRefreshToken expiringRefreshToken = (LemonRefreshToken) refreshToken;
                LocalDateTime expiration = expiringRefreshToken.getExpiration();
                if (expiration != null) {
                    int seconds = Long.valueOf((DateTimeUtils.toEpochMilli(expiration) - System.currentTimeMillis()) / 1000L)
                            .intValue();
                    conn.expire(refreshKey, seconds);
                }
            }
            conn.closePipeline();
        } finally {
            conn.close();
        }
    }

    @Override
    public void removeRefreshToken(RefreshToken refreshToken) {
        removeRefreshToken(refreshToken.getValue());
    }
    
    public void removeRefreshToken(String tokenValue) {
        byte[] refreshKey = serializeKey(REFRESH + tokenValue);
        byte[] refresh2AccessKey = serializeKey(REFRESH_TO_ACCESS + tokenValue);
        byte[] access2RefreshKey = serializeKey(ACCESS_TO_REFRESH + tokenValue);
        RedisConnection conn = getConnection();
        try {
            conn.openPipeline();
            conn.del(refreshKey);
            conn.del(refresh2AccessKey);
            conn.del(access2RefreshKey);
            conn.closePipeline();
        } finally {
            conn.close();
        }
    }

    @Override
    public void storeAccessToken(AccessToken token) {
        RedisConnection conn = getConnection();
        try {
            conn.openPipeline();
            RefreshToken refreshToken = token.getRefreshToken();
            if (refreshToken != null && refreshToken.getValue() != null) {
                byte[] refresh = serialize(token.getRefreshToken().getValue());
                byte[] auth = serialize(token.getValue());
                byte[] refreshToAccessKey = serializeKey(REFRESH_TO_ACCESS + token.getRefreshToken().getValue());
                conn.set(refreshToAccessKey, auth);
                byte[] accessToRefreshKey = serializeKey(ACCESS_TO_REFRESH + token.getValue());
                conn.set(accessToRefreshKey, refresh);
                if (refreshToken instanceof LemonRefreshToken) {
                    LemonRefreshToken expiringRefreshToken = (LemonRefreshToken) refreshToken;
                    LocalDateTime expiration = expiringRefreshToken.getExpiration();
                    if (expiration != null) {
                        int seconds = Long.valueOf((DateTimeUtils.toEpochMilli(expiration) - System.currentTimeMillis()) / 1000L)
                                .intValue();
                        conn.expire(refreshToAccessKey, seconds);
                        conn.expire(accessToRefreshKey, seconds);
                    }
                }
            }
            conn.closePipeline();
        } finally {
            conn.close();
        }
    }
    
    @Override
    public RefreshToken readRefreshToken(String tokenValue) {
        byte[] key = serializeKey(REFRESH + tokenValue);
        byte[] bytes = null;
        RedisConnection conn = getConnection();
        try {
            bytes = conn.get(key);
        } finally {
            conn.close();
        }
        RefreshToken refreshToken = deserializeRefreshToken(bytes);
        return refreshToken;
    }
    
    private RefreshToken deserializeRefreshToken(byte[] bytes) {
        return serializationStrategy.deserialize(bytes, RefreshToken.class);
    }

    @Override
    public String removeAccessTokenUsingRefreshToken(String refreshTokenValue) {
        byte[] key = serializeKey(REFRESH_TO_ACCESS + refreshTokenValue);
        List<Object> results = null;
        RedisConnection conn = getConnection();
        try {
            conn.openPipeline();
            conn.get(key);
            conn.del(key);
            results = conn.closePipeline();
        } finally {
            conn.close();
        }
        if (results == null) {
            return "";
        }
        byte[] bytes = (byte[]) results.get(0);
        String accessToken = deserializeString(bytes);
        if (accessToken != null) {
            removeAccessToken(accessToken);
        }
        return accessToken;
    }
    
    public void removeAccessToken(String tokenValue) {
        byte[] accessToRefreshKey = serializeKey(ACCESS_TO_REFRESH + tokenValue);
        RedisConnection conn = getConnection();
        try {
            conn.openPipeline();
            conn.del(accessToRefreshKey);
            conn.closePipeline();
        } finally {
            conn.close();
        }
    }

    @Override
    public AccessToken readAccessToken(String tokenValue) {
        byte[] accessToRefreshKey = serializeKey(ACCESS_TO_REFRESH + tokenValue);
        String refreshTokenValue = null;
        RedisConnection conn = getConnection();
        try {
            //conn.openPipeline();
            byte[] refresh = conn.get(accessToRefreshKey);
            //List<Object> results = conn.closePipeline();
            //byte[] refresh = (byte[]) results.get(0);
            refreshTokenValue = deserializeString(refresh);
        } finally {
            conn.close();
        }
        LemonAccessToken accessToken = new LemonAccessToken(tokenValue);
        if(JudgeUtils.isNotBlank(refreshTokenValue)) {
            accessToken.setRefreshToken(readRefreshToken(refreshTokenValue));
        }
        return accessToken;
    }
}
