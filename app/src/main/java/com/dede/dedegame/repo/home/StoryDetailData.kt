package com.dede.dedegame.repo.home


import com.google.gson.annotations.SerializedName

data class StoryDetailData(

    @SerializedName("id")
    var id: Int? = null,

    @SerializedName("title")
    var title: String? = null,

    @SerializedName("description")
    var description: String? = null,

    @SerializedName("postedBy")
    var postedBy: String? = null,

    @SerializedName("imageHorizontal")
    var imageHorizontal: String? = null,

    @SerializedName("image")
    var image: String? = null,

    @SerializedName("views")
    var views: Int? = null,

    @SerializedName("ratings")
    var ratings: RatingData? = null,

    @SerializedName("likes")
    var likes: Int? = null,
    @SerializedName("follows")
    var follows: Int? = null,
    @SerializedName("followed")
    var followed: Int? = null,
    @SerializedName("liked")
    var liked: Int? = null,
    @SerializedName("publishedAt")
    var publishedAt: String? = null,
    @SerializedName("createdAt")
    var createdAt: String? = null,
    @SerializedName("updatedAt")
    var updatedAt: String? = null,
    @SerializedName("chapters")
    var chapters: List<ChapterData>? = null,
    @SerializedName("authors")
    var authors: List<AuthorData>? = null,
    @SerializedName("tags")
    var tags: List<TagData>? = null,

)