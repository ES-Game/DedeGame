package com.quangph.socket

interface IOnSocketCallback {
    fun onError()
    fun onConnected()
    fun onReconnect()
}