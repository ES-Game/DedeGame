package com.dede.dedegame.domain.usecase

import com.dede.dedegame.domain.model.StoryDetail
import com.dede.dedegame.domain.model.StoryListDataPage
import com.dede.dedegame.domain.repo.IDedeGameRepo
import com.dede.dedegame.domain.repo.RepoFactory
import com.quangph.base.mvp.action.Action

class SearchStoryAction : Action<SearchStoryAction.RV, StoryListDataPage<StoryDetail>>() {

    class RV : Action.RequestValue {
        var keyword: String = ""
        var page: Int = 1
    }

    override fun onExecute(rv: RV): StoryListDataPage<StoryDetail> {
        val dedeRepo: IDedeGameRepo = RepoFactory.getDedeGameRepo()
        return dedeRepo.searchStory(rv.keyword, rv.page)
    }
}