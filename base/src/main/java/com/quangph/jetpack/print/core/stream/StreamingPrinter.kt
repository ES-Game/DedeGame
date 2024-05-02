package com.quangph.jetpack.print.core.stream

import com.quangph.jetpack.print.core.IPrinter
import java.io.IOException
import java.io.OutputStream

/**
 * Created by QuangPH on 2020-08-26.
 */
open class StreamingPrinter(private val outputStream: OutputStream): IPrinter<ByteArray> {

    companion object {
        const val CHUNK_SIZE = 1024
    }

    override fun print(data: ByteArray, numberOfCopy: Int): Boolean {
        try {
            for (j in 0 until numberOfCopy) {
                var i = 0
                while (i < data.size) {
                    outputStream.write(data, i, Math.min(CHUNK_SIZE, data.size - i))
                    outputStream.flush()
                    i += CHUNK_SIZE
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