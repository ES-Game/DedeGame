package com.quangph.jetpack.gallery

import android.os.Parcel
import android.os.Parcelable
import com.quangph.jetpack.kotlin.parcel.KParcelable

/**
 * Created by Pham Hai Quang on 7/2/20.
 */
class MediaGallery() : KParcelable {
    enum class SourceType {
        CAMERA, LIBRARY
    }

    enum class MediaType {
        IMAGE,
        VIDEO
    }

    companion object CREATOR : Parcelable.Creator<MediaGallery> {
        override fun createFromParcel(parcel: Parcel): MediaGallery {
            return MediaGallery(parcel)
        }

        override fun newArray(size: Int): Array<MediaGallery?> {
            return arrayOfNulls(size)
        }
    }

    var timestamp: String? = null
    var path: String? = null
    var sourceType: SourceType = SourceType.LIBRARY
    var mediaType: MediaType = MediaType.IMAGE
    var size: Long = -1

    constructor(parcel: Parcel) : this() {
        timestamp = parcel.readString()
        path = parcel.readString()
        val srcStr = parcel.readString()
        sourceType = if (srcStr != null) {
            enumValueOf(srcStr)
        } else {
            SourceType.LIBRARY
        }

        val typeStr = parcel.readString()
        mediaType = if (typeStr != null) {
            enumValueOf(typeStr)
        } else {
            MediaType.IMAGE
        }
        size = parcel.readLong()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(timestamp)
        parcel.writeString(path)
        parcel.writeString(sourceType.name)
        parcel.writeString(mediaType.name)
        parcel.writeLong(size)
    }

    override fun describeContents(): Int {
        return 0
    }
}