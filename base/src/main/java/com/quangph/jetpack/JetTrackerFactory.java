package com.quangph.jetpack;

import android.app.Activity;
import android.app.Application;

import com.quangph.base.tracking.ITrackerConfig;
import com.quangph.base.tracking.ITrackingFactory;
import com.quangph.base.tracking.Tracker;
import com.quangph.base.tracking.impl.firebase.FirebaseTracker;
import com.quangph.base.tracking.impl.firebase.IFirebaseTrackerConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Pham Hai Quang on 5/1/2019.
 */
public class JetTrackerFactory implements ITrackingFactory {

    private Application mContext;
    private Map<Class, Tracker> mTrackerMap = new HashMap<>();

    public JetTrackerFactory(Application context) {
        mContext = context;
    }

    @Override
    public<T extends ITrackerConfig> T getTracker(Activity activity, Class<T> configClass) {
        Tracker tracker = mTrackerMap.get(configClass);
        if (tracker == null) {
            tracker = init(configClass);
            mTrackerMap.put(configClass, tracker);
        }
        return (T) tracker.createTrackerConfig(configClass, activity);
    }

    private Tracker init(Class configClass) {
        if (IFirebaseTrackerConfig.class.isAssignableFrom(configClass)) {
            return new FirebaseTracker<>(mContext);
        }
        return null;
    }
}
