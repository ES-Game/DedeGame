package com.quangph.base.viewmodel.observer;

import androidx.lifecycle.Observer;

/**
 * Created by QuangPH on 2020-11-18.
 */
public interface IDelayObserver<T> extends Observer<T> {
    boolean hasConsumed();
    void setActive(boolean active);
    void onNotifyActive();
}
