package com.quangph.dedegame.repo.home


import com.google.gson.annotations.SerializedName

data class ChapterData(
    @SerializedName("id")
    var id: Int? = null,
    @SerializedName("title")
    var title: String? = null,
    @SerializedName("publishedAt")
    var publishedAt: String? = null,
    @SerializedName("view")
    var view: Int? = null

)