package com.dede.dedegame.repo.temp.home


import com.google.gson.annotations.SerializedName

data class HomeData(
    @SerializedName("articles")
    var articles: List<ArticleData>?,
    @SerializedName("coming_games")
    var comingGames: List<ComingGameData>?,
    @SerializedName("opened_games")
    var openedGames: List<OpenedGameData>?,
    @SerializedName("slider")
    var sliders: List<SliderData>?
)