package com.quangph.base.tracking;

import android.app.Activity;

/**
 * Created by quangph on 1/2/2020
 */
public interface ITrackingFactory {
    <T extends ITrackerConfig> T getTracker(Activity activity, Class<T> configClass);
}
