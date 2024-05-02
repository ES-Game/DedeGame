package com.quangph.base.viewmodel.livedata.collection;

import androidx.lifecycle.LifecycleOwner;

/**
 * Created by QuangPH on 2020-11-13.
 */
public class CollectionLiveDataObserverInfo {
    public ICollectionLiveDataObserver observer;
    public LifecycleOwner owner;

    public CollectionLiveDataObserverInfo(ICollectionLiveDataObserver observer, LifecycleOwner owner) {
        this.observer = observer;
        this.owner = owner;
    }
}
