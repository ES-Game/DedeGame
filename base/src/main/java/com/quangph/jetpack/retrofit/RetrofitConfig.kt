package com.quangph.jetpack.retrofit

import com.google.gson.*
import com.quangph.base.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.lang.reflect.Type
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Config retrofit
 * Created by QuangPH on 2020-02-07.
 */
abstract class BaseRetrofitConfig {

    abstract fun getUrl(): String
    abstract fun getInterceptors(): Array<Interceptor>?

    fun getRetrofit(): Retrofit {
        return getRetrofitBuilder().build()
    }

    open fun getRetrofitBuilder(): Retrofit.Builder {
        val builder = Retrofit.Builder()
            .baseUrl(getUrl())
        val converterFactories = getConverterFactories()
        if (converterFactories != null) {
            for (factory in converterFactories) {
                builder.addConverterFactory(factory)
            }
        }
        builder.client(provideOkHttpClient())
        return builder
    }

    open fun getConverterFactories(): Array<Converter.Factory>? {
        return arrayOf(NullOnEmptyConverterFactory(),
            ScalarsConverterFactory.create(),
            GsonConverterFactory.create(getGson()))
    }

    open fun provideOkHttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()
        val interceptors = getInterceptors()
        interceptors?.let {
            it.forEach { interceptor ->
                builder.addInterceptor(interceptor)
            }
        }
        configClient(builder)
        return builder.build()
    }

    open fun configClient(builder: OkHttpClient.Builder) {
        builder.connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
    }

    private fun getGson(): Gson {
        return GsonBuilder()
                .setLenient()
                .excludeFieldsWithoutExposeAnnotation()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .registerTypeAdapter(Date::class.java, GsonUtcDateAdapter())
                .create()
    }
}


class NullOnEmptyConverterFactory : Converter.Factory() {
    override fun responseBodyConverter(type: Type?,
                                       annotations: Array<Annotation>?,
                                       retrofit: Retrofit): Converter<ResponseBody, *>? {
        val delegate = retrofit.nextResponseBodyConverter<Any>(this, type!!, annotations!!)
        return Converter<ResponseBody, Any> { value ->
            if (value.contentLength() == 0L) {
                null
            } else delegate.convert(value)
        }
    }
}


class GsonUtcDateAdapter : JsonSerializer<Date?>, JsonDeserializer<Date> {
    private val ISO_DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'"
    private val dateFormat: DateFormat

    @Synchronized
    override fun serialize(date: Date?, type: Type,
                           jsonSerializationContext: JsonSerializationContext): JsonElement {
        return JsonPrimitive(dateFormat.format(date))
    }

    @Synchronized
    override fun deserialize(jsonElement: JsonElement, type: Type,
                             jsonDeserializationContext: JsonDeserializationContext): Date {
        return try {
            dateFormat.parse(jsonElement.asString)
        } catch (e: ParseException) {
            throw JsonParseException(e)
        }
    }

    init {
        dateFormat = SimpleDateFormat(ISO_DATE_TIME_FORMAT, Locale.getDefault())
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")
    }
}
