package com.quangph.base.viewmodel;

import androidx.lifecycle.Observer;

import com.quangph.base.viewmodel.livedata.collection.ICollectionLiveDataObserver;
import com.quangph.base.viewmodel.livedata.map.IMapLiveDataObserver;
import com.quangph.base.viewmodel.observer.DelayCollectionObserver;
import com.quangph.base.viewmodel.observer.DelayMapObserver;
import com.quangph.base.viewmodel.observer.DelayObserver;
import com.quangph.base.viewmodel.observer.IDelayObserver;

/**
 * Created by QuangPH on 2020-11-18.
 */
public class DefaultDelayObserverFactory extends DelayObserverFactory {

    @Override
    public IDelayObserver create(Observer observer, boolean isActive) {
        if (observer instanceof ICollectionLiveDataObserver) {
            return new DelayCollectionObserver((ICollectionLiveDataObserver) observer, isActive);
        } else if (observer instanceof IMapLiveDataObserver) {
            return new DelayMapObserver((IMapLiveDataObserver) observer, isActive);
        } else {
            return new DelayObserver(observer, isActive);
        }
    }
}
