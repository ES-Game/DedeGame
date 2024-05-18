package com.dede.dedegame.domain.model.news

import android.os.Parcel
import com.dede.dedegame.extension.parcel.KParcelable
import com.dede.dedegame.extension.parcel.parcelableCreator

class RelatedArticle() : KParcelable {
    var id: Int? = null
    var title: String? = null
    var date: String? = null
    var image: String? = null
    var category: String? = null


    constructor(parcel: Parcel) : this() {
        id = parcel.readInt()
        title = parcel.readString()
        date = parcel.readString()
        image = parcel.readString()
        category = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id ?: 0)
        parcel.writeString(title)
        parcel.writeString(date)
        parcel.writeString(image)
        parcel.writeString(category)
    }

    companion object {
        @JvmField
        val CREATOR = parcelableCreator(::RelatedArticle)
    }
}