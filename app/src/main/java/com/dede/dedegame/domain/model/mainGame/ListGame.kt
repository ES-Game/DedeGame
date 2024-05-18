package com.dede.dedegame.domain.model.mainGame

import android.os.Parcel
import com.dede.dedegame.extension.parcel.KParcelable
import com.dede.dedegame.extension.parcel.parcelableCreator

class ListGame() : KParcelable {
    var pagination: Pagination? = null
    var games: List<Game>? = null

    constructor(parcel: Parcel) : this() {
        pagination = parcel.readTypedObject(Pagination.CREATOR)
        games = parcel.createTypedArrayList(Game.CREATOR)
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeParcelable(pagination, flags)
        if (android.os.Build.VERSION.SDK_INT >= 34) {
            dest.writeTypedList(games, flags);
        } else {
            dest.writeList(games);
        }
    }

    companion object {
        @JvmField
        val CREATOR = parcelableCreator(::ListGame)
    }
}