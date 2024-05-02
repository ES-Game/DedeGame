package com.quangph.sync.core.enqueue

import com.quangph.sync.core.action.SyncAction

/**
 * Created by QuangPH on 2021-02-01.
 */
interface ISyncEnqueue {
    fun enqueue(syncAction: SyncAction)
}