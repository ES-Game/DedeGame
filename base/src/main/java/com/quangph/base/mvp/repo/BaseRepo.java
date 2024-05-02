package com.quangph.base.mvp.repo;

import android.util.Log;

import com.quangph.base.cache.CacheManager;
import com.quangph.base.cache.DefaultCacheFactory;
import com.quangph.base.cache.ICache;

public class BaseRepo implements IRepo {

    public<T> CacheBuilder buildCache(String cacheName) {
        return new CacheBuilder<T>().name(cacheName);
    }

    public<T> ICache<String, T> getCache(String cacheName) {
        return CacheManager.getInstance().getCache(cacheName);
    }


    public static class CacheBuilder<T> {
        private String name;
        private int maxSize = Integer.MAX_VALUE;

        public CacheBuilder name(String cacheName) {
            this.name = cacheName;
            return this;
        }

        public CacheBuilder maxSize(int maxSize) {
            this.maxSize = maxSize;
            return this;
        }

        public ICache<String, T> build() {
            CacheManager manager = CacheManager.getInstance();
            if (manager.hasCache(name)) {
                Log.e("BaseRepo", "has cache with name: " + name);
            } else {
                return manager.createCache(name, maxSize, new DefaultCacheFactory<T>());
            }
            return null;
        }
    }
}
