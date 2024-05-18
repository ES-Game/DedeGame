package com.dede.dedegame.repo.temp.news


import com.google.gson.annotations.SerializedName

data class RelatedArticleData(
    @SerializedName("category")
    var category: String?,
    @SerializedName("date")
    var date: String?,
    @SerializedName("id")
    var id: Int?,
    @SerializedName("image")
    var image: String?,
    @SerializedName("title")
    var title: String?
)