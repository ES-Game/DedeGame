package com.quangph.base.common;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import androidx.multidex.MultiDexApplication;

import com.quangph.base.cache.CacheManager;

/**
 * Created by QuangPH on 2020-01-09.
 */
public class BaseMultiDexApplication extends MultiDexApplication {
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
        registerActivityLifecycleCallbacks(new BackgroundControllLifecycleHandler());
        registerComponentCallbacks(CacheManager.getInstance());
    }

    public boolean gotoIntoBackgroud() {
        return wasInBackground;
    }

    public class BackgroundControllLifecycleHandler implements ActivityLifecycleCallbacks {
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
