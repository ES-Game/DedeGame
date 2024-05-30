package com.dede.dedegame

import androidx.lifecycle.LifecycleObserver
import androidx.multidex.MultiDexApplication
import com.google.firebase.analytics.FirebaseAnalytics

class DedeApp : MultiDexApplication(), LifecycleObserver {

    override fun onCreate() {
        super.onCreate()
        instance = this
        firebaseAnalytics = FirebaseAnalytics.getInstance(this)
    }

    companion object {
        private lateinit var instance: DedeApp
        private lateinit var firebaseAnalytics: FirebaseAnalytics

        fun getInstance(): DedeApp {
            return instance
        }

        fun getFirebaseAnalytics(): FirebaseAnalytics {
            return firebaseAnalytics
        }
    }
}