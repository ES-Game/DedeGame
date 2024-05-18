package com.dede.dedegame.repo.temp.mainGame


import com.google.gson.annotations.SerializedName

data class PaginationData(
    @SerializedName("current_page")
    var currentPage: Int?,
    @SerializedName("last_page")
    var lastPage: Int?,
    @SerializedName("per_page")
    var perPage: Int?,
    @SerializedName("total")
    var total: Int?
)