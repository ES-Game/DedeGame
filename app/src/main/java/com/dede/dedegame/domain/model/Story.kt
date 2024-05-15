package com.dede.dedegame.domain.model

import android.os.Parcel
import com.dede.dedegame.extension.parcel.KParcelable
import com.dede.dedegame.extension.parcel.parcelableCreator

class Story() : KParcelable {
    var id: Int? = null
    var title: String? = null
    var views: Int? = null
    var featured: Int? = null
    var ended: Int? = null
    var urlImage: String? = null
    var urlImageHorizontal: String? = null

    constructor(parcel: Parcel) : this() {
        id = parcel.readInt()
        title = parcel.readString()
        views = parcel.readInt()
        featured = parcel.readInt()
        ended = parcel.readInt()
        urlImage = parcel.readString()
        urlImageHorizontal = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id ?: 0)
        parcel.writeString(title)
        parcel.writeInt(views ?: 0)
        parcel.writeInt(featured ?: 0)
        parcel.writeInt(ended ?: 0)
        parcel.writeString(urlImage)
        parcel.writeString(urlImageHorizontal)

    }

    companion object {
        @JvmField
        val CREATOR = parcelableCreator(::Story)
    }
}