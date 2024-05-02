package com.quangph.sync.core

import android.util.Log
import com.quangph.sync.core.progresscalculator.DefaultProgressCalculator
import com.quangph.sync.core.progresscalculator.IProgressCalculator
import java.util.concurrent.ConcurrentHashMap

/**
 * Created by QuangPH on 2021-02-08.
 */

class SyncResultManager {

    var progressCalculator: IProgressCalculator = DefaultProgressCalculator()

    private val syncResultCache = ConcurrentHashMap<String, SyncResult>()

    fun getProgress(): Float {
        return progressCalculator.calculateProgress(syncResultCache)
    }

    fun addSyncResult(id: String): Boolean {
        return if (!syncResultCache.containsKey(id)) {
            syncResultCache[id] = SyncResult()
            true
        } else {
            Log.e("SyncResultManager", "The task has already running with id: $id")
            false
        }
    }

    fun updateResult(id: String, any: Any?) {
        val result = syncResultCache[id]
        if (result != null) {
            result.data = any
        }
    }

    fun updateError(id: String, t: Throwable) {
        val result = syncResultCache[id]
        if (result != null) {
            result.error = t
        }
    }

    fun updateProgress(id: String, progress: Any?) {
        val result = syncResultCache[id]
        if (result != null) {
            result.currentProgress = progress
        }
    }

    fun remove(id: String) {
        syncResultCache.remove(id)
    }

    fun isEmpty(): Boolean {
        return syncResultCache.isEmpty()
    }
}