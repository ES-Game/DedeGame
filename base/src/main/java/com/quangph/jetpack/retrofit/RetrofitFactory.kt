package com.quangph.jetpack.retrofit

import android.util.Log
import retrofit2.Retrofit
import java.util.concurrent.ConcurrentHashMap

/**
 * We need cache Retrofit.Builder for avoiding a ton of memory problems
 * Created by QuangPH on 2020-11-27.
 */
abstract class RetrofitFactory {

    private val builderMap = ConcurrentHashMap<String, RetrofitBuilderInfo>()

    abstract fun<T> onCreate(service: Class<T>, vararg params: Any?): Retrofit.Builder?

    open fun<T> createKey(service: Class<T>, params: RetroConfigParams?) : String {
        var key = service.name
        if (params != null) {
            key = key + "_" + params.type
        }

        return key
    }

    fun <T> create(service: Class<T>, params: RetroConfigParams? = null): T? {
        synchronized(builderMap) {
            val key = createKey(service, params)
            var builderInfo = builderMap[key]
            if (builderInfo == null || !builderInfo.valid(params)) {
                Log.e("RetrofitFactory", "Create new retrofit service for: $key")
                builderInfo = RetrofitBuilderInfo()
                if (params != null) {
                    builderInfo.service = onCreate(service, params)?.build()?.create(service)
                } else {
                    builderInfo.service = onCreate(service)?.build()?.create(service)
                }

                builderInfo.params = params
                builderMap[key] = builderInfo
            } else {
                Log.e("RetrofitFactory", "Reuse retrofit service for: $key")
            }
            //return builderInfo.builder?.build()?.create(service)
            return builderInfo.service as T
        }
    }


    private class RetrofitBuilderInfo {
        var service: Any? = null
        var params: RetroConfigParams? = null

        fun valid(params: RetroConfigParams?): Boolean {
            return if (this.params == null) {
                params == null
            } else {
                if (params == null) {
                    false
                } else {
                    this.params!! == params
                }
            }
        }
    }
}