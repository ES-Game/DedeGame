package com.dede.dedegame.repo.user

import com.google.gson.annotations.SerializedName


data class AuthenTokenData(
    @SerializedName("token_type")
    var tokenType: String? = null,

    @SerializedName("expires_in")
    var expiresIn: String? = null,

    @SerializedName("access_token")
    var accessToken: String? = null,

    @SerializedName("refresh_token")
    var refreshToken: String? = null
)