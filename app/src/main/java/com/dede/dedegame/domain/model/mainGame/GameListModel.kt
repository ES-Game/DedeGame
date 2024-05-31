package com.dede.dedegame.domain.model.mainGame

import android.os.Parcel
import com.dede.dedegame.domain.model.mainGame.gameDetail.GameInfo
import com.dede.dedegame.extension.parcel.KParcelable
import com.dede.dedegame.extension.parcel.parcelableCreator
import com.dede.dedegame.extension.parcel.writeEnum
import com.dede.dedegame.extension.readEnum
import com.dede.dedegame.extension.writeEnum

class GameListModel() : KParcelable {
    var title: String? = null
    var type: GameType? = null
    var games: List<GameInfo>? = null

    constructor(parcel: Parcel) : this() {
        title = parcel.readString()
        type = parcel.readEnum<GameType>()
        games = parcel.createTypedArrayList(GameInfo.CREATOR)
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(title)
        dest.writeEnum(type)
        if (android.os.Build.VERSION.SDK_INT >= 34) {
            dest.writeTypedList(games, flags);
        } else {
            dest.writeList(games);
        }
    }

    companion object {
        @JvmField
        val CREATOR = parcelableCreator(::GameListModel)
    }
}