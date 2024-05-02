package com.quangph.jetpack.navigation

import android.content.Context
import android.content.Intent

/**
 * Created by QuangPH on 2020-11-30.
 */

interface INaviSource {
    fun getContext(): Context?
    fun startActivity(intent: Intent)
    fun startActivityForResult(requestCode: Int, intent: Intent)
}