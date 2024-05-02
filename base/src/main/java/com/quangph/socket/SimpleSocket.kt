package com.quangph.socket

abstract class SimpleSocket: ISocket {
    var msgFactory: ISocketMessageFactory? = null
    var msgPublisher: ISocketPublisher? = null
    var msgSocketCallback: IOnSocketCallback? = null

    override fun setMessageFactory(factory: ISocketMessageFactory) {
        msgFactory = factory
    }

    override fun setPublisher(publisher: ISocketPublisher) {
        msgPublisher = publisher
    }

    override fun setSocketCallback(callback: IOnSocketCallback?) {
        msgSocketCallback = callback
    }

    override fun disconnect() {
        msgSocketCallback = null
        msgPublisher = null
        msgFactory = null
    }
}