package com.dede.dedegame.repo.payment


import com.google.gson.annotations.SerializedName

data class PaymentData(
    @SerializedName("link")
    var link: String?
)