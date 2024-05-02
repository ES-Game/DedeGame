package com.quangph.base.cache;

import android.util.LruCache;

import java.util.Collection;
import java.util.Set;

public class LruCacheImpl<T> implements ICache<String, T> {

    private LruCache<String, T> mCache;

    public LruCacheImpl(int maxSize) {
        mCache = new LruCache<String, T>(maxSize);
    }

    @Override
    public void put(String key, T data) {
        mCache.put(key, data);
    }

    @Override
    public T get(String key) {
        return mCache.get(key);
    }

    @Override
    public void remove(String key) {
        mCache.remove(key);
    }

    @Override
    public void update(String key, T data) {
        mCache.remove(key);
        mCache.put(key, data);
    }

    @Override
    public void removeAll() {
        mCache.evictAll();
    }

    @Override
    public void trimToSize(int size) {
        mCache.trimToSize(size);
    }

    @Override
    public int getSize() {
        return mCache.size();
    }

    @Override
    public int sizeOf(String key, T item) {
        return 1;
    }

    @Override
    public Collection<T> getAllValues() {
        return mCache.snapshot().values();
    }

    @Override
    public Set<String> getAllKeys() {
        return mCache.snapshot().keySet();
    }
}
