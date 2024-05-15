package com.dede.dedegame.repo.home


import com.google.gson.annotations.SerializedName

data class RatingData(
    @SerializedName("score")
    var score: Float? = null,
    @SerializedName("count")
    var count: Int? = null,


)