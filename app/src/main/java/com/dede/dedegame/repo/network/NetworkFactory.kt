package com.dede.dedegame.repo.network

import android.util.Log
import com.dede.dedegame.repo.ApiConfig
import retrofit2.Retrofit
import java.util.concurrent.ConcurrentHashMap

object NetworkFactory {
    private const val DEFAULT_SERVER_CONFIG = "BOT_SERVER_CONFIG"

    private val builderMap = ConcurrentHashMap<String, NetworkBuilderInfo>()

    fun <T> createDefaultService(service: Class<T>): T? {
        synchronized(NetworkBuilderInfo::class.java) {
            var builderInfo = builderMap[DEFAULT_SERVER_CONFIG]

            if (builderInfo == null || !builderInfo.valid()) {

                builderInfo = NetworkBuilderInfo()
                builderInfo.builder = DefaultNetworkConfig(ApiConfig.API_KEY).getRetrofitBuilder()
                builderMap[DEFAULT_SERVER_CONFIG] = builderInfo
            } else {
                Log.e(
                    "RetrofitFactory", "Reuse domain retrofit builder for ${ApiConfig.DOMAIN_URL}"
                )
            }

            return builderInfo.builder?.build()?.create(service)
        }
    }

    class NetworkBuilderInfo {
        var builder: Retrofit.Builder? = null

        fun valid(): Boolean {
            return true
        }
    }
}