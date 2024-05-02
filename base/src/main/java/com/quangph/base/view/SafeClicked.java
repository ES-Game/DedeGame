package com.quangph.base.view;

import android.os.SystemClock;
import android.util.Log;
import android.view.View;

/**
 * Created by Pham Hai Quang on 4/10/2019.
 */
public abstract class SafeClicked implements View.OnClickListener {

    private static final int DEFAULT_MINIMUM_INTERVAL = 100;
    private long mLastClickedTime;

    public abstract void onSafeClicked(View view);

    @Override
    public void onClick(View v) {
        long currentTime = SystemClock.elapsedRealtime();
        long diff = currentTime - mLastClickedTime;
        mLastClickedTime = currentTime;
        if (diff > DEFAULT_MINIMUM_INTERVAL) {
            onSafeClicked(v);
        } else {
            Log.e("SafeClicked", "Reject multi click on a same view in a short time");
        }
    }
}
