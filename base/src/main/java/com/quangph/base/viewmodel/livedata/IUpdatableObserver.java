package com.quangph.base.viewmodel.livedata;

import androidx.lifecycle.Observer;

/**
 * Created by QuangPH on 2020-11-16.
 */
public interface IUpdatableObserver<T> extends Observer<T> {
    void onUpdate(T data);
}
