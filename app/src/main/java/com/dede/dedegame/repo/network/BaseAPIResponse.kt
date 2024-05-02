package com.quangph.dedegame.repo.network

import com.google.gson.annotations.SerializedName

abstract class BaseAPIResponse<T> {
    @SerializedName("code")
    var code: Int? = null

    @SerializedName("message")
    var message: String? = null

    @SerializedName("data")
    var data: T? = null

    fun isSuccess(): Boolean{
        return code == 200
    }
}