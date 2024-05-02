package com.quangph.dedegame.repo.home


import com.google.gson.annotations.SerializedName
import com.quangph.dedegame.domain.model.StoryDetail

data class RankData(
    @SerializedName("all")
    var all: List<StoryDetailData>? = null,
    @SerializedName("views")
    var views: List<StoryDetailData>? = null,
    @SerializedName("reactions")
    var reactions: List<StoryDetailData>? = null,

)