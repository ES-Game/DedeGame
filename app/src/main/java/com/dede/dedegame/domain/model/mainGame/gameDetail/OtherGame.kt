package com.dede.dedegame.domain.model.mainGame.gameDetail

import android.os.Build
import android.os.Parcel
import com.dede.dedegame.extension.parcel.KParcelable
import com.dede.dedegame.extension.parcel.parcelableCreator

class OtherGame() : KParcelable {
    var id: Int? = null
    var title: String? = null
    var statusOpen: Int? = null
    var image: String? = null
    var description: String? = null
    var tags: List<String>? = null


    constructor(parcel: Parcel) : this() {
        id = parcel.readInt()
        title = parcel.readString()
        statusOpen = parcel.readInt()
        image = parcel.readString()
        description = parcel.readString()
        tags = parcel.createStringArrayList()?.toList()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id ?: 0)
        parcel.writeString(title)
        parcel.writeInt(statusOpen ?: 0)
        parcel.writeString(image)
        parcel.writeString(description)
        if (Build.VERSION.SDK_INT >= 34) {
            parcel.writeStringList(tags)
        } else {
            parcel.writeList(tags)
        }
    }

    companion object {
        @JvmField
        val CREATOR = parcelableCreator(::OtherGame)
    }
}