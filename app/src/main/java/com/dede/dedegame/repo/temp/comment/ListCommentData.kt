package com.dede.dedegame.repo.temp.comment


import com.dede.dedegame.repo.temp.mainGame.PaginationData
import com.google.gson.annotations.SerializedName

data class ListCommentData(
    @SerializedName("comments")
    var comments: List<CommentData?>?,
    @SerializedName("pagination")
    var pagination: PaginationData?
)