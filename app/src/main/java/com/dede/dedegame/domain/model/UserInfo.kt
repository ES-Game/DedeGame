package com.dede.dedegame.domain.model

import android.os.Parcel
import com.dede.dedegame.extension.parcel.KParcelable
import com.dede.dedegame.extension.parcel.parcelableCreator

class UserInfo() : KParcelable {
    var authen: AuthenToken? = null
    var user: User? = null

    constructor(parcel: Parcel) : this() {
        authen = parcel.readTypedObject(AuthenToken.CREATOR)
        user = parcel.readTypedObject(User.CREATOR)
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeParcelable(authen, flags)
        dest.writeParcelable(user, flags)
    }

    companion object {
        @JvmField
        val CREATOR = parcelableCreator(::UserInfo)
    }
}