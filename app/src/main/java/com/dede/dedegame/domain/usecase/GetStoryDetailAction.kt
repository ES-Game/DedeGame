package com.dede.dedegame.domain.usecase

import com.quangph.base.mvp.action.Action
import com.dede.dedegame.domain.model.StoryDetail
import com.dede.dedegame.domain.repo.IDedeGameRepo
import com.dede.dedegame.domain.repo.RepoFactory

class GetStoryDetailAction : Action<GetStoryDetailAction.RV, StoryDetail>() {

    class RV : Action.RequestValue {
        var storyId: Int = -1
    }

    override fun onExecute(rv: RV): StoryDetail {
        val dedeRepo: IDedeGameRepo = RepoFactory.getDedeGameRepo()
        return dedeRepo.getStoryDetail(rv.storyId)
    }
}