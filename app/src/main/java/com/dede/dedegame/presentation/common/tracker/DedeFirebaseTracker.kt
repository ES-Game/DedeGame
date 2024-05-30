package com.dede.dedegame.presentation.common.tracker

import android.os.Bundle
import com.dede.dedegame.DedeApp

object DedeFirebaseTracker: ITracker<DedeFirebaseTrackerModel> {

    override fun track(model: DedeFirebaseTrackerModel) {
        val bundle = Bundle()
        bundle.putString("screen_name", model.screenName ?: "")
        model.createParams(bundle)
        model.eventName?.let {
            DedeApp.getFirebaseAnalytics().logEvent(it, bundle)
        }
    }
}