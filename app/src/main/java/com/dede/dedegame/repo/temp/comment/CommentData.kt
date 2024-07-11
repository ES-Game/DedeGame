package com.dede.dedegame.repo.temp.comment


import com.google.gson.annotations.SerializedName

data class CommentData(
    @SerializedName("id")
    var id: Int?,
    @SerializedName("user")
    var user: String?,
    @SerializedName("comment")
    var comment: String?,
    @SerializedName("children")
    var children: List<CommentData>?,
    @SerializedName("likes")
    var likes: Int?,
    @SerializedName("liked")
    var liked: Int?,
    @SerializedName("created_at")
    var createdAt: String?,
    @SerializedName("updated_at")
    var updatedAt: String?,
)