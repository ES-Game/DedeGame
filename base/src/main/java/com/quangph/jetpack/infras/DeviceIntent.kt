package com.quangph.jetpack.infras

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import com.quangph.base.R

/**
 * Created by QuangPH on 2020-11-28.
 */
class DeviceIntent {
    fun callPhoneAction(context: Context, phoneNumber: String) {
        val packageManager: PackageManager = context.packageManager
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${phoneNumber}"))
        if (intent.resolveActivity(packageManager) != null) {
            context.startActivity(intent)
        }
    }

    fun sendEmailAction(context: Context, email: String) {
        val packageManager: PackageManager = context.packageManager
        val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:${email}"))
        intent.putExtra(Intent.EXTRA_SUBJECT, "")
        intent.putExtra(Intent.EXTRA_TEXT, "")
        if (intent.resolveActivity(packageManager) != null) {
            context.startActivity(Intent.createChooser(intent, context.getString(R.string.please_choose)))
        }
    }

    fun isPackageInstalled(context: Context, packageName: String): Boolean {
        return try {
            context.packageManager?.getPackageInfo(packageName, 0)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            false
        }
    }
}