package com.quangph.dedegame.domain.usecase

import com.quangph.base.mvp.action.Action
import com.quangph.dedegame.AppConfig
import com.quangph.dedegame.domain.model.BookPage
import com.quangph.dedegame.domain.model.Home
import com.quangph.dedegame.domain.model.Rank
import com.quangph.dedegame.domain.model.StoryDetail
import com.quangph.dedegame.domain.repo.IDedeGameRepo
import com.quangph.dedegame.domain.repo.RepoFactory

class GetStoryDetailAction : Action<GetStoryDetailAction.RV, StoryDetail>() {

    class RV : Action.RequestValue {
        var storyId: Int = -1
    }

    override fun onExecute(rv: RV): StoryDetail {
        val dedeRepo: IDedeGameRepo = RepoFactory.getDedeGameRepo()
        return dedeRepo.getStoryDetail(rv.storyId)
    }
}