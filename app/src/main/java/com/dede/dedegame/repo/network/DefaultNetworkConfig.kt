package com.dede.dedegame.repo.network

import android.util.Log
import com.dede.dedegame.repo.ApiConfig
import com.google.gson.Gson
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

open class DefaultNetworkConfig(private val apiKey: String) : BaseNetworkConfig() {
    override fun getUrl(): String {
        return ApiConfig.DOMAIN_URL
    }

    override fun getInterceptors(): Array<Interceptor>? {
        return arrayOf(DefaultNetworkInterceptor(apiKey))
    }

    class DefaultNetworkInterceptor(private val apiKey: String): Interceptor{
        override fun intercept(chain: Interceptor.Chain): Response {
            Log.e("Api service", Gson().toJson(chain.request().url.toString()))
            val original: Request = chain.request()
            val originalHttpUrl: HttpUrl = original.url

            val url: HttpUrl = originalHttpUrl.newBuilder()
                .addQueryParameter("api-key", apiKey)
                .build()

            val requestBuilder: Request.Builder = original.newBuilder()
                .url(url)

            val request: Request = requestBuilder.build()
            return chain.proceed(request)
        }

    }
}