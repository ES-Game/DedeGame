package com.quangph.jetpack.print.core.aidl

import android.os.RemoteException
import com.quangph.jetpack.print.core.IPrinter

/**
 * Created by QuangPH on 2020-08-25.
 */
class AIDLRawDataPrinter(private val service: IAIDLPrinterService): IPrinter<ByteArray> {
    override fun print(data: ByteArray, numberOfCopy: Int): Boolean {
        return try {
            for (j in 0 until numberOfCopy) {
                service.printerInit(null)
                service.printRawData(data, null)
                service.lineWrap(2, null)
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
}