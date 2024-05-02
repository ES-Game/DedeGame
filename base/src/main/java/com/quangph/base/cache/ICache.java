package com.quangph.base.cache;

import java.util.Collection;
import java.util.Set;

public interface ICache<String, T> {
    void put(String key, T data);
    T get(String key);
    void remove(String key);
    void update(String key, T data);
    void removeAll();
    void trimToSize(int size);
    int getSize();
    int sizeOf(String key, T item);
    Collection<T> getAllValues();
    Set<String> getAllKeys();
}
