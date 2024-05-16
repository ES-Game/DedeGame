package com.dede.dedegame.domain.repo

import com.dede.dedegame.domain.model.OldHome
import com.dede.dedegame.domain.model.Rank
import com.dede.dedegame.domain.model.StoryDetail
import com.dede.dedegame.domain.model.UserInfo
import com.dede.dedegame.domain.model.home.Home

interface IDedeGameRepo {

    fun getHomeData(): Home
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
}