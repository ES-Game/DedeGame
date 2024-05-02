package com.quangph.jetpack.print.core.aidl

interface IAIDLPrinterCallback {
    fun onCallback(isSuccess: Boolean, code: Int, msg: String)
}