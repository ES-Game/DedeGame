package com.dede.dedegame.repo.home


import com.google.gson.annotations.SerializedName

data class TagData(
    @SerializedName("id")
    var id: Int? = null,
    @SerializedName("name")
    var name: String? = null

)