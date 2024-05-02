package com.quangph.jetpack.network

import android.content.Context

/**
 * Created by QuangVH on 3/22/2021.
 */

interface INetworkManager {
    fun startCheckNetwork(context: Context)
    fun isAvailable(context: Context): Boolean
    fun setNotifyNetworkStatusCallback(callback: (NETWORK_STATUS) -> Unit)
}

enum class NETWORK_STATUS {
    AVAILABLE, UNAVAILABLE, SLOW
}