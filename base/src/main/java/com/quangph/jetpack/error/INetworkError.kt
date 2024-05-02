package com.quangph.jetpack.error

import com.quangph.jetpack.IJetContext

/**
 * Created by QuangPH on 2020-06-04.
 */
interface INetworkError {
    fun onNetworkConnected()
    fun onNetworkError(context: IJetContext)
}