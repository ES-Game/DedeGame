package com.dede.dedegame.repo.temp.home


import com.google.gson.annotations.SerializedName

data class ArticleData(
    @SerializedName("category")
    var category: String?,
    @SerializedName("date")
    var date: String?,
    @SerializedName("description")
    var description: String?,
    @SerializedName("id")
    var id: Int?,
    @SerializedName("image")
    var image: String?,
    @SerializedName("title")
    var title: String?
)