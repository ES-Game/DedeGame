package com.dede.dedegame.domain.model

import android.os.Parcel
import com.dede.dedegame.extension.parcel.KParcelable
import com.dede.dedegame.extension.parcel.parcelableCreator

class Chapter() : KParcelable {
    var id: Int? = null
    var title: String? = null
    var publishedAt: String? = null
    var view: Int? = null

    constructor(parcel: Parcel) : this() {
        id = parcel.readInt()
        title = parcel.readString()
        view = parcel.readInt()
        publishedAt = parcel.readString()

    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id ?: 0)
        parcel.writeString(title)
        parcel.writeInt(view ?: 0)
        parcel.writeString(publishedAt)


    }

    companion object {
        @JvmField
        val CREATOR = parcelableCreator(::Chapter)
    }
}