package com.quangph.base.tracking.impl.firebase;


import android.app.Application;

import com.quangph.base.tracking.ITrackerBuilder;
import com.quangph.base.tracking.Tracker;

/**
 * Created by Pham Hai Quang on 4/11/2019.
 */
public class FirebaseTracker<T> extends Tracker<T> {

    private Application mContext;

    public FirebaseTracker(Application context) {
        mContext = context;
    }

    @Override
    protected ITrackerBuilder onCreateTrackerBuilder() {
        return new FirebaseTrackerBuilder(mContext);
    }
}
