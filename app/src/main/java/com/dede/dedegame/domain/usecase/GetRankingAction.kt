package com.quangph.dedegame.domain.usecase

import com.quangph.base.mvp.action.Action
import com.quangph.dedegame.AppConfig
import com.quangph.dedegame.domain.model.BookPage
import com.quangph.dedegame.domain.model.Home
import com.quangph.dedegame.domain.model.Rank
import com.quangph.dedegame.domain.repo.IDedeGameRepo
import com.quangph.dedegame.domain.repo.RepoFactory

class GetRankingAction : Action<GetRankingAction.RV, Rank>() {

    class RV : Action.RequestValue {
        var from: String = ""
        var to: String = ""
        var categoryId: Int = -1
        var limit: Int = -1
    }

    override fun onExecute(rv: RV): Rank {
        val dedeRepo: IDedeGameRepo = RepoFactory.getDedeGameRepo()
        return dedeRepo.getRanking(rv.from, rv.to, rv.categoryId, rv.limit)
    }
}