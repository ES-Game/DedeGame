package com.dede.dedegame.repo.user

import com.google.gson.annotations.SerializedName

data class UserData(
    @SerializedName("id")
    var id: Int? = null,
    @SerializedName("name")
    var name: String? = null,
    @SerializedName("active")
    var active: Int? = null,
    @SerializedName("provider")
    var provider: String? = null,
    @SerializedName("full_name")
    var fullName: String? = null,
    @SerializedName("email")
    var email: String? = null,
    @SerializedName("birthday")
    var birthday: String? = null,
    @SerializedName("phone")
    var phone: String? = null,
    @SerializedName("address")
    var address: String? = null,
    @SerializedName("gender")
    var gender: String? = null,
    @SerializedName("national_id")
    var nationalId: String? = null,
    @SerializedName("national_id_issued_date")
    var nationalIdIssuedDate: String? = null,
    @SerializedName("national_id_issued_location")
    var nationalIdIssuedLocation: String? = null,
    @SerializedName("email_verified_at")
    var emailVerifiedAt: String? = null,
    @SerializedName("play_now_limitation_method")
    var playNowLimitationMethod: Int? = null,
    @SerializedName("coin")
    var coin: Int? = null,
    @SerializedName("is_new")
    var isNew: Boolean? = null,
    @SerializedName("dede_token")
    var dedeToken: String? = null,
    @SerializedName("created_at")
    var createdAt: String? = null
)