package com.quangph.jetpack.navigation

import android.app.Activity
import android.content.Context
import android.content.Intent

/**
 * Created by QuangPH on 2020-11-30.
 */
class ActivityNaviSource(private val activity: Activity) : INaviSource {

    override fun getContext(): Context? {
        return activity
    }

    override fun startActivity(intent: Intent) {
        activity.startActivity(intent)
    }

    override fun startActivityForResult(requestCode: Int, intent: Intent) {
        activity.startActivityForResult(intent, requestCode)
    }
}