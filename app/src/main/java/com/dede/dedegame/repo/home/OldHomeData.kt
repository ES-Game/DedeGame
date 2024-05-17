package com.dede.dedegame.repo.home


import com.google.gson.annotations.SerializedName

data class OldHomeData(
    @SerializedName("featured_stories")
    var featuredStories: List<StoryData>? = null,
    @SerializedName("categories")
    var categories: List<CategoryData>? = null,

)