package com.quangph.jetpack.print.dataadapter.tsc

import android.graphics.*
import com.quangph.jetpack.print.dataadapter.IPrinterDataAdapter
import java.io.UnsupportedEncodingException
import java.util.*
import kotlin.experimental.or

/**
 * Created by QuangPH on 2020-08-26.
 */
class TSC_PrinterDataAdapter: IPrinterDataAdapter<Bitmap, List<ByteArray?>> {

    private val charsetName = "gbk"

    override fun generateData(source: Bitmap): List<ByteArray> {
        val list = ArrayList<ByteArray>()
        list.add(cls())
        list.add(sizeBymm(75.0, 50.88))
        list.add(gapBymm(2.25, 0.0))
        list.add(direction(1))
        list.add(cls())
        list.add(bitmap(0, 0, 0, source))
        list.add(print(1))
        return list
    }

    private fun cls(): ByteArray {
        val str = "CLS\n"
        return strTobytes(str)
    }

    private fun sizeBymm(m: Double, n: Double): ByteArray {
        val str = "SIZE $m mm,$n mm\n"
        return strTobytes(str)
    }

    private fun gapBymm(m: Double, n: Double): ByteArray {
        val str = "GAP $m mm,$n mm\n"
        return strTobytes(str)
    }

    private fun direction(n: Int): ByteArray {
        val str = "DIRECTION $n\n"
        return strTobytes(str)
    }

    /**/
    private fun bitmap(x: Int, y: Int, mode: Int, bitmap: Bitmap): ByteArray {
        val width = (bitmap.width + 7) / 8
        val heigth = bitmap.height
        val str = "BITMAP $x,$y,$width,$heigth,$mode,"
        val end = "\n"
        val ended: ByteArray? = strTobytes(end)
        val head: ByteArray? = strTobytes(str)
        var data: ByteArray? = downLoadBmpToSendTSCData(bitmap)
        if (ended != null && head != null && data != null) {
            data = byteMerger(head, data)
            data = byteMerger(data, ended)
        }

        return data ?: ("").toByteArray(charset(charsetName))
    }

    private fun downLoadBmpToSendTSCData(bitmap: Bitmap): ByteArray? {
        var bitmap: Bitmap = toGrayscale(bitmap)
        bitmap = convertGreyImg(bitmap)
        val width = bitmap.width
        val height = bitmap.height
        val n = (width + 7) / 8
        val h = (height + 7) / 8
        val pixels = IntArray(width * height)
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height)
        return getbmpdataTsc(pixels, width, height)
    }

    private fun toGrayscale(bmpOriginal: Bitmap): Bitmap {
        val height = bmpOriginal.height
        val width = bmpOriginal.width
        val bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
        val c = Canvas(bmpGrayscale)
        val paint = Paint()
        val cm = ColorMatrix()
        cm.setSaturation(0.0f)
        val f = ColorMatrixColorFilter(cm)
        paint.colorFilter = f
        c.drawBitmap(bmpOriginal, 0.0f, 0.0f, paint)
        return bmpGrayscale
    }

    private fun convertGreyImg(img: Bitmap): Bitmap {
        val width = img.width
        val height = img.height
        val pixels = IntArray(width * height)
        img.getPixels(pixels, 0, width, 0, 0, width, height)
        var redSum = 0.0
        val total = (width * height).toDouble()
        var m: Int
        var i: Int
        var j: Int
        var grey: Int
        m = 0
        while (m < height) {
            i = 0
            while (i < width) {
                j = pixels[width * m + i]
                grey = j and 16711680 shr 16
                redSum += grey.toDouble()
                ++i
            }
            ++m
        }
        m = (redSum / total).toInt()
        i = 0
        while (i < height) {
            j = 0
            while (j < width) {
                grey = pixels[width * i + j]
                val alpha1 = -16777216
                var red = grey and 16711680 shr 16
                var green = grey and '\uff00'.toInt() shr 8
                var blue = grey and 255
                if (red >= m) {
                    blue = 255
                    green = 255
                    red = 255
                } else {
                    blue = 0
                    green = 0
                    red = 0
                }
                grey = alpha1 or (red shl 16) or (green shl 8) or blue
                pixels[width * i + j] = grey
                ++j
            }
            ++i
        }
        val mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
        mBitmap.setPixels(pixels, 0, width, 0, 0, width, height)
        return mBitmap
    }

    private fun getbmpdataTsc(b: IntArray, w: Int, h: Int): ByteArray? {
        val n = (w + 7) / 8
        val data = ByteArray(n * h)
        val mask: Byte = 1
        for (y in 0 until h) {
            for (x in 0 until n * 8) {
                if (x < w) {
                    if (b[y * w + x] and 16711680 shr 16 != 0) {
                        data[y * n + x / 8] = data[y * n + x / 8] or (mask.toInt() shl 7 - x % 8).toByte()
                    }
                } else if (x >= w) {
                    data[y * n + x / 8] = data[y * n + x / 8] or (mask.toInt() shl 7 - x % 8).toByte()
                }
            }
        }
        return data
    }

    private fun byteMerger(byte_1: ByteArray, byte_2: ByteArray): ByteArray {
        val byte_3 = ByteArray(byte_1.size + byte_2.size)
        System.arraycopy(byte_1, 0, byte_3, 0, byte_1.size)
        System.arraycopy(byte_2, 0, byte_3, byte_1.size, byte_2.size)
        return byte_3
    }

    private fun print(m: Int): ByteArray {
        val str = "PRINT $m\n"
        return strTobytes(str)
    }

    private fun strTobytes(str: String): ByteArray {
        var data: ByteArray? = null
        try {
            val b = str.toByteArray(charset("utf-8"))
            data = String(b, charset("utf-8")).toByteArray(charset(charsetName))
        } catch (var4: UnsupportedEncodingException) {
            var4.printStackTrace()
        }
        return data ?: ("").toByteArray(charset(charsetName))
    }
}