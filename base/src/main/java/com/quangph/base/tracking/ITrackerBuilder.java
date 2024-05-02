package com.quangph.base.tracking;

import android.app.Activity;

/**
 * Created by Pham Hai Quang on 4/10/2019.
 */
public interface ITrackerBuilder {
    ITrackerBuilder put(String key, Object value);
    ITrackerBuilder event(String event);
    ITrackerBuilder screen(Activity screen, String screenName);
    ITrackerBuilder payload(Object payload);
    void track();
}
