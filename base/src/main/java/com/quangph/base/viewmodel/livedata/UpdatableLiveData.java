package com.quangph.base.viewmodel.livedata;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by QuangPH on 2020-11-16.
 */
public class UpdatableLiveData<T> extends MutableLiveData<T> {

    private List<UpdatableLiveDataInfo> mObserverList = new ArrayList<>();

    public UpdatableLiveData() {
        this(null);
    }

    public UpdatableLiveData(T initValue) {
        setValue(initValue);
    }

    @Override
    public void observe(@NonNull LifecycleOwner owner, @NonNull Observer<? super T> observer) {
        super.observe(owner, observer);
        if (observer instanceof IUpdatableObserver) {
            mObserverList.add(new UpdatableLiveDataInfo((IUpdatableObserver) observer, owner));
        }
    }

    @Override
    public void removeObserver(@NonNull Observer<? super T> observer) {
        super.removeObserver(observer);
        synchronized (mObserverList) {
            Iterator<UpdatableLiveDataInfo> iterator = mObserverList.iterator();
            while (iterator.hasNext()) {
                UpdatableLiveDataInfo next = iterator.next();
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
            Iterator<UpdatableLiveDataInfo> iterator = mObserverList.iterator();
            while (iterator.hasNext()) {
                UpdatableLiveDataInfo next = iterator.next();
                if (next.owner.equals(owner)) {
                    iterator.remove();
                }
            }
        }
    }

    public void update() {
        for (UpdatableLiveDataInfo info : mObserverList) {
            info.observer.onUpdate(getValue());
        }
    }


    private static class UpdatableLiveDataInfo {
        public IUpdatableObserver observer;
        public LifecycleOwner owner;

        UpdatableLiveDataInfo(IUpdatableObserver observer, LifecycleOwner owner) {
            this.observer = observer;
            this.owner = owner;
        }
    }
}
