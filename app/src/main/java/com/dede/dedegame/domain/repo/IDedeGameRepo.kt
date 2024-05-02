package com.quangph.dedegame.domain.repo

import android.service.notification.NotificationListenerService.Ranking
import com.quangph.dedegame.domain.model.BookPage
import com.quangph.dedegame.domain.model.Home
import com.quangph.dedegame.domain.model.Rank
import com.quangph.dedegame.domain.model.StoryDetail

interface IDedeGameRepo {

    fun getHomeData(limit: Int): Home
    fun getRanking(from: String, to: String, categoryId: Int, limit: Int): Rank
    fun getStoryDetail(storyId: Int): StoryDetail
}