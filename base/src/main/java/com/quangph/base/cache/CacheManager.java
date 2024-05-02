package com.quangph.base.cache;

import android.content.ComponentCallbacks2;
import android.content.res.Configuration;
import android.util.LruCache;

import androidx.annotation.NonNull;

import java.util.Map;

public class CacheManager implements ComponentCallbacks2 {

    private LruCache<String, ICache> mCacheStore = new LruCache<>(Integer.MAX_VALUE);

    private static CacheManager sInstance;

    public static CacheManager getInstance() {
        if (sInstance == null) {
            synchronized (CacheManager.class) {
                if (sInstance == null) {
                    sInstance = new CacheManager();
                }
            }
        }
        return sInstance;
    }

    @Override
    public void onTrimMemory(int level) {
        if (level >= TRIM_MEMORY_MODERATE) {
            Map<String, ICache> snapshot = mCacheStore.snapshot();
            for (String id : snapshot.keySet()) {
                ICache cache = mCacheStore.get(id);
                cache.removeAll();
            }
        }
        else if (level >= TRIM_MEMORY_BACKGROUND) {
            Map<String, ICache> snapshot = mCacheStore.snapshot();
            for (String id : snapshot.keySet()) {
                ICache cache = mCacheStore.get(id);
                cache.trimToSize(cache.getSize() / 2);
            }
        }
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {

    }

    @Override
    public void onLowMemory() {

    }

    public boolean hasCache(String cacheName) {
        return mCacheStore.get(cacheName) != null;
    }

    public<T> ICache<String, T> createCache(String cacheName, int maxSize, ICacheFactory<T> factory) {
        if (hasCache(cacheName)) {
            return mCacheStore.get(cacheName);
        }
        ICache<String, T> cache = factory.createCache(cacheName, maxSize);
        mCacheStore.put(cacheName, cache);
        return cache;
    }

    public ICache getCache(String cacheName) {
        return mCacheStore.get(cacheName);
    }
}
