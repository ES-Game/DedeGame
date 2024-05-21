package com.dede.dedegame.repo.temp.mainGame.gameDetail


import com.google.gson.annotations.SerializedName

data class OtherGameData(
    @SerializedName("description")
    var description: String?,
    @SerializedName("id")
    var id: Int?,
    @SerializedName("image")
    var image: String?,
    @SerializedName("status_open")
    var statusOpen: Int?,
    @SerializedName("tags")
    var tags: List<String>?,
    @SerializedName("title")
    var title: String?
)