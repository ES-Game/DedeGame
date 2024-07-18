package com.dede.dedegame.repo.network

import com.dede.dedegame.repo.home.HomeResponse
import com.dede.dedegame.repo.home.OldHomeResponse
import com.dede.dedegame.repo.home.RankResponse
import com.dede.dedegame.repo.home.StoryDetailResponse
import com.dede.dedegame.repo.payment.PaymentResponse
import com.dede.dedegame.repo.temp.comment.CommentResponse
import com.dede.dedegame.repo.temp.comment.LikedResponse
import com.dede.dedegame.repo.temp.comment.ListCommentResponse
import com.dede.dedegame.repo.temp.mainGame.ListGameResponse
import com.dede.dedegame.repo.temp.mainGame.ListStoryResponse
import com.dede.dedegame.repo.temp.mainGame.gameDetail.GameDetailResponse
import com.dede.dedegame.repo.temp.news.NewsDetailResponse
import com.dede.dedegame.repo.user.UserResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
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

    @GET("article/{articleId}")
    fun getNewsDetail(@Path("articleId") articleId: Int): Call<NewsDetailResponse>

    @POST("fetch-payment-link")
    fun fetchPayment(@Header("Authorization") authToken: String): Call<PaymentResponse>

    @GET("games/{type}")
    fun getGamesByType(
        @Path("type") gameType: Int,
        @Query("page") page: Int
    ): Call<ListGameResponse>

    @GET("game/{gameId}")
    fun getGameDetail(@Path("gameId") gameId: Int): Call<GameDetailResponse>

    @GET("category/{categoryId}")
    fun getStoryById(
        @Path("categoryId") categoryId: Int,
        @Query("page") page: Int
    ): Call<ListStoryResponse>

    @GET("comments/story/{storyId}")
    fun getCommentByIdStory(
        @Header("Authorization") authToken: String,
        @Path("storyId") categoryId: Int,
        @Query("page") page: Int
    ): Call<ListCommentResponse>

    @FormUrlEncoded
    @POST("comment-story")
    fun sendCommentToStory(
        @Header("Authorization") authToken: String,
        @Field("story_id") storyId: Int,
        @Field("comment") comment: String,
    ): Call<CommentResponse>

    @FormUrlEncoded
    @POST("comment-story")
    fun replyComment(
        @Header("Authorization") authToken: String,
        @Field("story_id") storyId: Int,
        @Field("comment") comment: String,
        @Field("parent_id") parentId: Int,
    ): Call<CommentResponse>


    @FormUrlEncoded
    @POST("like-story-comment")
    fun likeCommentStory(
        @Header("Authorization") authToken: String,
        @Field("story_id") storyId: Int,
        @Field("comment_id") commentId: Int,
    ): Call<LikedResponse>

    @FormUrlEncoded
    @POST("unlike-story-comment")
    fun unlikeCommentStory(
        @Header("Authorization") authToken: String,
        @Field("story_id") storyId: Int,
        @Field("comment_id") commentId: Int,
    ): Call<LikedResponse>
}