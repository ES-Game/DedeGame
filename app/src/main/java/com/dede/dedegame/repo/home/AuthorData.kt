package com.quangph.dedegame.repo.home


import com.google.gson.annotations.SerializedName

data class AuthorData(
    @SerializedName("id")
    var id: Int? = null,
    @SerializedName("name")
    var name: String? = null

)