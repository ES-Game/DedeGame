package com.quangph.base.viewmodel.livedata.collection;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Created by QuangPH on 2020-11-09.
 */
public class CollectionLiveData<E, COL extends Collection<E>> extends MutableLiveData<COL> {

    private List<CollectionLiveDataObserverInfo> mObserverList = new ArrayList<>();

    public CollectionLiveData(){}

    public CollectionLiveData(COL initValue) {
        setValue(initValue);
    }

    @Override
    public void observe(@NonNull LifecycleOwner owner, @NonNull Observer<? super COL> observer) {
        super.observe(owner, observer);
        if (observer instanceof ICollectionLiveDataObserver) {
            mObserverList.add(new CollectionLiveDataObserverInfo((ICollectionLiveDataObserver) observer, owner));
        }
    }

    @Override
    public void removeObserver(@NonNull Observer<? super COL> observer) {
        super.removeObserver(observer);
        synchronized (mObserverList) {
            Iterator<CollectionLiveDataObserverInfo> iterator = mObserverList.iterator();
            while (iterator.hasNext()) {
                CollectionLiveDataObserverInfo next = iterator.next();
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
            Iterator<CollectionLiveDataObserverInfo> iterator = mObserverList.iterator();
            while (iterator.hasNext()) {
                CollectionLiveDataObserverInfo next = iterator.next();
                if (next.owner.equals(owner)) {
                    iterator.remove();
                }
            }
        }
    }

    public void add(E elm) {
        Collection<E> data = getValue();
        if (data != null) {
            int nextIndex = data.size();
            getValue().add(elm);
            for (CollectionLiveDataObserverInfo info : mObserverList) {
                info.observer.onAdd(elm, nextIndex);
            }
        }
    }

    public void add(int index, E elm) {

    }

    public void remove(E elm) {
    }

    public void remove(int index) {}

    public void addAll(COL elmCol) {
        Collection<E> data = getValue();
        if (data != null) {
            int nextIndex = data.size();
            data.addAll(elmCol);
            for (CollectionLiveDataObserverInfo info : mObserverList) {
                info.observer.onAddAll(elmCol, nextIndex);
            }
        }
    }

    public void removeAll(COL elmCol) {
        Collection<E> data = getValue();
        if (data != null) {
            data.removeAll(elmCol);
            for (CollectionLiveDataObserverInfo info : mObserverList) {
                info.observer.onRemoveAll(elmCol);
            }
        }
    }

    public void replace(int index, E elm) {
    }

    public void reset(COL elmCol) {
        setValue(elmCol);
    }

    public List<CollectionLiveDataObserverInfo> getObserverList() {
        return mObserverList;
    }
}
