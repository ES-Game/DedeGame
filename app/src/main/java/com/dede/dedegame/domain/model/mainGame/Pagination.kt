package com.dede.dedegame.domain.model.mainGame

import android.os.Parcel
import com.dede.dedegame.extension.parcel.KParcelable
import com.dede.dedegame.extension.parcel.parcelableCreator

class Pagination() : KParcelable {
    var total: Int? = null
    var currentPage: Int? = null
    var lastPage: Int? = null
    var perPage: Int? = null



    constructor(parcel: Parcel) : this() {
        total = parcel.readInt()
        currentPage = parcel.readInt()
        lastPage = parcel.readInt()
        perPage = parcel.readInt()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(total ?: 0)
        parcel.writeInt(currentPage ?: 0)
        parcel.writeInt(lastPage ?: 0)
        parcel.writeInt(perPage ?: 0)
    }

    companion object {
        @JvmField
        val CREATOR = parcelableCreator(::Pagination)
    }
}