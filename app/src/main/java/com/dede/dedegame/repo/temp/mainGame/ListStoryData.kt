package com.dede.dedegame.repo.temp.mainGame


import com.dede.dedegame.repo.home.CategoryData
import com.dede.dedegame.repo.home.StoryDetailData
import com.google.gson.annotations.SerializedName

data class ListStoryData(
    @SerializedName("category")
    var category: CategoryData,
    @SerializedName("stories")
    var stories: List<StoryDetailData?>?,
    @SerializedName("pagination")
    var pagination: PaginationData?
)