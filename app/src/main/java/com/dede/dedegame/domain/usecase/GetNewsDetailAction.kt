package com.dede.dedegame.domain.usecase

import com.dede.dedegame.domain.model.news.NewsDetail
import com.dede.dedegame.domain.repo.IDedeGameRepo
import com.dede.dedegame.domain.repo.RepoFactory
import com.quangph.base.mvp.action.Action

class GetNewsDetailAction : Action<GetNewsDetailAction.RV, NewsDetail>() {

    class RV : RequestValue {
        var articleId: Int = -1
    }

    override fun onExecute(rv: RV): NewsDetail {
        val dedeRepo: IDedeGameRepo = RepoFactory.getDedeGameRepo()
        return dedeRepo.getNewsDetail(rv.articleId)
    }
}