package com.quangph.jetpack.print.core.wifi

import com.quangph.jetpack.print.core.IPrinter
import com.quangph.jetpack.print.core.stream.StreamingPrinter
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket

/**
 * Created by QuangPH on 2020-08-26.
 */
class WifiPrinter(private val address: String): IPrinter<ByteArray> {
    private val PORT = 9100
    private val TIME_OUT = 10000

    override fun print(data: ByteArray, numberOfCopy: Int): Boolean {
        val socket = Socket()
        val inetSocketAddress = InetSocketAddress(address, PORT)
        try {
            socket.connect(inetSocketAddress, TIME_OUT)
            socket.sendBufferSize = StreamingPrinter.CHUNK_SIZE
            return if (socket.isConnected) {
                val printer = StreamingPrinter(socket.getOutputStream())
                printer.print(data, numberOfCopy)
            } else {
                false
            }
        } catch (e: IOException) {
            e.printStackTrace()
            return false
        } catch (e: InterruptedException) {
            e.printStackTrace()
            return false
        } finally {
            try {
                socket.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}