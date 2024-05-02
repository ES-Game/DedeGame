package com.quangph.jetpack.alert

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.quangph.base.R

/**
 * Created by QuangPH on 2020-11-26.
 */
class HeaderAlertDefault(activity: AppCompatActivity): HeaderAlert(activity) {

    private val mesageEnqueue = arrayListOf<MessageInfo>()
    private val handler = Handler(Looper.getMainLooper())
    private val dismissRunnable = Runnable {
        dismiss()
    }

    override fun onCreateView(activity: AppCompatActivity, type: String): ViewGroup {
        return LayoutInflater.from(activity).inflate(R.layout.layout_header_alert, null) as ViewGroup
    }

    override fun onViewCreated(view: ViewGroup, type: String) {
        if ((activity.window.decorView.systemUiVisibility == View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
            || (activity.window.decorView.systemUiVisibility == View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)) {
            view.setPadding(view.paddingLeft,
                view.resources.getDimensionPixelSize(R.dimen.dimen_20) + getStatusBarHeight(view.context),
                view.paddingRight, view.paddingBottom)
        }
        val iv = view.findViewById<ImageView>(R.id.ivHeaderAlertIcon)
        if (type == ALERT_TYPE.ERROR || type == ALERT_TYPE.WARNING) {
            iv.setImageResource(R.drawable.ic_error_info)
            view.setBackgroundResource(R.color.red_color)
        } else {
            iv.setImageResource(R.drawable.ic_success_info)
            view.setBackgroundResource(R.color.green_color)
        }

        val tvMsg = view.findViewById<TextView>(R.id.tvHeaderAlertMsg)
        tvMsg.text = message
        view.setOnClickListener {

        }

        handler.postDelayed(dismissRunnable, 1500)
    }

    override fun destroy() {
        handler.removeCallbacks(dismissRunnable)
        mesageEnqueue.clear()
    }

    override fun onDismiss() {
        super.onDismiss()
        Log.e("HeaderAlert", "onDismiss: ${mesageEnqueue.size}")
        if (mesageEnqueue.isNotEmpty()) {
            val nextMsg = mesageEnqueue.removeAt(0)
            show(nextMsg.msg, nextMsg.type!!)
        }
    }

    override fun enqueueMessage(msg: String?, type: String) {
        super.enqueueMessage(msg, type)
        var exist = false
        mesageEnqueue.forEach {
            if (it.isSame(msg)) {
                exist = true
                return@forEach
            }
        }
        if (!exist) {
            val msgInfo = MessageInfo()
            msgInfo.msg = msg
            msgInfo.type = type
            mesageEnqueue.add(msgInfo)
        }
    }

    private fun getStatusBarHeight(context: Context): Int{
        var result = 0
        val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = context.resources.getDimensionPixelSize(resourceId)
        }
        return result
    }


    private class MessageInfo() {
        var type: String? = null
        var msg: String? = null

        fun isSame(msg: String?): Boolean {
            return this.msg == msg
        }
    }
}