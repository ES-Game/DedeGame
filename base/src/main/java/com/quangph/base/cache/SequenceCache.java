package com.quangph.base.cache;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

/**
 * When trim to size, the last element will be remove first
 * @param <T>
 */
public class SequenceCache<T> implements ICache<String, T> {

    private Map<String, T> mCache = new HashMap<>();
    private Stack<String> mKeyStacks = new Stack<>();

    @Override
    public void put(String key, T data) {
        if (mKeyStacks.contains(key)) {
            return;
        } else {
            mCache.put(key, data);
            mKeyStacks.push(key);
        }
    }

    @Override
    public T get(String key) {
        return mCache.get(key);
    }

    @Override
    public void remove(String key) {
        mCache.remove(key);
        mKeyStacks.remove(key);
    }

    @Override
    public void update(String key, T data) {
        mCache.remove(key);
        mCache.put(key, data);
    }

    @Override
    public void removeAll() {
        mCache.clear();
        mKeyStacks.clear();
    }

    @Override
    public void trimToSize(int size) {
        if (mKeyStacks.size() <= size) {
            return;
        }

        int totalSize = mKeyStacks.size();
        while (totalSize > size) {
            String topKey = mKeyStacks.pop();
            T remove = mCache.remove(topKey);
            totalSize -= sizeOf(topKey, remove);
        }
    }

    @Override
    public int getSize() {
        return mKeyStacks.size();
    }

    @Override
    public int sizeOf(String key, T item) {
        return 1;
    }

    @Override
    public Collection<T> getAllValues() {
        return mCache.values();
    }

    @Override
    public Set<String> getAllKeys() {
        return mCache.keySet();
    }
}
