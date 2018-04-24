package com.hisun.lemon.framework.redis.serializer;

import java.nio.charset.Charset;

import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.util.Assert;

/**
 * redis serializer for Long type
 * 
 * @author yuzhou
 * @date 2017年7月6日
 * @time 上午10:03:06
 *
 */
public class LongRedisSerializer implements RedisSerializer<Long> {

    private final Charset charset;

    public LongRedisSerializer() {
        this(Charset.forName("UTF8"));
    }

    public LongRedisSerializer(Charset charset) {
        Assert.notNull(charset, "Charset must not be null!");
        this.charset = charset;
    }
    @Override
    public byte[] serialize(Long t) throws SerializationException {
        return String.valueOf(t).getBytes(charset);
    }

    @Override
    public Long deserialize(byte[] bytes) throws SerializationException {
        return Long.valueOf(new String(bytes, charset));
    }

}
