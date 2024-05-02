package com.quangph.dedegame.domain.model

import android.os.Parcel
import com.quangph.dedegame.extension.parcel.KParcelable
import com.quangph.dedegame.extension.parcel.parcelableCreator

class Home() : KParcelable {
    var featuredStories: List<Story>? = null
    var categories: List<Category>? = null


    constructor(parcel: Parcel) : this() {
        featuredStories = parcel.createTypedArrayList(Story.CREATOR) ?: ArrayList()
        categories = parcel.createTypedArrayList(Category.CREATOR) ?: ArrayList()

    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeTypedList(featuredStories)
        parcel.writeTypedList(categories)

    }

    companion object {
        @JvmField
        val CREATOR = parcelableCreator(::Home)
    }
}