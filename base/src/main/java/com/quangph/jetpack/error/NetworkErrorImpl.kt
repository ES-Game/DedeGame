package com.quangph.jetpack.error

import android.util.Log
import com.quangph.jetpack.IJetContext
import com.quangph.jetpack.alert.ALERT_TYPE

/**
 * Created by QuangPH on 2020-06-04.
 */
object NetworkErrorImpl : INetworkError {

    open var networkErrorAlert: String = ""

    private var isConnected = true
    private var lastTimeShowMess: Long = 0
    private const val TIME_SHOW_SHOW = 3000
    private val shownNetworkErrorScreenList = arrayListOf<String>()

    override fun onNetworkConnected() {
        isConnected = true
        shownNetworkErrorScreenList.clear()
    }

    override fun onNetworkError(context: IJetContext) {
        isConnected = false
        if (shownNetworkErrorScreenList.contains(context.screenName)) {
            return
        }
        shownNetworkErrorScreenList.add(context.screenName)
        context.showAlert(networkErrorAlert, ALERT_TYPE.ERROR)


//        if (changedToErrorState()) {
//            Log.e("NetworkErrorImpl", "show error")
//            isConnected = false
//            context.showAlert(networkErrorAlert, ALERT_TYPE.ERROR)
//        } else {
//            Log.e("NetworkErrorImpl", "No show error")
//        }
    }

    private fun changedToErrorState(): Boolean {
        val currentTime = System.currentTimeMillis()
        var durCondition = false
        if (currentTime - lastTimeShowMess >= TIME_SHOW_SHOW) {
            lastTimeShowMess = currentTime
            durCondition = true
        }
        return isConnected || durCondition
    }
}