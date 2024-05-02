package com.quangph.base.common;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import com.quangph.base.cache.CacheManager;

/**
 * Created by quangph on 1/21/2016.
 */
public class BaseApplication extends Application {
    private static Application sApplication;
    private boolean wasInBackground = false;

    public static Application getApplication() {
        return sApplication;
    }

    public static Context getContext() {
        return getApplication().getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sApplication = this;
        registerActivityLifecycleCallbacks(new BackgroundControlLifecycleHandler());
        registerComponentCallbacks(CacheManager.getInstance());
    }

    public boolean gotoIntoBackground() {
        return wasInBackground;
    }

    public class BackgroundControlLifecycleHandler implements ActivityLifecycleCallbacks {
        private int started;
        private int stopped;

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        }

        @Override
        public void onActivityDestroyed(Activity activity) {
        }

        @Override
        public void onActivityResumed(Activity activity) {
            wasInBackground = true;
        }

        @Override
        public void onActivityPaused(Activity activity) {
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        }

        @Override
        public void onActivityStarted(Activity activity) {
            ++started;
        }

        @Override
        public void onActivityStopped(Activity activity) {
            ++stopped;
            wasInBackground = started > stopped;
        }
    }
}
