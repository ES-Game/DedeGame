package com.quangph.jetpack.perm;

import android.content.Context;

/**
 * Created by QuangPH on 2020-06-06.
 */
public interface IPermSource {
    Context getContext();
    void requestPermissions(String[] perms, int requestCode);
    boolean shouldShowRequestPermissionRationale(String perm);
}
