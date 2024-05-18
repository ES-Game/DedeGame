package com.dede.dedegame.repo.temp.mainGame


import com.google.gson.annotations.SerializedName

data class ListGameData(
    @SerializedName("games")
    var games: List<GameData?>?,
    @SerializedName("pagination")
    var pagination: PaginationData?
)