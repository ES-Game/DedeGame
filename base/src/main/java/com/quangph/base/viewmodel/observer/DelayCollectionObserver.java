package com.quangph.base.viewmodel.observer;


import com.quangph.base.viewmodel.livedata.collection.ICollectionLiveDataObserver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by QuangPH on 2020-11-18.
 */
public class DelayCollectionObserver<E, COL extends Collection<E>>
        implements ICollectionLiveDataObserver<E, COL>, IDelayObserver<COL> {

    private static final int ADD = 1;
    private static final int ADD_ALL = 2;
    private static final int REMOVE = 3;
    private static final int REMOVE_ALL = 4;
    private static final int REPLACE = 5;
    private static final int INIT = 6;

    private ICollectionLiveDataObserver<E, COL> mObserver;
    private boolean isActive = false;
    private boolean hasConsumed = false;
    private COL mData;
    private List<TempData> mTempList;
    private List<TempData> mTempDataCache;

    public DelayCollectionObserver(ICollectionLiveDataObserver<E, COL> observer, boolean isActive) {
        this.mObserver = observer;
        this.isActive = isActive;
    }

    @Override
    public void onAdd(Object elm, int index) {
        if (isActive) {
            hasConsumed = true;
            mObserver.onAdd((E) elm, index);
        } else {
            hasConsumed = false;
            obtain(ADD, elm, index);
        }
    }

    @Override
    public void onAddAll(COL elmCol, int index) {
        if (isActive) {
            hasConsumed = true;
            mObserver.onAddAll(elmCol, index);
        } else {
            hasConsumed = false;
            obtain(ADD_ALL, elmCol, index);
        }
    }

    @Override
    public void onRemove(E elm, int index) {
        if (isActive) {
            hasConsumed = true;
            mObserver.onRemove(elm, index);
        } else {
            hasConsumed = false;
            obtain(REMOVE, elm, index);
        }
    }

    @Override
    public void onRemoveAll(COL elmCol) {
        if (isActive) {
            hasConsumed = true;
            mObserver.onRemoveAll(elmCol);
        } else {
            hasConsumed = false;
            obtain(REMOVE_ALL, elmCol, -1);
        }
    }

    @Override
    public void onReplace(E old, E newElm, int index) {
        if (isActive) {
            hasConsumed = true;
            mObserver.onReplace(old, newElm, index);
        } else {
            hasConsumed = false;
            obtain(REPLACE, newElm, old, index);
        }
    }

    @Override
    public void onChanged(COL es) {
        mData = es;
        if (isActive) {
            hasConsumed = true;
            mObserver.onChanged(es);
        } else {
            hasConsumed = false;
            obtain(INIT, null, -1);
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
        if (mTempList == null) return;

        while (!mTempList.isEmpty()) {
            TempData first = mTempList.remove(0);
            if (first.type == INIT) {
                mObserver.onChanged(mData);
            } else if (first.type == ADD) {
                mObserver.onAdd((E) first.elm, first.index);
            } else if (first.type == ADD_ALL) {
                mObserver.onAddAll((COL) first.newCollection, first.index);
            } else if (first.type == REMOVE) {
                mObserver.onRemove((E) first.elm, first.index);
            } else if (first.type == REMOVE_ALL) {
                mObserver.onRemoveAll((COL) first.newCollection);
            } else if (first.type == REPLACE) {
                mObserver.onReplace((E)first.olm, (E)first.elm, first.index);
            }

            addTempDataToCache(first);
        }
    }

    private void obtain(int type, Object elm, int index) {
        if (mTempList == null) {
            mTempList = new ArrayList<>();
        }

        TempData temp = obtainTempData();
        temp.type = type;
        temp.elm = elm;
        temp.olm = null;
        temp.newCollection = null;
        temp.index = index;
        mTempList.add(temp);
    }

    private void obtain(int type, Object elm, Object old, int index) {
        if (mTempList == null) {
            mTempList = new ArrayList<>();
        }

        TempData temp = obtainTempData();
        temp.type = type;
        temp.elm = elm;
        temp.olm = old;
        temp.newCollection = null;
        temp.index = index;
        mTempList.add(temp);
    }

    private void obtain(int type, COL elm, int index) {
        if (mTempList == null) {
            mTempList = new ArrayList<>();
        }

        TempData temp = obtainTempData();
        temp.type = type;
        temp.elm = null;
        temp.olm = null;
        temp.newCollection = elm;
        temp.index = index;
        mTempList.add(temp);
    }

    private void initCacheTempData() {
        if (mTempDataCache == null) {
            mTempDataCache = new ArrayList<>();
        }
    }

    private TempData obtainTempData() {
        initCacheTempData();
        TempData temp;
        if (mTempDataCache.isEmpty()) {
            temp = new TempData();
        } else {
            temp = mTempDataCache.remove(0);
        }
        return temp;
    }

    private void addTempDataToCache(TempData temp) {
        initCacheTempData();
        temp.reset();
        mTempDataCache.add(temp);
    }

    private class TempData {
        int type = 0;
        Object elm;
        Object olm;
        Object newCollection;
        int index;

        void reset() {
            type = 0;
            elm = null;
            olm = null;
            newCollection = null;
            index = -1;
        }
    }
}
