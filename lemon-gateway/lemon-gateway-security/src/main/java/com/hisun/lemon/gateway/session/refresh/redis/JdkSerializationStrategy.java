package com.hisun.lemon.gateway.session.refresh.redis;

import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;

/**
 * @author yuzhou
 * @date 2017年9月13日
 * @time 下午12:31:46
 *
 */
public class JdkSerializationStrategy extends StandardStringSerializationStrategy {

    private static final JdkSerializationRedisSerializer OBJECT_SERIALIZER = new JdkSerializationRedisSerializer();

    @Override
    @SuppressWarnings("unchecked")
    protected <T> T deserializeInternal(byte[] bytes, Class<T> clazz) {
        return (T) OBJECT_SERIALIZER.deserialize(bytes);
    }

    @Override
    protected byte[] serializeInternal(Object object) {
        return OBJECT_SERIALIZER.serialize(object);
    }


}
