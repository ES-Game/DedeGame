package com.quangph.jetpack.alert

/**
 * Created by QuangPH on 2020-11-26.
 */
interface IAlert {
    fun show(msg: String?, type: String)
    fun dismiss()
    fun destroy()
}