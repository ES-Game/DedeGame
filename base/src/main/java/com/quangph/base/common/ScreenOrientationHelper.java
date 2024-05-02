package com.quangph.base.common;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by QuangPH on 2020-11-09.
 */
public class ScreenOrientationHelper {

    private static final String LAST_ORIENTATION = "LAST_ORIENTATION";
    private Activity mActivity;
    private int mLastOrientation;
    private boolean isStartFromOrientation;

    public ScreenOrientationHelper(Activity activity) {
        mActivity = activity;
    }

    public void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            mLastOrientation = mActivity.getResources().getConfiguration().orientation;
        }
    }

    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(LAST_ORIENTATION, mLastOrientation);
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        mLastOrientation = savedInstanceState != null ?
                savedInstanceState.getInt(LAST_ORIENTATION) :
                mActivity.getResources().getConfiguration().orientation;
    }

    public void checkOrientationChanged() {
        int currentOrientation = mActivity.getResources().getConfiguration().orientation;
        if (currentOrientation != mLastOrientation) {
            isStartFromOrientation = true;
        } else {
            isStartFromOrientation = false;
        }
        mLastOrientation = currentOrientation;
    }

    public boolean isOrientationChanged() {
        return isStartFromOrientation;
    }
}
