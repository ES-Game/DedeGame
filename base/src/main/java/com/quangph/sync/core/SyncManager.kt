package com.quangph.sync.core

import com.quangph.sync.core.action.SyncAction
import com.quangph.sync.core.emitter.HandlerEmitter
import com.quangph.sync.core.emitter.IEmitter
import com.quangph.sync.core.enqueue.ISyncEnqueue
import com.quangph.sync.core.enqueue.SerialSyncEnqueue
import java.util.concurrent.ConcurrentHashMap

/**
 * Created by QuangPH on 2021-01-29.
 */
object SyncManager {

    private const val TAG = "SyncManager"

    var enqueue: ISyncEnqueue = SerialSyncEnqueue()

    private val sharedEmitter = HandlerEmitter()
    private val syncResultCache = ConcurrentHashMap<String, SyncResultManager>()

    fun initSync(type: String) {
        if (syncResultCache.containsKey(type)) {
            val progress = syncResultCache[type]!!.getProgress()
            sharedEmitter.emit(SyncEvent().apply {
                this.type = SyncEvent.UPDATE_PROGRESS
                this.actionType = type
                this.payload = progress
            })
        } else {
            syncResultCache[type] =
                SyncResultManager()
            sharedEmitter.emit(SyncEvent().apply {
                this.type = SyncEvent.UPDATE_PROGRESS
                this.actionType = type
                this.payload = 0f
            })
        }
    }

    fun submit(client: SyncAction) {
        submit(client.getType(), client)
    }

    fun submit(type: String, client: SyncAction?) {
        if (client == null) {
            val progress = syncResultCache[type]!!.getProgress()
            sharedEmitter.emit(SyncEvent().apply {
                this.type = SyncEvent.UPDATE_PROGRESS
                this.actionType = type
                payload = progress
            })
        } else {
            var syncResultMng = syncResultCache[type]
            if (syncResultMng == null) {
                syncResultMng = SyncResultManager()
                syncResultCache[type] = syncResultMng
            }
            if (syncResultMng.addSyncResult(client.getId())) {
                client.callback =
                    WrapSyncCallback(
                        sharedEmitter
                    )
                enqueue.enqueue(client)
            }
        }
    }

    private fun updateResult(syncAction: SyncAction, any: Any?) {
        syncResultCache[syncAction.getType()]?.updateResult(syncAction.getId(), any)
    }

    private fun updateError(syncAction: SyncAction, t: Throwable) {
        syncResultCache[syncAction.getType()]?.updateError(syncAction.getId(), t)
    }

    private fun updateProgress(syncAction: SyncAction, progress: Any?) {
        syncResultCache[syncAction.getType()]?.updateProgress(syncAction.getId(), progress)
    }

    private fun removeSyncActionIfNeed(syncAction: SyncAction) {
        val manager = syncResultCache[syncAction.getType()]
        manager?.remove(syncAction.getId())
        if (manager?.isEmpty() == true) {
            syncResultCache.remove(syncAction.getType())
        }
    }

    private fun isFinishedAll(): Boolean {
        return syncResultCache.isEmpty()
    }


    private class WrapSyncCallback(private val emitter: IEmitter): SyncAction.SyncCallback {
        override fun onStart(action: SyncAction) {
            emitter.emit(SyncEvent().apply {
                this.type = SyncEvent.START
            })
        }

        override fun onSuccess(action: SyncAction, result: Any?) {
            updateResult(action, result)
            removeSyncActionIfNeed(action)
            emitter.emit(SyncEvent().apply {
                this.type = SyncEvent.SUCCESS
                this.payload = result
            })
            if (isFinishedAll()) {
                emitter.emit(SyncEvent().apply {
                    this.type = SyncEvent.FINISH_ALL
                })
            }
        }

        override fun onError(action: SyncAction, t: Throwable) {
            updateError(action, t)
            removeSyncActionIfNeed(action)
            emitter.emit(SyncEvent().apply {
                this.actionType = action.getType()
                this.type = SyncEvent.ERROR
                this.payload = t
            })

            if (isFinishedAll()) {
                emitter.emit(SyncEvent().apply {
                    this.type = SyncEvent.FINISH_ALL
                })
            }
        }

        override fun onPublishProgress(action: SyncAction, progress: Any?) {
            updateProgress(
                action,
                progress
            )
            emitter.emit(SyncEvent().apply {
                this.type = SyncEvent.UPDATE_PROGRESS
                this.actionType = action.getType()
                this.actionId = action.getId()
                this.payload = syncResultCache[action.getType()]?.getProgress()
            })
        }
    }
}