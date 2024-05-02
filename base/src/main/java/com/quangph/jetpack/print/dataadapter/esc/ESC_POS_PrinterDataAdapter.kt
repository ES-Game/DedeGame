package com.quangph.jetpack.print.dataadapter.esc

import android.graphics.Bitmap
import com.quangph.jetpack.print.dataadapter.IPrinterDataAdapter
import java.io.ByteArrayOutputStream
import java.util.*
import kotlin.experimental.or

/**
 * Created by QuangPH on 2020-08-26.
 */
class ESC_POS_PrinterDataAdapter(private val command: IPrinterCommand) :
    IPrinterDataAdapter<Bitmap, ByteArray> {
    override fun generateData(source: Bitmap): ByteArray {
        // BEGIN
        val os = ByteArrayOutputStream()
        //val command = ESC_POS_Command(os)
        command.initPrinter(os)
        command.setLineFeedAmount(os, 24)

        val imageBits = getBitsImageData(source)
        var offset = 0
        while (offset < source.height) {
            command.printBitmap(os, source.width, source.height)
            var imageDataLineIndex = 0
            val imageDataLine = ByteArray(3 * source.width)
            for (x in 0 until source.getWidth()) {
                // Remember, 24 dots = 24 bits = 3 bytes.
                // The 'k' variable keeps track of which of those
                // three bytes that we're currently scribbling into.
                for (k in 0..2) {
                    var slice: Byte = 0
                    // A byte is 8 bits. The 'b' variable keeps track
                    // of which bit in the byte we're recording.
                    for (b in 0..7) {
                        // Calculate the y position that we're currently
                        // trying to draw. We take our offset, divide it
                        // by 8 so we're talking about the y offset in
                        // terms of bytes, add our current 'k' byte
                        // offset to that, multiple by 8 to get it in terms
                        // of bits again, and add our bit offset to it.
                        val y = (offset / 8 + k) * 8 + b
                        // Calculate the location of the pixel we want in the bit array.
                        // It'll be at (y * width) + x.
                        val i: Int = y * source.getWidth() + x
                        // If the image (or this stripe of the image)
                        // is shorter than 24 dots, pad with zero.
                        var v = false
                        if (i < imageBits.length()) {
                            v = imageBits.get(i)
                        }
                        // Finally, store our bit in the byte that we're currently
                        // scribbling to. Our current 'b' is actually the exact
                        // opposite of where we want it to be in the byte, so
                        // subtract it from 7, shift our bit into place in a temp
                        // byte, and OR it with the target byte to get it into there.
                        slice = slice or ((if (v) 1 else 0) shl 7 - b).toByte()
                    }
                    imageDataLine[imageDataLineIndex + k] = slice
                    // Phew! Write the damn byte to the buffer
                }
                imageDataLineIndex += 3
            }
            command.printLine(os, imageDataLine)
            offset += 24
            command.printNewLine(os)
        }
        command.printEnd(os)
        command.cutPage(os)
        return os.toByteArray()
    }

    private fun getBitsImageData(image: Bitmap): BitSet {
        val threshold = 160
        var index = 0
        val dimenssions = image.width * image.height
        val imageBitsData = BitSet(dimenssions)
        for (y in 0 until image.height) {
            for (x in 0 until image.width) {
                val color = image.getPixel(x, y)
                val red = color and 0x00ff0000 shr 16
                val green = color and 0x0000ff00 shr 8
                val blue = color and 0x000000ff
                //int luminance = (int) (red * 0.3 + green * 0.59 + blue * 0.11);
                //dots[index] = (luminance < threshold);
                //imageBitsData.set(index, (luminance < threshold));
                imageBitsData[index] = red <= 160 || green <= 160 || blue <= 160
                index++
            }
        }
        return imageBitsData
    }
}