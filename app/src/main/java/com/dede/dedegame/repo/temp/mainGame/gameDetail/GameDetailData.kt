package com.dede.dedegame.repo.temp.mainGame.gameDetail


import com.google.gson.annotations.SerializedName

data class GameDetailData(
    @SerializedName("game")
    var game: GameData?,
    @SerializedName("other_games")
    var otherGames: List<OtherGameData?>?
)