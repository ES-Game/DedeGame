package com.quangph.jetpack.utils

import android.graphics.*
import android.media.ExifInterface
import android.util.Base64
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

object BitmapUtils {
    fun compressAndEncodedImage(filePath: String?, maxHeight: Float, maxWidth: Float): String? {
        if (filePath == null) {
            return null
        }
        var scaledBitmap: Bitmap
        val options: BitmapFactory.Options = BitmapFactory.Options()

        // By setting this field as true, the actual bitmap pixels are not loaded in the memory.
        // Just the bounds are loaded. If you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(filePath, options)
        val actualHeight: Int = options.outHeight
        val actualWidth: Int = options.outWidth
        if (actualHeight <= 0 || actualWidth <= 0) return null
        // max height and width to resize

        // setting inSampleSize value allows to load a scaled down version of the original image
        options.inSampleSize = Math.min(actualWidth / maxWidth, actualHeight / maxHeight).toInt()

        // inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false

        // this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true
        options.inInputShareable = true
        options.inTempStorage = ByteArray(16 * 1024)
        val bitmap: Bitmap
        try {
            // load the bitmap from its path
            bitmap = BitmapFactory.decodeFile(filePath, options)
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888)
            val ratioX = actualWidth / options.outWidth.toFloat()
            val ratioY = actualHeight / options.outHeight.toFloat()
            val middleX = actualWidth / 2.0f
            val middleY = actualHeight / 2.0f
            val scaleMatrix = Matrix()
            scaleMatrix.setScale(ratioX, ratioY, middleX, middleY)
            val canvas = Canvas(scaledBitmap)
            canvas.setMatrix(scaleMatrix)
            canvas.drawBitmap(bitmap, middleX - bitmap.width / 2, middleY - bitmap.height / 2,
                    Paint(Paint.FILTER_BITMAP_FLAG))
        } catch (exception: OutOfMemoryError) {
            exception.printStackTrace()
            return null
        }

        // check the rotation of the image and display it properly
        val exif: ExifInterface
        try {
            exif = ExifInterface(filePath)
            val orientation: Int = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0)
            val matrix = Matrix()
            if (orientation == 6) {
                matrix.postRotate(90f)
            } else if (orientation == 3) {
                matrix.postRotate(180f)
            } else if (orientation == 8) {
                matrix.postRotate(270f)
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.width, scaledBitmap.height, matrix, true)
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        } catch (error: OutOfMemoryError) {
            error.printStackTrace()
            return null
        }
        val maxFileSize = 2 * 1024 * 1024.toLong() // 2MB
        var jpegQuality = 85
        val outputStream = ByteArrayOutputStream()
        do {
            outputStream.reset()
            // write the compressed bitmap to output stream
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, jpegQuality, outputStream)
            jpegQuality -= 5
            if (jpegQuality <= 30) {
                break
            }
        } while (outputStream.toByteArray().size > maxFileSize)
        return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT)
    }

    fun decodeFileFromBase64Data(base64String: String?, externalCacheDir: File?, fileName: String): File? {
        var qrCodeFile: File? = null
        if (base64String != null && base64String.contains("data:image/png;base64")) {
            try {
                val decodedString: ByteArray = Base64.decode(base64String.split(",".toRegex()).toTypedArray()[1], Base64.DEFAULT)
                val decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
                val time = Calendar.getInstance().timeInMillis
                qrCodeFile = File(externalCacheDir, fileName)
                qrCodeFile.setReadable(true, false)
                qrCodeFile.setWritable(true, false)
                val fos = FileOutputStream(qrCodeFile)
                decodedByte.compress(Bitmap.CompressFormat.PNG, 100, fos)
                fos.flush()
                fos.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        return qrCodeFile
    }

    /**
     * Resize a bitmap
     */
    fun resizeBitmap(f: File): File? {
        var bitmap: Bitmap = BitmapFactory.decodeFile(f.absolutePath)
        val maxImageSize = 800f
        val ratio: Float = Math.min(maxImageSize / bitmap.width, maxImageSize / bitmap.height)
        val mWidth = Math.round(ratio * bitmap.width)
        val mHeight = Math.round(ratio * bitmap.height)
        bitmap = Bitmap.createScaledBitmap(bitmap, mWidth, mHeight, true)

        return bitmapToFile(bitmap, f)
    }

    /**
     * Write a bitmap to a file
     */
    fun bitmapToFile(bitmap: Bitmap, file: File): File? { // File name like "image.png"
        //create a file to write bitmap data
        val fos = FileOutputStream(file)
        return try {
            //Convert bitmap to byte array
            val bos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos) // YOU can also save it in JPEG
            val bitmapData = bos.toByteArray()

            //write the bytes in file
            fos.write(bitmapData)
            fos.flush()
            fos.close()
            file
        } catch (e: Exception) {
            e.printStackTrace()
            fos.close()
            file // it will return null
        }
    }

    fun decodeFileFromBase64Data(base64String: String?, externalCacheDir: File?): File? {
        var qrCodeFile: File? = null
        if (base64String != null && base64String.contains("data:image/png;base64")) {
            try {
                val decodedString: ByteArray = Base64.decode(base64String.split(",".toRegex()).toTypedArray()[1], Base64.DEFAULT)
                val decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
                val time = TimeUtils.getToday().timeInMillis
                qrCodeFile = File(externalCacheDir, "qrcode_$time.png")
                qrCodeFile.setReadable(true, false)
                qrCodeFile.setWritable(true, false)
                val fos = FileOutputStream(qrCodeFile)
                decodedByte.compress(Bitmap.CompressFormat.PNG, 100, fos)
                fos.flush()
                fos.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        return qrCodeFile
    }
}