package com.dede.dedegame.domain.repo

import com.dede.dedegame.domain.model.DataPage
import com.dede.dedegame.domain.model.OldHome
import com.dede.dedegame.domain.model.Rank
import com.dede.dedegame.domain.model.Rating
import com.dede.dedegame.domain.model.StoryDetail
import com.dede.dedegame.domain.model.StoryListDataPage
import com.dede.dedegame.domain.model.UserInfo
import com.dede.dedegame.domain.model.comment.Comment
import com.dede.dedegame.domain.model.home.Home
import com.dede.dedegame.domain.model.mainGame.Game
import com.dede.dedegame.domain.model.mainGame.GameType
import com.dede.dedegame.domain.model.mainGame.gameDetail.GameDetail
import com.dede.dedegame.domain.model.news.NewsDetail
import com.dede.dedegame.domain.model.payment.Payment

interface IDedeGameRepo {

    fun getHomeData(): Home
    fun fetchPayment(): Payment
    fun getCategories(limit: Int): OldHome
    fun getRanking(from: String, to: String, categoryId: Int, limit: Int): Rank
    fun getStoryDetail(storyId: Int): StoryDetail
    fun login(
        userNameOrMail: String,
        password: String,
        lang: String,
        clientId: Int,
        clientSecret: String
    ): UserInfo

    fun register(
        email: String,
        name: String,
        password: String,
        rePassword: String,
        lang: String,
        clientId: Int,
        clientSecret: String
    ): UserInfo

    fun getNewsDetail(articleId: Int): NewsDetail
    fun getGamesByType(type: GameType, page: Int): DataPage<Game>
    fun getStoryByType(categoryId: Int, page: Int): StoryListDataPage<StoryDetail>
    fun getCommentByStoryId(storyId: Int, page: Int): DataPage<Comment>
    fun getGameDetail(gameId: Int): GameDetail
    fun sendCommentToStory(storyId: Int, comment: String): Comment
    fun replyComment(storyId: Int, comment: String, parentId: Int): Comment
    fun likeCommentStory(storyId: Int, commentId: Int): Int
    fun unlikeCommentStory(storyId: Int, commentId: Int): Int

    fun refreshToken(
        token: String,
        clientId: Int,
        clientSecret: String
    ): UserInfo

    fun getCommentChapter(chapterId: Int, page: Int): DataPage<Comment>
    fun sendCommentToChapter(chapterId: Int, comment: String): Comment
    fun replyCommentChapter(chapterId: Int, comment: String, parentId: Int): Comment

    fun likeCommentChapter(chapterId: Int, commentId: Int): Int
    fun unlikeCommentChapter(chapterId: Int, commentId: Int): Int
    fun ratingStory(storyId: Int, rating: Int): Rating
    fun likeStory(storyId: Int): Int
    fun unlikeStory(storyId: Int): Int

    fun followStory(storyId: Int): Int
    fun unFollowStory(storyId: Int): Int

    fun getFollowedStories(page: Int): StoryListDataPage<StoryDetail>
}