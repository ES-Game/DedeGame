package com.dede.dedegame.repo.temp.home


import com.google.gson.annotations.SerializedName

data class OpenedGameData(
    @SerializedName("description")
    var description: String?,
    @SerializedName("id")
    var id: Int?,
    @SerializedName("image")
    var image: String?,
    @SerializedName("title")
    var title: String?
)