package com.quangph.jetpack.infras

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.os.Build
import android.provider.Settings
import com.quangph.jetpack.utils.StringUtil

class DeviceInfo() {
    fun getSystem() : String {
        return "ANDROID"
    }

    @SuppressLint("HardwareIds")
    fun getDeviceId(contentResolver: ContentResolver): String? {
        return Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
    }

    fun getBuildVersionName(): String? {
        return when (Build.VERSION.SDK_INT) {
            Build.VERSION_CODES.KITKAT -> "Android 4.4"
            Build.VERSION_CODES.LOLLIPOP -> "Android 5.0"
            Build.VERSION_CODES.M -> "Android 6.0"
            Build.VERSION_CODES.N -> "Android 7.0"
            Build.VERSION_CODES.N_MR1 -> "Android 7.1.2"
            Build.VERSION_CODES.O -> "Android 8.0"
            Build.VERSION_CODES.O_MR1 -> "Android 8.1"
            Build.VERSION_CODES.P -> "Android 9.0"
            29 -> "Android 10.0"
            else -> Build.VERSION.SDK_INT.toString() + ""
        }
    }

    fun getDeviceName(): String {
        val manufacturer = Build.MANUFACTURER
        val model = Build.MODEL
        return if (model.startsWith(manufacturer)) {
            StringUtil.capitalize(model)
        } else StringUtil.capitalize(manufacturer) + " " + model
    }
}