package com.quangph.jetpack.gallery

import android.app.Activity
import android.content.ClipData
import android.content.Intent
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import com.quangph.jetpack.JetActivity
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

/**
 * Capture image and return absolute image path
 * Created by Pham Hai Quang on 7/6/20.
 */
class CameraCaptureSupport {

    private var photoCameraTemp: MediaGallery? = null

    fun captureTempPhoto(activity: JetActivity<*>, callback: ((MediaGallery) -> Unit)? = null) {
        photoCameraTemp = null
        val currentTempFile: File?
        val pictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (pictureIntent.resolveActivity(activity.packageManager) != null) {
            //Create a file to store the image
            currentTempFile = createImageFile(activity)
            if (currentTempFile != null) {
                val photoURI = FileProvider.getUriForFile(activity,
                        "${activity.packageName}.provider", currentTempFile)
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
                    pictureIntent.clipData = ClipData.newRawUri("", photoURI)
                    pictureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                            or Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }
                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                activity.naviTo(pictureIntent).with { resultCode, intent ->
                    if (resultCode == Activity.RESULT_OK && photoCameraTemp != null) {
                        callback?.invoke(photoCameraTemp!!)
                    }
                }
            }
        }
    }

    private fun createImageFile(activity: JetActivity<*>): File? {
        val timestamp = formatDate(Calendar.getInstance().time, "yyyyMMdd_HHmmss")
        val storageDir: File? = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
            timestamp,  /* prefix */
            ".jpg",  /* suffix */
            storageDir /* directory */
        )
        photoCameraTemp = MediaGallery().apply {
            this.timestamp = timestamp
            path = image.absolutePath
            sourceType = MediaGallery.SourceType.CAMERA
            size = image.length()
        }
        return image
    }

    private fun formatDate(date: Date, formatPattern: String): String {
        val format = SimpleDateFormat(formatPattern, Locale.getDefault())
        return format.format(date)
    }
}