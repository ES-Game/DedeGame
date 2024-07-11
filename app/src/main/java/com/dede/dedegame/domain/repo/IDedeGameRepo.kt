package com.dede.dedegame.domain.repo

import com.dede.dedegame.domain.model.DataPage
import com.dede.dedegame.domain.model.OldHome
import com.dede.dedegame.domain.model.Rank
import com.dede.dedegame.domain.model.StoryDetail
import com.dede.dedegame.domain.model.UserInfo
import com.dede.dedegame.domain.model.comment.Comment
import com.dede.dedegame.domain.model.home.Home
import com.dede.dedegame.domain.model.mainGame.Game
import com.dede.dedegame.domain.model.mainGame.GameType
import com.dede.dedegame.domain.model.mainGame.ListGame
import com.dede.dedegame.domain.model.mainGame.gameDetail.GameDetail
import com.dede.dedegame.domain.model.news.NewsDetail
import com.dede.dedegame.domain.model.payment.Payment
import retrofit2.http.Path
import retrofit2.http.Query

interface IDedeGameRepo {

    fun getHomeData(): Home
    fun fetchPayment(): Payment
    fun getCategies(limit: Int): OldHome
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
    fun getStoryByType(categoryId: Int, page: Int): DataPage<StoryDetail>
    fun getCommentByStoryId(storyId: Int, page: Int): DataPage<Comment>

    fun getGameDetail(gameId: Int): GameDetail
}