package com.quangph.sync.core.emitter

import com.quangph.eventbus.IEvent


/**
 * Created by QuangPH on 2021-02-08.
 */

class ProgressEvent(val type: String, val id: String, val progress: Any?): IEvent
class ErrorEvent(val type: String, val id: String, val t: Throwable): IEvent
class FinishAllEvent: IEvent