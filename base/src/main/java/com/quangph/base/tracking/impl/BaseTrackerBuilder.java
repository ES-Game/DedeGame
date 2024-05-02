package com.quangph.base.tracking.impl;

import android.os.Bundle;

import com.quangph.base.tracking.ITrackerBuilder;

import java.util.Set;

/**
 * Created by Pham Hai Quang on 10/2/2019.
 */
public abstract class BaseTrackerBuilder implements ITrackerBuilder {

    @Override
    public ITrackerBuilder payload(Object payload) {
        if (payload instanceof Bundle) {
            Bundle val = (Bundle) payload;
            Set<String> keys = val.keySet();
            for (String key : keys) {
                put(key, val.get(key));
            }
        }
        return this;
    }
}
