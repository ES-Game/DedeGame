package com.quangph.base.viewmodel.observer;

import com.quangph.base.viewmodel.livedata.map.IMapLiveDataObserver;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by QuangPH on 2020-11-18.
 */
public class DelayMapObserver<K, V, MAP extends Map<K, V>>
        implements IMapLiveDataObserver<K, V, MAP>, IDelayObserver<MAP> {

    private static final int PUT = 1;
    private static final int REMOVE = 2;
    private static final int INIT = 3;

    private IMapLiveDataObserver<K, V, MAP> mObserver;
    private boolean isActive = false;
    private boolean hasConsumed = false;
    private MAP mData;
    private List<TempData> mTempList;
    private List<TempData> mTempDataCache;

    public DelayMapObserver(IMapLiveDataObserver<K, V, MAP> observer, boolean isActive) {
        this.mObserver = observer;
        this.isActive = isActive;
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
            } else if (first.type == PUT) {
                mObserver.onPut((K)first.key, (V)first.value);
            } else if (first.type == REMOVE) {
                mObserver.onRemove((K)first.key);
            }

            addTempDataToCache(first);
        }
    }

    @Override
    public void onPut(K key, V value) {
        if (isActive) {
            hasConsumed = true;
            mObserver.onPut(key, value);
        } else {
            hasConsumed = false;
            obtain(PUT, key, value);
        }
    }

    @Override
    public void onRemove(K key) {
        if (isActive) {
            hasConsumed = true;
            mObserver.onRemove(key);
        } else {
            hasConsumed = false;
            obtain(REMOVE, key, null);
        }
    }

    @Override
    public void onChanged(MAP map) {
        mData = map;
        if (isActive) {
            hasConsumed = true;
            mObserver.onChanged(map);
        } else {
            hasConsumed = false;
        }
    }

    private void obtain(int type, Object key, Object value) {
        if (mTempList == null) {
            mTempList = new ArrayList<>();
        }

        TempData temp = obtainTempData();
        temp.type = type;
        temp.value = value;
        temp.key = key;
        mTempList.add(temp);
    }

    private void addTempDataToCache(TempData temp) {
        initCacheTempData();
        temp.reset();
        mTempDataCache.add(temp);
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


    private class TempData {
        int type = 0;
        Object key;
        Object value;

        void reset() {
            type = 0;
            key = null;
            value = null;
        }
    }
}
