package com.quangph.base.viewmodel;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.quangph.base.viewmodel.observer.IDelayObserver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * This VM will delay to fire observer still a fragment is showing
 * Created by Pham Hai Quang on 2/22/2019.
 */
public class NotifyViewModel extends ViewModel {

    private Map<String, ObserverInfo> mObInfoMap = new HashMap<>();
    private boolean isActive = false;

    public ObserverInfo bind(LifecycleOwner obj) {
        return bind(obj.getClass().getName(), obj);
    }

    public ObserverInfo bind(String ownerID, LifecycleOwner obj) {
        ObserverInfo info = mObInfoMap.get(ownerID);
        if (info == null) {
            info = new ObserverInfo();
            info.mOwner = obj;
            mObInfoMap.put(ownerID, info);
        }

        return info;
    }

    public void unbind(LifecycleOwner owner) {
        unbind(owner.getClass().getName());
    }

    public void unbind(String ownerID) {
        ObserverInfo remove = mObInfoMap.remove(ownerID);
        if (remove != null) {
            remove.release();
        }
    }

    public void notifyActive(LifecycleOwner owner, boolean active) {
        isActive = active;
        notifyActive(owner.getClass().getName(), active);
    }

    public void notifyActive(String ownerID, boolean active) {
        isActive = active;
        ObserverInfo info = mObInfoMap.get(ownerID);
        if (info != null) {
            info.notifyActive(active);
        }
    }

    public boolean isActive() {
        return isActive;
    }


    /**********************************************************************************************/
    public static class ObserverInfo {
        private LifecycleOwner mOwner;
        private boolean isActive = false;
        private DelayObserverFactory mFactory = new DefaultDelayObserverFactory();
        //private List<IDelayObserver> mObserverList = new ArrayList<>();
        private List<DelayObserverInfo> mDelayObserverList = new ArrayList<>();

        public<T> ObserverInfo register(LiveData<T> data, Observer<T> observer) {
            IDelayObserver delayObserver = mFactory.create(observer, isActive);
            if (delayObserver != null) {
                //mObserverList.add(delayObserver);
                DelayObserverInfo info = new DelayObserverInfo();
                info.data = data;
                info.observer = delayObserver;
                info.originalObserver = observer;
                mDelayObserverList.add(info);

                data.observe(mOwner, delayObserver);
            }
            return this;
        }

        public<T> ObserverInfo unregister(LiveData<T> data, Observer<T> observer) {
            DelayObserverInfo needToRemove = null;
            for (DelayObserverInfo info : mDelayObserverList) {
                if (info.data.equals(data) && info.originalObserver.equals(observer)) {
                    needToRemove = info;
                    break;
                }
            }

            if (needToRemove != null) {
                needToRemove.release();
                mDelayObserverList.remove(needToRemove);
            }
            return this;
        }

        public<T> void unregisterAll(LiveData<T> data) {
            Iterator<DelayObserverInfo> itr = mDelayObserverList.iterator();
            while (itr.hasNext()) {
                DelayObserverInfo next = itr.next();
                if (next.data.equals(data)) {
                    next.release();
                    itr.remove();
                }
            }
        }

        public void notifyActive(boolean isActive) {
            this.isActive = isActive;
//            for (IDelayObserver observer : mObserverList) {
//                observer.setActive(isActive);
//                if (isActive) {
//                    if (!observer.hasConsumed()) {
//                        observer.onNotifyActive();
//                    }
//                }
//            }

            for (DelayObserverInfo info : mDelayObserverList) {
                info.setActive(isActive);
            }
        }

        public void release() {
            Iterator<DelayObserverInfo> itr = mDelayObserverList.iterator();
            while (itr.hasNext()) {
                itr.next().release();
                itr.remove();
            }
            mOwner = null;
        }


        private static class DelayObserverInfo<T> {
            LiveData<T> data;
            IDelayObserver<T> observer;
            Observer<T> originalObserver;

            public void setActive(boolean isActive) {
                observer.setActive(isActive);
                if (isActive) {
                    if (!observer.hasConsumed()) {
                        observer.onNotifyActive();
                    }
                }
            }

            public void release() {
                data.removeObserver(observer);
            }
        }
    }
}
