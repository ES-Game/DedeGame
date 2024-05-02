package com.quangph.base.viewmodel.livedata.map;

import androidx.lifecycle.Observer;

import java.util.Map;

/**
 * Created by QuangPH on 2020-11-17.
 */
public interface IMapLiveDataObserver<K, V, MAP extends Map<K, V>> extends Observer<MAP> {
    void onPut(K key, V value);
    void onRemove(K key);
}
