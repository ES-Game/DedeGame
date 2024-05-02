package com.quangph.jetpack.workmanager

import android.content.Context
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.google.common.util.concurrent.ListenableFuture
import java.util.concurrent.ExecutionException

/**
 * Created by QuangPH on 4/16/21.
 */
object WorkManagerUtil {
    fun isWorkScheduled(context: Context, workTag: String): Boolean {
        val workManager = WorkManager.getInstance(context)

        val statuses: ListenableFuture<List<WorkInfo>> = workManager.getWorkInfosByTag(workTag)
        return try {
            var running = false
            val workInfoList: List<WorkInfo> = statuses.get()
            for (workInfo in workInfoList) {
                val state = workInfo.state
                running = (state == WorkInfo.State.RUNNING) or (state == WorkInfo.State.ENQUEUED)
            }
            running
        } catch (e: ExecutionException) {
            e.printStackTrace()
            false
        } catch (e: InterruptedException) {
            e.printStackTrace()
            false
        }
    }
}