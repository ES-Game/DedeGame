package com.quangph.dedegame.repo.network

import retrofit2.Call

fun <T : BaseAPIResponse<*>, R> Call<T>.invokeApi(block: (T) -> R): R {
    try {
        val response = this.execute()
        if (response.isSuccessful) {
            val body: T? = response.body()
            if (body != null && body.isSuccess()) {
                return block(body)
            } else {
                if (body == null) {
                    throw APIException(APIException.BODY_NULL_ERROR)
                } else {
                    throw APIException(APIException.BODY_UNSUCCESS_ERROR)
                }
            }
        } else {
            throw APIException(APIException.SERVER_ERROR)
        }
    } catch (e: Exception) {
        throw APIException(APIException.NETWORK_ERROR, e)
    }
}
