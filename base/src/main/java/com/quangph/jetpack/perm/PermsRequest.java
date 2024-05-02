package com.quangph.jetpack.perm;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Support request permission for SDK > 16 to avoid boilerplate code
 * Created by Pham Hai QUANG on 10/22/2018.
 */
public class PermsRequest {

    public interface OnPermsRequestCallback {
        boolean onPermsRequest(String[] notGrantedPerms, String[] shouldRequestRationalPerms);
    }

    private static final int PERM_REQUEST_CODE = 8804;

    private static PermsRequest mCurrentRequest;
    private IPermSource mSource;
    private String[] mPerms;
    private OnPermsRequestCallback mCallback;

    private PermsRequest(Activity activity, String[] perms) {
        mSource = new ActivityPermSource(activity);
        mPerms = perms;
    }

    private PermsRequest(Fragment fragment, String[] perms) {
        mSource = new FragmentPermSource(fragment);
        mPerms = perms;
    }

    public static PermsRequest requestPerms(Activity activity, String[] perms) {
        mCurrentRequest = new PermsRequest(activity, perms);
        return mCurrentRequest;
    }

    public static PermsRequest requestPerms(Fragment fragment, String[] perms) {
        mCurrentRequest = new PermsRequest(fragment, perms);
        return mCurrentRequest;
    }

    public static boolean onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                                     @NonNull int[] grantResults) {
        try {
            return mCurrentRequest.onResult(requestCode, permissions, grantResults);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            mCurrentRequest.releaseInternal();
            mCurrentRequest = null;
        }
    }

    public static boolean hasRequestPerm() {
        return mCurrentRequest != null;
    }

    public static void release() {
        if (mCurrentRequest != null) {
            mCurrentRequest.releaseInternal();
            mCurrentRequest = null;
        }
    }

    public void with(OnPermsRequestCallback callback) {
        mCallback = callback;
        requestPerms();
    }

    private PermsRequest requestPerms() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            List<String> permsHasNotGrantedYet = new ArrayList<>();
            for (String perm : mPerms) {
                int granted = ContextCompat.checkSelfPermission(mSource.getContext(), perm);
                if (granted != PackageManager.PERMISSION_GRANTED) {
                    permsHasNotGrantedYet.add(perm);
                }
            }
            if (!permsHasNotGrantedYet.isEmpty()) {
                mSource.requestPermissions(permsHasNotGrantedYet.toArray(new String[0]), PERM_REQUEST_CODE);
            } else {
                mCallback.onPermsRequest(new String[]{}, new String[]{});
                releaseInternal();
                mCurrentRequest = null;
            }
        } else {
            mCallback.onPermsRequest(new String[]{}, new String[]{});
            releaseInternal();
            mCurrentRequest = null;
        }

        return this;
    }


    private boolean onResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERM_REQUEST_CODE) {
            List<String> permsHasNotGranted = new ArrayList<>();
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    permsHasNotGranted.add(permissions[i]);
                }
            }

            List<String> shouldShowRationalPerms = new ArrayList<>();
            for (String perm : permsHasNotGranted) {
                if (!mSource.shouldShowRequestPermissionRationale(perm)) {
                    shouldShowRationalPerms.add(perm);
                }
            }

            mCallback.onPermsRequest(permsHasNotGranted.toArray(new String[0]), shouldShowRationalPerms.toArray(new String[0]));
        }
        return false;
    }

    private void releaseInternal() {
        mPerms = null;
        mCallback = null;
        mSource = null;
    }
}
