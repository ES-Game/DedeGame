package com.quangph.base.cache;

public interface ICacheFactory<T> {
    ICache<String, T> createCache(String name, int maxSize);
}
