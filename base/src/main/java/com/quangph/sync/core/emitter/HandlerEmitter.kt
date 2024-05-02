package com.quangph.sync.core.emitter

import android.os.Handler
import android.os.Looper
import android.os.Message
import com.quangph.eventbus.PendingEventBus
import com.quangph.sync.core.SyncEvent

/**
 * Created by QuangPH on 2021-02-08.
 */
class HandlerEmitter: IEmitter {

    private val handler = EventHandler(Looper.getMainLooper())

    @Synchronized
    override fun emit(event: Any?) {
        handler.sendMessage(handler.obtainMessage(1, event))
    }


    private class EventHandler(looper: Looper): Handler(looper) {

        override fun handleMessage(msg: Message) {
            val event: SyncEvent? = msg.obj as SyncEvent?
            if (event != null) {
                if (event.type == SyncEvent.UPDATE_PROGRESS) {
                    PendingEventBus.getInstance().post(
                        ProgressEvent(event.actionType,
                        event.actionId, event.payload)
                    )
                } else if (event.type == SyncEvent.ERROR) {
                    PendingEventBus.getInstance().post(
                        ErrorEvent(event.actionType,
                        event.actionId, event.payload as Throwable)
                    )
                } else if (event.type == SyncEvent.FINISH_ALL) {
                    PendingEventBus.getInstance().post(FinishAllEvent())
                }
            }
        }
    }
}