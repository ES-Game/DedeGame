package com.dede.dedegame.domain.model.mainGame.gameDetail

import android.os.Parcel
import com.dede.dedegame.extension.parcel.KParcelable
import com.dede.dedegame.extension.parcel.parcelableCreator

class GameDetail() : KParcelable {
    var game: Game? = null
    var otherGames: List<OtherGame>? = null

    constructor(parcel: Parcel) : this() {
        game = parcel.readTypedObject(Game.CREATOR)
        otherGames = parcel.createTypedArrayList(OtherGame.CREATOR)
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeParcelable(game, flags)
        if (android.os.Build.VERSION.SDK_INT >= 34) {
            dest.writeTypedList(otherGames, flags)
        } else {
            dest.writeList(otherGames)
        }
    }

    companion object {
        @JvmField
        val CREATOR = parcelableCreator(::GameDetail)
    }
}