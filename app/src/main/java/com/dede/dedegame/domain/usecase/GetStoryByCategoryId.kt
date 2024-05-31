package com.dede.dedegame.domain.usecase

import com.dede.dedegame.domain.model.DataPage
import com.dede.dedegame.domain.model.StoryDetail
import com.dede.dedegame.domain.repo.IDedeGameRepo
import com.dede.dedegame.domain.repo.RepoFactory
import com.quangph.base.mvp.action.Action

class GetStoryByCategoryId : Action<GetStoryByCategoryId.RV, DataPage<StoryDetail>>() {

    class RV : Action.RequestValue {
        var categoryId: Int = -1
        var page: Int = 1
    }

    override fun onExecute(rv: RV): DataPage<StoryDetail> {
        val dedeRepo: IDedeGameRepo = RepoFactory.getDedeGameRepo()
        return dedeRepo.getStoryByType(rv.categoryId, rv.page)
    }
}