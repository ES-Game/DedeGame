package com.dede.dedegame.domain.model.mainGame.gameDetail

import android.os.Build
import android.os.Parcel
import com.dede.dedegame.extension.parcel.KParcelable
import com.dede.dedegame.extension.parcel.parcelableCreator

class Game() : KParcelable {
    var id: Int? = null
    var title: String? = null
    var statusOpen: Int? = null
    var image: String? = null
    var shortDescription: String? = null
    var description: String? = null
    var linkSocial: String? = null
    var linkAndroid: String? = null
    var linkIos: String? = null
    var tags: List<String>? = null

    constructor(parcel: Parcel) : this() {
        id = parcel.readInt()
        title = parcel.readString()
        statusOpen = parcel.readInt()
        image = parcel.readString()
        shortDescription = parcel.readString()
        description = parcel.readString()
        linkSocial = parcel.readString()
        linkAndroid = parcel.readString()
        linkIos = parcel.readString()
        tags = parcel.createStringArrayList()?.toList()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id ?: 0)
        parcel.writeString(title)
        parcel.writeInt(statusOpen ?: 0)
        parcel.writeString(image)
        parcel.writeString(shortDescription)
        parcel.writeString(description)
        parcel.writeString(linkSocial)
        parcel.writeString(linkAndroid)
        parcel.writeString(linkIos)
        if (Build.VERSION.SDK_INT >= 34) {
            parcel.writeStringList(tags)
        } else {
            parcel.writeList(tags)
        }
    }

    companion object {
        @JvmField
        val CREATOR = parcelableCreator(::Game)
    }
}