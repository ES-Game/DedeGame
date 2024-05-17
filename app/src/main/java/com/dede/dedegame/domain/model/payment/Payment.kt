package com.dede.dedegame.domain.model.payment


import android.os.Parcel
import com.dede.dedegame.extension.parcel.KParcelable
import com.dede.dedegame.extension.parcel.parcelableCreator

class Payment() : KParcelable {
    var link: String? = null

    constructor(parcel: Parcel) : this() {
        link = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(link)
    }

    companion object {
        @JvmField
        val CREATOR = parcelableCreator(::Payment)
    }
}