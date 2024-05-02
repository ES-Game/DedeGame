package com.quangph.sync.core.action

/**
 * Created by QuangPH on 2021-02-01.
 */
abstract class SyncAction {

    var callback: SyncCallback? = null

    abstract fun onSync(): Any?
    abstract fun getType(): String

    fun onStart(){}
    fun onSuccess(result: Any?){}
    fun onError(t: Throwable){}

    open fun getRetryCount(): Int {
        return 1
    }

    open fun getId(): String {
        return SyncAction::class.java.name
    }

    open fun sync() {
        var retry = 1
        onStart()
        callback?.onStart(this)
        while (retry <= getRetryCount()) {
            try {
                val result = onSync()
                onSuccess(result)
                callback?.onSuccess(this, result)
                break
            } catch (t: Throwable) {
                if (retry < getRetryCount()) {
                    retry++
                } else {
                    onError(t)
                    callback?.onError(this, t)
                }
            }
        }
    }

    fun publishProgress(progress: Any?) {
        callback?.onPublishProgress(this, progress)
    }

    fun match(other: SyncAction): Boolean {
        return other.getId() == this.getId()
    }


    interface SyncCallback {
        fun onStart(action: SyncAction)
        fun onSuccess(action: SyncAction, result: Any?)
        fun onError(action: SyncAction, t: Throwable)
        fun onPublishProgress(action: SyncAction, progress: Any?)
    }
}