package com.hisun.lemon.gateway.session.refresh.redis;

/**
 * @author yuzhou
 * @date 2017年9月13日
 * @time 下午12:27:48
 *
 */
public interface RedisTokenStoreSerializationStrategy {
    <T> T deserialize(byte[] bytes, Class<T> clazz);

    String deserializeString(byte[] bytes);

    byte[] serialize(Object object);

    byte[] serialize(String data);
}
