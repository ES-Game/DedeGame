package com.dede.dedegame.domain.model

import android.os.Parcel
import com.dede.dedegame.extension.parcel.KParcelable
import com.dede.dedegame.extension.parcel.parcelableCreator

class AuthenToken() : KParcelable {
    var tokenType: String? = null
    var expiresIn: String? = null
    var accessToken: String? = null
    var refreshToken: String? = null


    constructor(parcel: Parcel) : this() {
        tokenType = parcel.readString()
        expiresIn = parcel.readString()
        accessToken = parcel.readString()
        refreshToken = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(tokenType)
        parcel.writeString(expiresIn)
        parcel.writeString(accessToken)
        parcel.writeString(refreshToken)
    }

    companion object {
        @JvmField
        val CREATOR = parcelableCreator(::AuthenToken)
    }
}