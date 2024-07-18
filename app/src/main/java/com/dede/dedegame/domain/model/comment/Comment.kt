package com.dede.dedegame.domain.model.comment

import android.os.Parcel
import com.dede.dedegame.extension.parcel.KParcelable
import com.dede.dedegame.extension.parcel.parcelableCreator
import com.dede.dedegame.extension.readEnum
import com.dede.dedegame.extension.writeEnum

class Comment() : KParcelable {
    var id: Int? = null
    var user: String? = null
    var comment: String? = null
    var children: List<Comment>? = null
    var likes: Int = 0
    var statusLike: LikeStatus = LikeStatus.NOT_LOGIN
    var createdAt: String? = null
    var updatedAt: String? = null
    var level: Int = 0

    constructor(parcel: Parcel) : this() {
        id = parcel.readInt()
        user = parcel.readString()
        comment = parcel.readString()
        children = parcel.createTypedArrayList(Comment.CREATOR)
        likes = parcel.readInt()
        statusLike = parcel.readEnum<LikeStatus>()
        createdAt = parcel.readString()
        updatedAt = parcel.readString()
        level = parcel.readInt()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id ?: 0)
        parcel.writeString(user)
        parcel.writeString(comment)
        parcel.writeTypedList(children)
        parcel.writeInt(likes)
        parcel.writeEnum(statusLike)
        parcel.writeString(createdAt)
        parcel.writeString(updatedAt)
        parcel.writeInt(level ?: 0)
    }

    enum class LikeStatus {
        NOT_LOGIN, NOT_YET_LIKED, LIKED
    }

    companion object {
        @JvmField
        val CREATOR = parcelableCreator(::Comment)
    }
}