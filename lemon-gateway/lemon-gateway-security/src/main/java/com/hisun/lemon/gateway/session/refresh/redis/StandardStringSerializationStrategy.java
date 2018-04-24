package com.hisun.lemon.gateway.session.refresh.redis;

import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author yuzhou
 * @date 2017年9月13日
 * @time 下午12:30:17
 *
 */
public abstract class StandardStringSerializationStrategy extends BaseRedisTokenStoreSerializationStrategy {

    private static final StringRedisSerializer STRING_SERIALIZER = new StringRedisSerializer();

    @Override
    protected String deserializeStringInternal(byte[] bytes) {
        return STRING_SERIALIZER.deserialize(bytes);
    }

    @Override
    protected byte[] serializeInternal(String string) {
        return STRING_SERIALIZER.serialize(string);
    }

}
