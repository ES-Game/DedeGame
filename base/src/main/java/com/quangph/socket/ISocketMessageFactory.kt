package com.quangph.socket

interface ISocketMessageFactory {
    fun create(msg: String) : SocketMessage?
}