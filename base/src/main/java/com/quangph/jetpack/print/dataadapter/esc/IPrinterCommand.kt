package com.quangph.jetpack.print.dataadapter.esc

import java.io.OutputStream

/**
 * Created by QuangPH on 2020-08-26.
 */
interface IPrinterCommand {
    fun initPrinter(os: OutputStream)
    fun setLineFeedAmount(os: OutputStream, amount: Byte)
    fun printBitmap(os: OutputStream, width: Int, height: Int)
    fun printLine(os: OutputStream, data: ByteArray)
    fun printNewLine(os: OutputStream)
    fun printEnd(os: OutputStream)
    fun cutPage(os: OutputStream)
}