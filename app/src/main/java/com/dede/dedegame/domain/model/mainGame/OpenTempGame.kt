package com.dede.dedegame.domain.model.mainGame

import android.os.Parcel
import com.dede.dedegame.domain.model.home.OpenedGame
import com.dede.dedegame.extension.parcel.KParcelable
import com.dede.dedegame.extension.parcel.parcelableCreator

class OpenTempGame() : KParcelable {
    var title: String? = null
    var games: List<OpenedGame>? = null

    constructor(parcel: Parcel) : this() {
        title = parcel.readString()
        games = parcel.createTypedArrayList(OpenedGame.CREATOR)
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
        val CREATOR = parcelableCreator(::OpenTempGame)
    }
}