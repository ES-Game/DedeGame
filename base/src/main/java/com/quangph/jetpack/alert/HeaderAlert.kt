package com.quangph.jetpack.alert

import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import androidx.appcompat.app.AppCompatActivity
import com.quangph.base.R

/**
 * This alert will show on the top of activity. It also is dismissed automatically
 * Created by QuangPH on 2020-11-26.
 */
abstract class HeaderAlert(val activity: AppCompatActivity) : IAlert {
    var message: String? = null
    var alertView: ViewGroup? = null
    private var isShowing = false

    override fun show(msg: String?, type: String) {
        Log.e("HeaderAlert", "message: $msg $isShowing")
        if (isShowing) {
            if (msg != message) {
                enqueueMessage(msg, type)
            }
            return
        }

        isShowing = true
        message = msg
        if (alertView == null) {
            alertView = onCreateView(activity, type)
            addView(alertView!!)
        }

        onViewCreated(alertView!!, type)
        showAnim(alertView!!, type)
    }

    override fun dismiss() {
        message = null
        dismissAnim(alertView!!)
    }

    abstract fun onCreateView(activity: AppCompatActivity, type: String): ViewGroup
    abstract fun onViewCreated(view: ViewGroup, type: String)

    open fun addView(view: ViewGroup) {
        activity.window.addContentView(alertView,
            ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))
    }

    open fun showAnim(view: ViewGroup, type: String) {
        val animDown = AnimationUtils.loadAnimation(activity, R.anim.slide_in_bottom)
        val animController = LayoutAnimationController(animDown)

        view.visibility = View.VISIBLE
        view.layoutAnimation = animController
        animDown.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {

            }

            override fun onAnimationEnd(animation: Animation?) {
                onShow(type)
                //Log.e("HeaderAlert", "onSHow")
            }

            override fun onAnimationStart(animation: Animation?) {
                //Log.e("HeaderAlert", "onStart")
            }
        })
        view.startAnimation(animDown)
    }

    open fun dismissAnim(view: ViewGroup) {
        val animUp = AnimationUtils.loadAnimation(activity, R.anim.slide_out_top)
        val animController = LayoutAnimationController(animUp)

        view.visibility = View.GONE
        view.layoutAnimation = animController
        animUp.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {

            }

            override fun onAnimationEnd(animation: Animation?) {
                isShowing = false
                onDismiss()
            }

            override fun onAnimationStart(animation: Animation?) {
            }

        })
        view.startAnimation(animUp)
    }

    open fun onShow(type: String) {}
    open fun onDismiss() {}
    open fun enqueueMessage(msg: String?, type: String) {}
}