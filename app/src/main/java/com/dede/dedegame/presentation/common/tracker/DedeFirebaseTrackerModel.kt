package com.dede.dedegame.presentation.common.tracker

import android.os.Bundle

open class DedeFirebaseTrackerModel: ITrackerModel {
    open var screenName: String? = null
    open var eventName: String? = null

    override fun createParams(bundle: Bundle){}
}