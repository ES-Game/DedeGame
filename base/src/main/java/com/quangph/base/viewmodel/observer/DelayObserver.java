package com.quangph.base.viewmodel.observer;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;

/**
 * Created by QuangPH on 2020-11-18.
 */
public class DelayObserver<T> implements IDelayObserver<T> {
    private boolean isActive = false;
    private Observer<T> mObserver;
    private T mData;
    private boolean hasConsumed = false;

    public DelayObserver(Observer<T> observer, boolean isActive) {
        mObserver = observer;
        this.isActive = isActive;
    }

    @Override
    public void onChanged(@Nullable T t) {
        mData = t;
        if (isActive) {
            hasConsumed = true;
            mObserver.onChanged(t);
        } else {
            hasConsumed = false;
        }
    }

    @Override
    public boolean hasConsumed() {
        return hasConsumed;
    }

    @Override
    public void setActive(boolean active) {
        isActive = active;
    }

    @Override
    public void onNotifyActive() {
        hasConsumed = true;
        mObserver.onChanged(mData);
    }
}
