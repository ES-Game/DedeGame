package com.quangph.jetpack.view.span

import android.os.SystemClock
import android.text.style.ClickableSpan
import android.util.Log
import android.view.View

abstract class SafeClickableSpan : ClickableSpan() {

    private val DEFAULT_MINIMUM_INTERVAL = 100
    private var mLastClickedTime: Long = 0

    abstract fun onSafeClicked(view: View?)

    override fun onClick(v: View) {
        var currentTime = SystemClock.elapsedRealtime()
        var diff: Long = currentTime - mLastClickedTime
        mLastClickedTime = currentTime
        if (diff > DEFAULT_MINIMUM_INTERVAL) {
            onSafeClicked(v)
        } else {
            Log.e("SafeClicked", "Reject multi click on a same view in a short time")
        }
    }
}