package com.quangph.jetpack.print.core.aidl

import android.graphics.Bitmap

interface IAIDLPrinterService {
    fun printerInit(callback: IAIDLPrinterCallback?)
    fun printBitmap(bitmap: Bitmap, callback: IAIDLPrinterCallback?)
    fun printRawData(data: ByteArray, callback: IAIDLPrinterCallback?)
    fun lineWrap(count: Int, callback: IAIDLPrinterCallback?)
    fun cutPaper(callback: IAIDLPrinterCallback?)
}