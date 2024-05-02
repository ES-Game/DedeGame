package com.quangph.jetpack.perm;

import android.content.Context;

import androidx.fragment.app.Fragment;

/**
 * Created by QuangPH on 2020-06-06.
 */
public class FragmentPermSource implements IPermSource {

    private Fragment mFragment;

    public FragmentPermSource(Fragment fragment) {
        mFragment = fragment;
    }

    @Override
    public Context getContext() {
        return mFragment.getContext();
    }

    @Override
    public void requestPermissions(String[] perms, int requestCode) {
        mFragment.requestPermissions(perms, requestCode);
    }

    @Override
    public boolean shouldShowRequestPermissionRationale(String perm) {
        return mFragment.shouldShowRequestPermissionRationale(perm);
    }
}
