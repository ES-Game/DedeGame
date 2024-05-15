package com.dede.dedegame.repo.user

import com.google.gson.annotations.SerializedName

data class UserInfoData(
    @SerializedName("authen")
    var authen: AuthenTokenData? = null,
    @SerializedName("user")
    var user: UserData? = null
)