package com.dede.dedegame

import androidx.lifecycle.LifecycleObserver
import androidx.multidex.MultiDexApplication

class DedeApp : MultiDexApplication(), LifecycleObserver {

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        private lateinit var instance: DedeApp
        fun getInstance(): DedeApp {
            return instance
        }
    }
}