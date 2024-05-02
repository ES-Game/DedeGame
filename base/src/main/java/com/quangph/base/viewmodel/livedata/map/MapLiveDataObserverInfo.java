package com.quangph.base.viewmodel.livedata.map;

import androidx.lifecycle.LifecycleOwner;


/**
 * Created by QuangPH on 2020-11-17.
 */
public class MapLiveDataObserverInfo {
    public IMapLiveDataObserver observer;
    public LifecycleOwner owner;

    public MapLiveDataObserverInfo(IMapLiveDataObserver observer, LifecycleOwner owner) {
        this.observer = observer;
        this.owner = owner;
    }
}
