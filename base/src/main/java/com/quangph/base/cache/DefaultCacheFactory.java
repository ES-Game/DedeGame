package com.quangph.base.cache;

public class DefaultCacheFactory<T> implements ICacheFactory<T> {
    @Override
    public ICache<String, T> createCache(String name, int maxSize) {
        return new LruCacheImpl<T>(maxSize);
    }
}
