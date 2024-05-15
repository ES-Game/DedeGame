package com.dede.dedegame.domain.model

import android.os.Parcel
import com.dede.dedegame.extension.parcel.KParcelable
import com.dede.dedegame.extension.parcel.parcelableCreator

class Category() : KParcelable {
    var id: Int? = null
    var name: String? = null
    var stories: List<Story>? = null


    constructor(parcel: Parcel) : this() {
        id = parcel.readInt()
        name = parcel.readString()
        stories = parcel.createTypedArrayList(Story.CREATOR) ?: ArrayList()

    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        id?.let { parcel.writeInt(it) }
        parcel.writeString(name)
        parcel.writeTypedList(stories)

    }

    companion object {
        @JvmField
        val CREATOR = parcelableCreator(::Category)
    }
}