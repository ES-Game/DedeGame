package com.dede.dedegame.domain.model

import android.os.Parcel
import com.dede.dedegame.extension.parcel.KParcelable
import com.dede.dedegame.extension.parcel.parcelableCreator

class Tag() : KParcelable {
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
        val CREATOR = parcelableCreator(::Tag)
    }
}