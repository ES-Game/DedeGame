package com.quangph.sync.core.progresscalculator

import com.quangph.sync.core.SyncResult

/**
 * Created by QuangPH on 2021-02-08.
 */
interface IProgressCalculator {
    fun calculateProgress(syncResultCache: MutableMap<String, SyncResult>): Float
}