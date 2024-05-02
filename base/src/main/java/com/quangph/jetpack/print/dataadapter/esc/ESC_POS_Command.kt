package com.quangph.jetpack.print.dataadapter.esc

import java.io.OutputStream

/**
 * Created by QuangPH on 2020-08-26.
 */
class ESC_POS_Command : IPrinterCommand {
    override fun initPrinter(os: OutputStream) {
        os.write(byteArrayOf(27, 64))
    }

    override fun setLineFeedAmount(os: OutputStream, amount: Byte) {
        os.write(byteArrayOf(0x1B, 0x33, amount))
    }

    override fun printBitmap(os: OutputStream, width: Int, height: Int) {
        val widthLSB = (width and 0xFF).toByte()
        val widthMSB = (width shr 8 and 0xFF).toByte()
        os.write(byteArrayOf(0x1B, 0x2A, 33, widthLSB, widthMSB))
    }

    override fun printLine(os: OutputStream, data: ByteArray) {
        os.write(data)
    }

    override fun printNewLine(os: OutputStream) {
        os.write(0x0A)
    }

    override fun printEnd(os: OutputStream) {
        os.write(byteArrayOf(27, 100, 4))
    }

    override fun cutPage(os: OutputStream) {
        os.write(byteArrayOf(0x1D, 0x56, 0x41, 0x10))
    }

}