package com.dede.dedegame.domain.model

import android.os.Parcel
import com.dede.dedegame.extension.parcel.KParcelable
import com.dede.dedegame.extension.parcel.parcelableCreator

class User() : KParcelable {
    var id: Int? = null
    var name: String? = null
    var active: Int? = null
    var provider: String? = null
    var fullName: String? = null
    var email: String? = null
    var birthday: String? = null
    var phone: String? = null
    var address: String? = null
    var gender: String? = null
    var nationalId: String? = null
    var nationalIdIssuedDate: String? = null
    var nationalIdIssuedLocation: String? = null
    var emailVerifiedAt: String? = null
    var playNowLimitationMethod: Int? = null
    var coin: Int? = null
    var isNew: Boolean? = null
    var dedeToken: String? = null
    var createdAt: String? = null


    constructor(parcel: Parcel) : this() {
        id = parcel.readInt()
        name = parcel.readString()
        active = parcel.readInt()
        provider = parcel.readString()
        fullName = parcel.readString()
        email = parcel.readString()
        birthday = parcel.readString()
        phone = parcel.readString()
        address = parcel.readString()
        gender = parcel.readString()
        nationalId = parcel.readString()
        nationalIdIssuedDate = parcel.readString()
        nationalIdIssuedLocation = parcel.readString()
        emailVerifiedAt = parcel.readString()
        playNowLimitationMethod = parcel.readInt()
        coin = parcel.readInt()
        isNew = parcel.readByte() != 0.toByte()
        dedeToken = parcel.readString()
        createdAt = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id ?: 0)
        parcel.writeString(name)
        parcel.writeInt(active ?: 0)
        parcel.writeString(provider)
        parcel.writeString(fullName)
        parcel.writeString(email)
        parcel.writeString(birthday)
        parcel.writeString(phone)
        parcel.writeString(address)
        parcel.writeString(gender)
        parcel.writeString(nationalId)
        parcel.writeString(nationalIdIssuedDate)
        parcel.writeString(nationalIdIssuedLocation)
        parcel.writeString(emailVerifiedAt)
        parcel.writeInt(playNowLimitationMethod ?: 0)
        parcel.writeInt(coin ?: 0)
        parcel.writeByte((if (isNew == true) 1 else 0).toByte())
        parcel.writeString(dedeToken)
        parcel.writeString(createdAt)
    }

    companion object {
        @JvmField
        val CREATOR = parcelableCreator(::User)
    }
}