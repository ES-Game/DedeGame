package com.dede.dedegame.domain.model.home


import android.os.Parcel
import com.dede.dedegame.extension.parcel.KParcelable
import com.dede.dedegame.extension.parcel.parcelableCreator

class ComingGame() : KParcelable {
    var description: String? = null
    var id: Int? = null
    var image: String? = null
    var title: String? = null

    constructor(parcel: Parcel) : this() {
        description = parcel.readString()
        id = parcel.readInt()
        image = parcel.readString()
        title = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(description)
        parcel.writeInt(id ?: 0)
        parcel.writeString(image)
        parcel.writeString(title)

    }

    companion object {
        @JvmField
        val CREATOR = parcelableCreator(::ComingGame)
    }
}