package com.dede.dedegame.repo.temp.mainGame.gameDetail


import com.google.gson.annotations.SerializedName

data class GameData(
    @SerializedName("description")
    var description: String?,
    @SerializedName("id")
    var id: Int?,
    @SerializedName("image")
    var image: String?,
    @SerializedName("link_android")
    var linkAndroid: String?,
    @SerializedName("link_ios")
    var linkIos: String?,
    @SerializedName("link_social")
    var linkSocial: String?,
    @SerializedName("short_description")
    var shortDescription: String?,
    @SerializedName("status_open")
    var statusOpen: Int?,
    @SerializedName("tags")
    var tags: List<String>?,
    @SerializedName("title")
    var title: String?
)