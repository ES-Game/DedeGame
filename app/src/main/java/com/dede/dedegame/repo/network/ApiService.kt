package com.dede.dedegame.repo.network

import com.dede.dedegame.repo.home.HomeResponse
import com.dede.dedegame.repo.home.OldHomeResponse
import com.dede.dedegame.repo.home.RankResponse
import com.dede.dedegame.repo.home.StoryDetailResponse
import com.dede.dedegame.repo.user.AuthenTokenData
import com.dede.dedegame.repo.user.UserResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("index")
    fun getHomeData(): Call<HomeResponse>

    @GET("home")
    fun getCategoriesData(@Query("limit") limit: Int): Call<OldHomeResponse>

    @GET("ranking")
    fun getRanking(
        @Query("from") from: String,
        @Query("to") to: String,
        @Query("category_id") categoryId: Int,
        @Query("limit") limit: Int,
    ): Call<RankResponse>

    @GET("story/{storyId}")
    fun getStoryDetail(@Path("storyId") storyId: Int): Call<StoryDetailResponse>

    @FormUrlEncoded
    @POST("auth/login")
    fun login(
        @Field("login") userNameOrMail: String,
        @Field("password") password: String,
        @Field("lang") lang: String,
        @Field("client_id") clientId: Int,
        @Field("client_secret") clientSecret: String
    ): Call<UserResponse>

    @FormUrlEncoded
    @POST("auth/register")
    fun register(
        @Field("email") email: String,
        @Field("name") name: String,
        @Field("password") password: String,
        @Field("password_confirmation") rePassword: String,
        @Field("lang") lang: String,
        @Field("client_id") clientId: Int,
        @Field("client_secret") clientSecret: String
    ): Call<UserResponse>


}