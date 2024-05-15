package com.dede.dedegame.domain.model

import android.os.Parcel
import com.dede.dedegame.extension.parcel.KParcelable
import com.dede.dedegame.extension.parcel.parcelableCreator

class StoryDetail() : KParcelable {
    var id: Int? = null
    var title: String? = null
    var description: String? = null
    var postedBy: String? = null
    var imageHorizontal: String? = null
    var image: String? = null
    var views: Int? = null
    var score: Float? = null
    var count: Int? = null
    var likes: Int? = null
    var follows: Int? = null
    var followed: Int? = null
    var liked: Int? = null
    var publishedAt: String? = null
    var createdAt: String? = null
    var updatedAt: String? = null
    var chapters: List<Chapter>? = null
    var authors: List<Author>? = null
    var tags: List<Tag>? = null

    constructor(parcel: Parcel) : this() {
        id = parcel.readInt()
        title = parcel.readString()
        description = parcel.readString()
        postedBy = parcel.readString()
        views = parcel.readInt()
        imageHorizontal = parcel.readString()
        image = parcel.readString()
        score = parcel.readFloat()
        count = parcel.readInt()
        likes = parcel.readInt()
        follows = parcel.readInt()
        followed = parcel.readInt()
        liked = parcel.readInt()
        publishedAt = parcel.readString()
        createdAt = parcel.readString()
        updatedAt = parcel.readString()
        chapters = parcel.createTypedArrayList(Chapter.CREATOR)
        authors = parcel.createTypedArrayList(Author.CREATOR)
        tags = parcel.createTypedArrayList(Tag.CREATOR)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id ?: 0)
        parcel.writeString(title)
        parcel.writeString(description)
        parcel.writeString(postedBy)
        parcel.writeInt(views ?: 0)
        parcel.writeString(imageHorizontal)
        parcel.writeString(image)
        parcel.writeFloat((score ?: 0) as Float)
        count?.let { parcel.writeInt(it) }
        likes?.let { parcel.writeInt(it) }
        follows?.let { parcel.writeInt(it) }
        followed?.let { parcel.writeInt(it) }
        liked?.let { parcel.writeInt(it) }
        parcel.writeString(publishedAt)
        parcel.writeString(createdAt)
        parcel.writeString(updatedAt)
        parcel.writeTypedList(chapters)
        parcel.writeTypedList(authors)
        parcel.writeTypedList(tags)

    }

    companion object {
        @JvmField
        val CREATOR = parcelableCreator(::StoryDetail)
    }
}