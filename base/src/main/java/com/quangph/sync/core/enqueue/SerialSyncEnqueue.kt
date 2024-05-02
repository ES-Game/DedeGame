package com.quangph.sync.core.enqueue

import com.quangph.sync.core.action.SyncAction
import com.quangph.base.thread.SerialExecutor

/**
 * Created by QuangPH on 2021-02-19.
 */
class SerialSyncEnqueue: ISyncEnqueue {

    private val serialExecutor = SerialExecutor()

    override fun enqueue(syncAction: SyncAction) {
        serialExecutor.execute {
            syncAction.sync()
        }
    }
}