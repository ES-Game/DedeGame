package com.quangph.dedegame.domain.model

import android.os.Parcel
import com.quangph.dedegame.extension.parcel.KParcelable
import com.quangph.dedegame.extension.parcel.parcelableCreator

class Author() : KParcelable {
    var id: Int? = null
    var name: String? = null


    constructor(parcel: Parcel) : this() {
        id = parcel.readInt()
        name = parcel.readString()

    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id ?: 0)
        parcel.writeString(name)


    }

    companion object {
        @JvmField
        val CREATOR = parcelableCreator(::Author)
    }
}