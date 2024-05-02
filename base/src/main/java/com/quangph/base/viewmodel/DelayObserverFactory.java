package com.quangph.base.viewmodel;

import androidx.lifecycle.Observer;

import com.quangph.base.viewmodel.observer.IDelayObserver;

/**
 * Created by QuangPH on 2020-11-18.
 */
public abstract class DelayObserverFactory {
    public abstract <T> IDelayObserver create(Observer observer, boolean isActive);
}
