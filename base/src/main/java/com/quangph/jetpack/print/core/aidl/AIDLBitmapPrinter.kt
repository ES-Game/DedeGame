package com.quangph.jetpack.print.core.aidl

import android.graphics.Bitmap
import android.os.RemoteException
import com.quangph.jetpack.print.core.IPrinter
import java.util.*

/**
 * Printer for some device which integrate printer inside by aidl.
 * Ex: Sunmi device
 * Created by QuangPH on 2020-08-25.
 */
class AIDLBitmapPrinter(private val service: IAIDLPrinterService): IPrinter<Bitmap> {
    override fun print(data: Bitmap, numberOfCopy: Int): Boolean {
        return try {
            val splits = splitBitmap(data)
            for (j in 0 until numberOfCopy) {
                service.printerInit(null)
                for (k in splits!!.indices) {
                    val segment = splits[k]
                    service.printBitmap(segment, null)
                    Thread.sleep(1000)
                }
                service.lineWrap(3, null)
                service.cutPaper(null)
            }
            true
        } catch (e: RemoteException) {
            e.printStackTrace()
            false
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    private fun splitBitmap(source: Bitmap): List<Bitmap>? {
        val max = 1000
        val split: MutableList<Bitmap> = ArrayList()
        var i = 0
        while (i < source.height) {
            val b = Bitmap.createBitmap(source, 0, i, source.width, Math.min(max, source.height - i))
            split.add(b)
            i += max
        }
        return split
    }
}