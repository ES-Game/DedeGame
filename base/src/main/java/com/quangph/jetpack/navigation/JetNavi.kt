package com.quangph.jetpack.navigation

import android.app.Activity
import android.content.Intent
import com.quangph.base.lifecycle.OnActivityResult
import com.quangph.base.lifecycle.OnDestroy
import com.quangph.base.lifecycle.OnDestroyView

/**
 * Navigation helper
 * Created by QuangPH on 2020-11-30.
 */
class JetNavi(private val naviSource: INaviSource): OnActivityResult, OnDestroy, OnDestroyView {

    private var currentJetInfo: JetNaviInfo? = null
    private var requestCode: Int = 1

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent): Boolean {
        if (currentJetInfo == null) return false
        if (currentJetInfo!!.requestCode != requestCode) return false
        currentJetInfo?.callback?.invoke(resultCode, data)
        currentJetInfo = null
        return true
    }

    override fun onDestroy() {
        currentJetInfo = null
    }

    override fun onDestroyView() {
        currentJetInfo = null
    }

    fun<ACTIVITY: Activity> naviTo(destClass: Class<ACTIVITY>, argsBuilder: (Intent.() -> Unit)? = null): JetNavi {
        return naviTo(Intent(naviSource.getContext(), destClass).apply {
            argsBuilder?.invoke(this)
        })
    }

    fun naviTo(intent: Intent): JetNavi {
        requestCode++
        currentJetInfo = JetNaviInfo()
        currentJetInfo!!.requestCode = requestCode
        currentJetInfo?.intent = intent
        return this
    }

    fun with(callback: (Int, Intent) -> Unit): JetNavi {
        if (currentJetInfo == null) return this
        currentJetInfo!!.callback = callback
        return this
    }

    fun start() {
        if (currentJetInfo == null) return

        if (currentJetInfo!!.callback == null) {
            naviSource.startActivity(currentJetInfo!!.intent!!)
        } else {
            naviSource.startActivityForResult(requestCode, currentJetInfo!!.intent!!)
        }
    }


    private class JetNaviInfo {
        var requestCode: Int = 0
        var callback: ((Int, Intent) -> Unit)? = null
        var intent: Intent? = null
    }
}