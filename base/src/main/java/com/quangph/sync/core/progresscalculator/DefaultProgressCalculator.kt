package com.quangph.sync.core.progresscalculator

import com.quangph.sync.core.SyncResult
import kotlin.math.max

/**
 * Created by QuangPH on 2021-02-08.
 */
class DefaultProgressCalculator: IProgressCalculator {

    private var currentProgress: Float = 0f

    override fun calculateProgress(syncResultCache: MutableMap<String, SyncResult>): Float {
        val maxProgress = syncResultCache.size * 100f
        var totalProgress = 0f
        syncResultCache.values.forEach {
            if (it.currentProgress != null && it.currentProgress is Float) {
                totalProgress += it.currentProgress as Float
            }
        }

        var realProgress = (totalProgress / maxProgress) * 100
        realProgress = max(currentProgress, realProgress)
        currentProgress = realProgress
        return realProgress
    }
}