package com.dede.dedegame.repo.network

import retrofit2.Call

@Throws(Exception::class)
fun <T : BaseAPIResponse<*>, R> Call<T>.invokeApi(block: (T) -> R): R {
    val response = this.execute()
    if (response.isSuccessful) {
        val body: T? = response.body()
        if (body != null) {
            if (body.isSuccess()) {
                return block(body)
            } else {
                throw APIException(body.message, body.code!!)
            }
        } else {
            throw APIException(APIException.BODY_NULL_ERROR)
        }
    } else {
        throw APIException(APIException.SERVER_ERROR)
    }
}
