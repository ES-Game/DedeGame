package com.quangph.dedegame.repo.network

import com.quangph.dedegame.repo.home.HomeResponse
import com.quangph.dedegame.repo.home.RankResponse
import com.quangph.dedegame.repo.home.StoryDetailData
import com.quangph.dedegame.repo.home.StoryDetailResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Path

interface ApiService {
    @GET("home")
    fun getHomeData(@Query("limit") limit: Int): Call<HomeResponse>
    @GET("ranking")
    fun getRanking(
        @Query("from") from: String,
        @Query("to") to: String,
        @Query("category_id") categoryId: Int,
        @Query("limit")limit: Int,
    ): Call<RankResponse>
    @GET("story/{storyId}")
    fun getStoryDetail(@Path("storyId") storyId: Int): Call<StoryDetailResponse>
}