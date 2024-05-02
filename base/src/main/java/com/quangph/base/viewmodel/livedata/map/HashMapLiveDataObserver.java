package com.quangph.base.viewmodel.livedata.map;

import java.util.HashMap;

/**
 * Created by QuangPH on 2020-11-19.
 */
public class HashMapLiveDataObserver<K, V> implements IMapLiveDataObserver<K, V, HashMap<K, V>> {
    @Override
    public void onPut(K key, V value) {

    }

    @Override
    public void onRemove(K key) {

    }

    @Override
    public void onChanged(HashMap<K, V> kvHashMap) {

    }
}
