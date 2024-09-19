package com.dede.dedegame.domain.model

import android.os.Parcel
import com.dede.dedegame.extension.parcel.KParcelable
import com.dede.dedegame.extension.parcel.parcelableCreator

class Rating() : KParcelable {
    var count: Int? = null
    var score: Float? = null


    constructor(parcel: Parcel) : this() {
        count = parcel.readInt()
        score = parcel.readFloat()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(count ?: 0)
        parcel.writeFloat((score ?: 0) as Float)
    }

    companion object {
        @JvmField
        val CREATOR = parcelableCreator(::Rating)
    }
}