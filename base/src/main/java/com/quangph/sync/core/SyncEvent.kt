package com.quangph.sync.core

/**
 * Created by QuangPH on 2021-02-08.
 */
class SyncEvent {
    companion object {
        const val START = "START"
        const val UPDATE_PROGRESS = "UPDATE_PROGRESS"
        const val SUCCESS = "SUCCESS"
        const val ERROR = "ERROR"
        const val FINISH_ALL = "FINISH_ALL"
    }

    var type: String = ""
    var actionType: String = ""
    var actionId: String = ""
    var payload: Any? = null
}