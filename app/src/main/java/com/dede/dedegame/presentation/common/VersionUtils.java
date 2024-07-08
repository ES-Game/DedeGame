package com.dede.dedegame.presentation.common;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;

public class VersionUtils {
    public static int getVersionCode(Context context) {
        PackageManager packageManager = context.getPackageManager();
        String packageName = context.getPackageName();

        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(packageName, 0);
            long versionCode;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                versionCode = packageInfo.getLongVersionCode();
            } else {
                versionCode = packageInfo.versionCode;
            }
            return (int) versionCode; // Casting to int
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return -1;
        }
    }
}
