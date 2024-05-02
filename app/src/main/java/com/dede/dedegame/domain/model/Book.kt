package com.quangph.dedegame.domain.model

import android.os.Parcel
import com.quangph.dedegame.extension.parcel.KParcelable
import com.quangph.dedegame.extension.parcel.parcelableCreator

class Book() : KParcelable {
    var title: String? = null
    var displayTitle: String? = null
    var author: String? = null
    var description: String? = null
    var publisher: String? = null
    var rank: Int? = null

    constructor(parcel: Parcel) : this() {
        title = parcel.readString()
        displayTitle = parcel.readString()
        author = parcel.readString()
        description = parcel.readString()
        publisher = parcel.readString()
        rank = parcel.readInt()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(displayTitle)
        parcel.writeString(author)
        parcel.writeString(description)
        parcel.writeString(publisher)
        parcel.writeInt(rank ?: 0)
    }

    companion object {
        @JvmField
        val CREATOR = parcelableCreator(::Book)
    }
}