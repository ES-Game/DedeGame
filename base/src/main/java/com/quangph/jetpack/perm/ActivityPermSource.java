package com.quangph.jetpack.perm;

import android.app.Activity;
import android.content.Context;
import android.os.Build;

import androidx.core.app.ActivityCompat;

/**
 * Created by QuangPH on 2020-06-06.
 */
public class ActivityPermSource implements IPermSource {
    private Activity mActivity;

    public ActivityPermSource(Activity activity) {
        mActivity = activity;
    }

    @Override
    public Context getContext() {
        return mActivity;
    }

    @Override
    public void requestPermissions(String[] perms, int requestCode) {
        ActivityCompat.requestPermissions(mActivity, perms, requestCode);
    }

    @Override
    public boolean shouldShowRequestPermissionRationale(String perm) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return mActivity.shouldShowRequestPermissionRationale(perm);
        } else {
            return false;
        }
    }
}
