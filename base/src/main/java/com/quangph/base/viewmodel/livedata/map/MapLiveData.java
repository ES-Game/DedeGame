package com.quangph.base.viewmodel.livedata.map;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by QuangPH on 2020-11-17.
 */
public class MapLiveData<K, V, MAP extends Map<K, V>> extends MutableLiveData<MAP> {

    private List<MapLiveDataObserverInfo> mObserverList = new ArrayList<>();

    @Override
    public void observe(@NonNull LifecycleOwner owner, @NonNull Observer<? super MAP> observer) {
        super.observe(owner, observer);
        if (observer instanceof IMapLiveDataObserver) {
            mObserverList.add(new MapLiveDataObserverInfo((IMapLiveDataObserver) observer, owner));
        }
    }

    @Override
    public void removeObserver(@NonNull Observer<? super MAP> observer) {
        super.removeObserver(observer);
        synchronized (mObserverList) {
            Iterator<MapLiveDataObserverInfo> iterator = mObserverList.iterator();
            while (iterator.hasNext()) {
                MapLiveDataObserverInfo next = iterator.next();
                if (next.observer.equals(observer)) {
                    iterator.remove();
                }
            }
        }
    }

    @Override
    public void removeObservers(@NonNull LifecycleOwner owner) {
        super.removeObservers(owner);
        synchronized (mObserverList) {
            Iterator<MapLiveDataObserverInfo> iterator = mObserverList.iterator();
            while (iterator.hasNext()) {
                MapLiveDataObserverInfo next = iterator.next();
                if (next.owner.equals(owner)) {
                    iterator.remove();
                }
            }
        }
    }

    public void put(K key, V value) {
        MAP map = getValue();
        if (map != null) {
            map.put(key, value);
            for (MapLiveDataObserverInfo info : mObserverList) {
                info.observer.onPut(key, value);
            }
        }
    }

    public void remove(K key) {
        MAP map = getValue();
        if (map != null) {
            map.remove(key);
            for (MapLiveDataObserverInfo info : mObserverList) {
                info.observer.onRemove(key);
            }
        }
    }
}
