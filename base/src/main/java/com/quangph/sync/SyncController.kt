package com.quangph.sync

import android.app.NotificationManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat

/**
 * Sync gateway
 * Created by QuangPH on 4/24/21.
 */
class SyncController: ISyncController {

    private var syncServiceConnection: ServiceConnection? = null
    private var callback: IBindCallback? = null

    override fun start(context: Context, intent: Intent?) {
        val serviceIntent = Intent(context.applicationContext, SyncService::class.java).apply {
            initSyncService(this)
        }
        ContextCompat.startForegroundService(context.applicationContext, serviceIntent)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun bindToSync(context: Context, callback: IBindCallback?) {
        this.callback = callback
        if (isSyncServiceRunning(context)) {
            bindSyncService(context)
        } else {
            val savedProgressMap = SyncSharedPreference.getSyncProgress(context)
            callback?.bind(savedProgressMap)
        }
    }

    override fun unbind(context: Context) {
        syncServiceConnection?.let {
            context.applicationContext.unbindService(it)
        }
        syncServiceConnection = null
        callback = null
    }

    fun initSyncService(intent: Intent){}

    @RequiresApi(Build.VERSION_CODES.M)
    private fun isSyncServiceRunning(context: Context): Boolean {
        val activeNotificationList = ContextCompat.getSystemService(
            context,
            NotificationManager::class.java
        )?.activeNotifications

        if (!activeNotificationList.isNullOrEmpty()) {
            for (notification in activeNotificationList) {
                if (notification.id == SyncService.NOTIFICATION_ID) {
                    return true
                }
            }
        }

        return false
    }

    private fun bindSyncService(context: Context) {
        syncServiceConnection = object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                val progressMap = (service as? SyncService.SyncBinder)?.getProgressMap()
                if (progressMap != null) {
                    callback?.bind(progressMap)
                }
            }

            override fun onServiceDisconnected(name: ComponentName?) {
                callback?.syncDone()
            }
        }
        val syncServiceIntent = Intent(context, SyncService::class.java)
        context.bindService(syncServiceIntent, syncServiceConnection!!, 0)
    }
}