package com.dede.dedegame.domain.model.mainGame

import android.os.Parcel
import com.dede.dedegame.domain.model.mainGame.gameDetail.GameInfo
import com.dede.dedegame.extension.parcel.KParcelable
import com.dede.dedegame.extension.parcel.parcelableCreator

class ComingTempGame() : KParcelable {
    var title: String? = null
    var games: List<GameInfo>? = null

    constructor(parcel: Parcel) : this() {
        title = parcel.readString()
        games = parcel.createTypedArrayList(GameInfo.CREATOR)
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(title)
        if (android.os.Build.VERSION.SDK_INT >= 34) {
            dest.writeTypedList(games, flags);
        } else {
            dest.writeList(games);
        }
    }

    companion object {
        @JvmField
        val CREATOR = parcelableCreator(::ComingTempGame)
    }
}