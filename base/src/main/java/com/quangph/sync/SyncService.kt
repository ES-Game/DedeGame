package com.quangph.sync

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.quangph.sync.core.SyncManager
import com.quangph.sync.core.action.SyncAction
import com.quangph.sync.core.emitter.ErrorEvent
import com.quangph.sync.core.emitter.ProgressEvent
import com.quangph.eventbus.IEvent
import com.quangph.eventbus.IEventHandler
import com.quangph.eventbus.PendingEventBus


/**
 * Created by QuangVH on 3/15/2021.
 */
abstract class SyncService: Service(), IEventHandler {
    companion object {
        const val CHANNEL_ID = "SyncChannel"
        const val SYNC_STARTED_ACTION = "SYNC_STARTED_ACTION"
        const val NOTIFICATION_ID = 100
    }
    private val throwableMap = hashMapOf<String, Throwable>()
    private val progressMap = hashMapOf<String, Float>()
    private var config: SyncServiceConfig? = null

    override fun onCreate() {
        super.onCreate()
        PendingEventBus.getInstance().register(this)
        PendingEventBus.getInstance().notifyActive(this)
        config = getConfig()
        startSyncService()
        sendBroadcast(Intent().apply {
            this.action = SYNC_STARTED_ACTION
        })
    }

    override fun onBind(intent: Intent?): IBinder {
        return SyncBinder()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        submitSyncTask(getSyncAction(intent))
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        PendingEventBus.getInstance().unregister(this)
        throwableMap.forEach {
            it.value.printStackTrace()
        }
    }

    override fun onEvent(event: IEvent?) {
        when (event) {
            is ProgressEvent -> {
                updateProgress(event.type, event.progress as Float)
                if (event.progress == 100f) {
                    finishIfDone()
                }
            }
            is ErrorEvent -> {
                throwableMap[event.type] = event.t
                finishIfDone()
            }
        }
    }

    abstract fun getConfig(): SyncServiceConfig
    abstract fun getSyncAction(intent: Intent?): Map<String, SyncAction>
    fun onFinishAll(){}
    fun onSyncProgressTitle(eventType: String, eventProgress: Float): String {
        return ""
    }

    private fun startSyncService() {
        createNotificationChannel()
        startForeground(NOTIFICATION_ID, createSyncNotification(""))
    }

    private fun createSyncNotification(contentText: String): Notification {
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(config?.contentTitle ?: "")
            .setContentText(contentText)

        if (config?.smallIconDrawable != null) {
            builder.setSmallIcon(config?.smallIconDrawable!!)
        }
        return builder.build()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                config?.notificationChannelName,
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(
                NotificationManager::class.java
            )
            manager.createNotificationChannel(serviceChannel)
        }
    }

    private fun updateProgress(eventType: String, eventProgress: Float) {
        progressMap[eventType] = eventProgress
        updateNotification(eventType, eventProgress)
    }

    private fun updateNotification(eventType: String, eventProgress: Float) {
        val updateNotification = createSyncNotification(onSyncProgressTitle(eventType, eventProgress))
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID, updateNotification)
    }

    private fun submitSyncTask(syncActionMap: Map<String, SyncAction>) {
        for ((jobType, jobAction) in syncActionMap) {
            SyncManager.initSync(jobType)
            SyncManager.submit(jobAction)
        }
    }

    private fun finishIfDone() {
        var isDone = true
        progressMap.forEach {
            val isJobDone = (it.value == 100f || throwableMap.containsKey(it.key))
            isDone = (isDone && isJobDone)
        }
        if (isDone) {
            onFinishAll()
            SyncSharedPreference.setSyncProgress(this, progressMap)
            updateSyncTime(System.currentTimeMillis())
            stopSelf()
        }
    }

    private fun updateSyncTime(syncTime: Long) {
        if (syncTime > SyncSharedPreference.getLatestSyncTime(this)) {
            SyncSharedPreference.setLatestSyncTime(this, syncTime)
        }
    }


    inner class SyncBinder: Binder() {
        fun getProgressMap(): HashMap<String, Float> = this@SyncService.progressMap
    }
}
