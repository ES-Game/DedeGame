package com.quangph.socket

interface ISocket {
    fun connect()
    fun disconnect()
    fun setMessageFactory(factory: ISocketMessageFactory)
    fun setPublisher(publisher: ISocketPublisher)
    fun setSocketCallback(callback: IOnSocketCallback?)
    fun emit(key: String, vararg params: Any?)
}