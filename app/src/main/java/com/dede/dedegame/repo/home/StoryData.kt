package com.quangph.dedegame.repo.home


import com.google.gson.annotations.SerializedName

data class StoryData(
    @SerializedName("id")
    var id: Int? = null,
    @SerializedName("title")
    var title: String? = null,
    @SerializedName("views")
    var views: Int? = null,
    @SerializedName("featured")
    var featured: Int? = null,
    @SerializedName("ended")
    var ended: Int? = null,
    @SerializedName("image")
    var urlImage: String? = null,
    @SerializedName("image_horizontal")
    var urlImageHorizontal: String? = null,

)