package com.quangph.jetpack.print.core.stream

import com.quangph.jetpack.print.core.IPrinter
import java.io.IOException
import java.io.OutputStream
import kotlin.math.min

/**
 * Created by QuangPH on 2020-08-26.
 */
class StreamingListByteArrPrinter(private val outputStream: OutputStream):
    IPrinter<List<ByteArray>> {

    companion object {
        const val CHUNK_SIZE = 1024
    }

    override fun print(data: List<ByteArray>, numberOfCopy: Int): Boolean {
        try {
            for (j in 0 until numberOfCopy) {
                data.forEach {
                    var i = 0
                    while (i < it.size) {
                        outputStream.write(it, i, min(StreamingPrinter.CHUNK_SIZE, it.size - i))
                        outputStream.flush()
                        i += CHUNK_SIZE
                    }
                }
            }
            Thread.sleep(1000)
            return true
        } catch (e: IOException) {
            e.printStackTrace()
            return false
        } catch (e: InterruptedException) {
            return false
        } finally {
            try {
                outputStream.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}