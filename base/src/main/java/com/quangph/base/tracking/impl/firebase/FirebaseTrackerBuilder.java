package com.quangph.base.tracking.impl.firebase;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.quangph.base.tracking.ITrackerBuilder;
import com.quangph.base.tracking.impl.BaseTrackerBuilder;


/**
 * Created by Pham Hai Quang on 4/10/2019.
 */
public class FirebaseTrackerBuilder extends BaseTrackerBuilder {

    private Application mContext;
    private Bundle mBundle = new Bundle();
    private String mEvent = "";
    private Activity mSource;
    private String mSrcLabel;

    public FirebaseTrackerBuilder(Application context) {
        mContext = context;
    }

    @Override
    public ITrackerBuilder put(String key, Object value) {
        addData(key, value);
        return this;
    }

    @Override
    public ITrackerBuilder event(String event) {
        mEvent = event;
        return this;
    }

    @Override
    public ITrackerBuilder screen(Activity screen, String screenName) {
        mSource = screen;
        mSrcLabel = screenName;
        return this;
    }

    @Override
    public void track() {
        if (mSource != null && mSrcLabel != null) {
            FirebaseAnalytics.getInstance(mContext)
                    .setCurrentScreen(mSource, mSrcLabel,null);
        }
        FirebaseAnalytics.getInstance(mContext).logEvent(mEvent, mBundle);
    }

    private void addData(String key, Object value) {
        if (value instanceof String) {
            mBundle.putString(key, (String) value);
        } else if (value instanceof Double) {
            mBundle.putDouble(key, (Double) value);
        } else if (value instanceof Integer) {
            mBundle.putInt(key, (Integer) value);
        }
    }
}
