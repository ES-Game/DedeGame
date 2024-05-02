package com.quangph.socket

/**
 * Publish message from socket.
 * NOTE: We don't care about what thread publishing the msg, so the message can be published from background thread
 */
interface ISocketPublisher {
    fun publish(msgObj: SocketMessage?)
    fun release()
}