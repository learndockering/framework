package com.hisun.lemon.common;

/**
 * @author yuzhou
 * @date 2017年7月11日
 * @time 下午5:51:27
 *
 * @param <T>
 */
@FunctionalInterface
public interface Callback<T> {
    public T callback();
}
