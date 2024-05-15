package com.dede.dedegame.domain.model.home


import android.os.Parcel
import com.dede.dedegame.extension.parcel.KParcelable
import com.dede.dedegame.extension.parcel.parcelableCreator

class Slider() : KParcelable {
    var image: String? = null
    var sid: Int? = null
    var type: Type? = Type.GAME_DETAIL

    constructor(parcel: Parcel) : this() {
        image = parcel.readString()
        sid = parcel.readInt()
        type = parcel.readString()?.let { string -> Type.valueOf(string) }
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(image)
        parcel.writeInt(sid ?: 0)
        parcel.writeString(image)
        parcel.writeString(type?.name)
    }

    companion object {
        @JvmField
        val CREATOR = parcelableCreator(::Slider)
    }

    enum class Type {
        GAME_DETAIL,
        COMIC_CATEGORY

    }
}