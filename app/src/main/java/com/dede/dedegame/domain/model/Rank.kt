package com.quangph.dedegame.domain.model

import android.os.Parcel
import com.quangph.dedegame.extension.parcel.KParcelable
import com.quangph.dedegame.extension.parcel.parcelableCreator

class Rank() : KParcelable {
    var all: List<StoryDetail>? = null
    var views: List<StoryDetail>? = null
    var reactions: List<StoryDetail>? = null

    constructor(parcel: Parcel) : this() {
        all = parcel.createTypedArrayList(StoryDetail.CREATOR)
        views = parcel.createTypedArrayList(StoryDetail.CREATOR)
        reactions = parcel.createTypedArrayList(StoryDetail.CREATOR)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeTypedList(all)
        parcel.writeTypedList(views)
        parcel.writeTypedList(reactions)


    }

    companion object {
        @JvmField
        val CREATOR = parcelableCreator(::Rank)
    }
}