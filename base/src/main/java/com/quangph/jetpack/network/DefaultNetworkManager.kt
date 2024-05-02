package com.quangph.jetpack.network

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Build
import android.util.Log
import androidx.core.app.JobIntentService
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.quangph.eventbus.PendingEventBus
import com.quangph.jetpack.workmanager.WorkManagerUtil
import java.util.concurrent.TimeUnit


/**
 * Check network periodic by default 15 minutes
 * When WIFI turn on in slow internet -> We decide the network is unavailable.
 * We check slow WIFI by running a job in which we ping to google.com and calculate the response
 * time and the download volume
 * NOTE: Use this on Main Thread
 *
 * Created by QuangPH on 3/22/2021.
 */
class DefaultNetworkManager : INetworkManager {

    companion object {
        const val CHECK_NETWORK_CONNECTION_INTERVAL_MINUTE = 15L
        const val CHECK_NETWORK_CONNECTION_TAG = "CHECK_NETWORK_CONNECTION_TAG"
    }

    private var callback: ((NETWORK_STATUS) -> Unit)? = null
    private var isNetworkAvailable = false

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent?) {
            when (intent?.action) {
                CheckNetworkConnectionJob.NETWORK_STATE_ACTION -> {
                    isNetworkAvailable =
                        intent.getBooleanExtra(CheckNetworkConnectionJob.NETWORK_STATE_KEY, false)
                    Log.e(
                        "NETWORK_SERVICE",
                        "$isNetworkAvailable"
                    )

                    if (!isNetworkAvailable) {
                        val hasInternetConnection = haveNetworkConnection(context)

                        if (hasInternetConnection) {
                            this@DefaultNetworkManager.callback?.invoke(NETWORK_STATUS.SLOW)
                            val workManager = WorkManager.getInstance(context.applicationContext)
                            workManager.cancelAllWorkByTag(CHECK_NETWORK_CONNECTION_TAG)

                            startCheckNetWorkJob(context)
                        }
                    } else {
                        this@DefaultNetworkManager.callback?.invoke(NETWORK_STATUS.AVAILABLE)
                        PendingEventBus.getInstance().post(NetworkAvailableStatusEvent())
                    }
                }
                ConnectivityManager.CONNECTIVITY_ACTION -> {

                    val hasInternetConnection = haveNetworkConnection(context)

                    Log.e(
                        "NETWORK_SERVICE",
                        "network change $hasInternetConnection"
                    )

                    if (hasInternetConnection) {
                        startCheckNetWorkJob(context.applicationContext)
                    } else {
                        isNetworkAvailable = hasInternetConnection
                        this@DefaultNetworkManager.callback?.invoke(NETWORK_STATUS.UNAVAILABLE)
                        val workManager = WorkManager.getInstance(context.applicationContext)
                        workManager.cancelAllWorkByTag(CHECK_NETWORK_CONNECTION_TAG)
                    }
                }

            }

        }
    }

    override fun startCheckNetwork(context: Context) {

        val intentFilter = IntentFilter().apply {
            addAction(CheckNetworkConnectionJob.NETWORK_STATE_ACTION)
            addAction(ConnectivityManager.CONNECTIVITY_ACTION)
        }

        context.registerReceiver(receiver, intentFilter)

        Log.e("NETWORK_SERVICE", "startCheckNetwork")

        val workManager = WorkManager.getInstance(context.applicationContext)
        workManager.cancelAllWorkByTag(CHECK_NETWORK_CONNECTION_TAG)

        if (haveNetworkConnection(context)) {
            startCheckNetWorkJob(context)
        }
    }

    override fun isAvailable(context: Context): Boolean {
        return isNetworkAvailable
    }

    override fun setNotifyNetworkStatusCallback(callback: (NETWORK_STATUS) -> Unit) {
        this.callback = callback
    }

    private fun startCheckNetWorkJob(context: Context) {
        val isWorkScheduled = isWorkScheduled(context, CHECK_NETWORK_CONNECTION_TAG)
        Log.e("NETWORK_SERVICE", "startCheckNetwork $isWorkScheduled")

        if (!isWorkScheduled) {
            val checkNetworkConnectionJob = PeriodicWorkRequestBuilder<CheckNetworkConnectionJob>(
                CHECK_NETWORK_CONNECTION_INTERVAL_MINUTE, TimeUnit.MINUTES
            )
                .addTag(CHECK_NETWORK_CONNECTION_TAG)
                .build()

            val workManager = WorkManager.getInstance(context.applicationContext)
            workManager.enqueue(checkNetworkConnectionJob)
        }
    }

    private fun isWorkScheduled(context: Context, workTag: String): Boolean {
        return WorkManagerUtil.isWorkScheduled(context.applicationContext, workTag)
    }

    private fun haveNetworkConnection(context: Context): Boolean {
        var status = false

        val cm =
            context.getSystemService(JobIntentService.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (cm.activeNetwork != null && cm.getNetworkCapabilities(cm.activeNetwork) != null) {
                status = true
            }
        } else {
            if (cm.activeNetworkInfo != null && cm.activeNetworkInfo!!.isConnectedOrConnecting) {
                status = true
            }
        }

        return status
    }
}