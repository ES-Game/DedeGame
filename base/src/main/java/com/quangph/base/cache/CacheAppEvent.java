package com.quangph.base.cache;

import com.quangph.base.common.bus.IAppEvent;

public class CacheAppEvent implements IAppEvent {
    public static final int TRIM_MEMORY_MODERATE = 1;
    public static final int TRIM_MEMORY_BACKGROUND = 2;

    private int mEventType;

    public CacheAppEvent(int type) {
        mEventType = type;
    }

    @Override
    public int getEventType() {
        return mEventType;
    }
}
