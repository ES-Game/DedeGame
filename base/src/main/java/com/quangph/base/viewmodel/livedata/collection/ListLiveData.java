package com.quangph.base.viewmodel.livedata.collection;

import androidx.annotation.Nullable;

import java.util.List;

/**
 * Created by QuangPH on 2020-11-09.
 */
public class ListLiveData<E> extends CollectionLiveData<E, List<E>> {

    public ListLiveData() {

    }

    public ListLiveData(@Nullable List<E> initValue) {
        super(initValue);
    }

    @Override
    public void add(int index, E elm) {
        List<E> value = getValue();
        if (value != null) {
            value.add(index, elm);
            for (CollectionLiveDataObserverInfo info : getObserverList()) {
                info.observer.onAdd(elm, index);
            }
        }
    }

    @Override
    public void remove(E elm) {
        List<E> value = getValue();
        if (value != null) {
            int index = value.indexOf(elm);
            value.remove(elm);
            for (CollectionLiveDataObserverInfo info : getObserverList()) {
                info.observer.onRemove(elm, index);
            }
        }
    }

    @Override
    public void remove(int index) {
        List<E> value = getValue();
        if (value != null) {
            E removed = value.remove(index);
            for (CollectionLiveDataObserverInfo info : getObserverList()) {
                info.observer.onRemove(removed, index);
            }
        }
    }

    @Override
    public void replace(int index, E elm) {
        List<E> value = getValue();
        E old = value.remove(index);
        value.add(index, elm);
        for (CollectionLiveDataObserverInfo info : getObserverList()) {
            info.observer.onReplace(old, elm, index);
        }
    }
}
