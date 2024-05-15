package com.dede.dedegame.repo.temp.home


import com.google.gson.annotations.SerializedName

data class SliderData(
    @SerializedName("image")
    var image: String?,
    @SerializedName("sid")
    var sid: Int?,
    @SerializedName("type")
    var type: String?
)