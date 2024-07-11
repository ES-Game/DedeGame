package com.dede.dedegame.domain.model.comment

import android.os.Parcel
import com.dede.dedegame.extension.parcel.KParcelable
import com.dede.dedegame.extension.parcel.parcelableCreator

class Comment() : KParcelable {
    var id: Int? = null
    var user: String? = null
    var comment: String? = null
    var children: List<Comment>? = null
    var likes: Int? = null
    var liked: Int? = null
    var createdAt: String? = null
    var updatedAt: String? = null
    var tab: Int? = null
    constructor(parcel: Parcel) : this() {
        id = parcel.readInt()
        user = parcel.readString()
        comment = parcel.readString()
        children = parcel.createTypedArrayList(Comment.CREATOR)
        likes = parcel.readInt()
        liked = parcel.readInt()
        createdAt = parcel.readString()
        updatedAt = parcel.readString()
        tab = parcel.readInt()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id ?: 0)
        parcel.writeString(user)
        parcel.writeString(comment)
        parcel.writeTypedList(children)
        likes?.let { parcel.writeInt(it) }
        liked?.let { parcel.writeInt(it) }
        parcel.writeString(createdAt)
        parcel.writeString(updatedAt)
        parcel.writeInt(tab ?: 0)
    }

    companion object {
        @JvmField
        val CREATOR = parcelableCreator(::Comment)
    }
}