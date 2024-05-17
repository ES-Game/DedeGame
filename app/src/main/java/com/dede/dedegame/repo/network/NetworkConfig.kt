package com.dede.dedegame.repo.network

import com.dede.dedegame.BuildConfig
import com.dede.dedegame.DedeSharedPref
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.Authenticator
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

abstract class BaseNetworkConfig {

    abstract fun getUrl(): String
    abstract fun getInterceptors(): Array<Interceptor>?
    open fun getAuthenticator(): Authenticator? = null


    fun getRetrofit(): Retrofit {
        return getRetrofitBuilder().build()
    }

    fun getRetrofitBuilder(): Retrofit.Builder {
        return Retrofit.Builder().baseUrl(getUrl())
            .addConverterFactory(GsonConverterFactory.create(getGson()))
            .client(provideOkHttpClient())
//            .addConverterFactory(ScalarsConverterFactory.create())
//            .addConverterFactory(NullOnEmptyConverterFactory())
    }

    private fun provideOkHttpClient(): OkHttpClient {

        val logging = HttpLoggingInterceptor()
        if (BuildConfig.DEBUG) {
            logging.level = HttpLoggingInterceptor.Level.BODY
        } else {
            logging.level = HttpLoggingInterceptor.Level.NONE
        }

        val builder = OkHttpClient.Builder().addInterceptor {
            val original = it.request()
            var token = ""
            if (DedeSharedPref.getUserInfo()?.authen?.accessToken != null && !DedeSharedPref.getUserInfo()?.authen?.accessToken?.isEmpty()!!){
                token = DedeSharedPref.getUserInfo()?.authen?.accessToken!!
            }
            val request = original.newBuilder().addHeader("Authorization",
                String.format("Bearer %s", token))
            .build()
            val response = it.proceed(request)
            response
        }.addInterceptor(logging)

        getInterceptors()?.let {
            it.forEach { interceptor ->
                builder.addInterceptor(interceptor)
            }
        }

        getAuthenticator()?.let {
            builder.authenticator(it)
        }

        builder.connectTimeout(30, TimeUnit.SECONDS).readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)

        return builder.build()
    }

    private fun getGson(): Gson {
        return GsonBuilder()
            .setLenient()
            .disableHtmlEscaping()
            .create()
//            .registerTypeAdapter(java.util.Date::class.java, GsonUtcDateAdapter())
    }
}





