package com.dede.dedegame.domain.usecase

import com.dede.dedegame.domain.model.Rank
import com.dede.dedegame.domain.repo.IDedeGameRepo
import com.dede.dedegame.domain.repo.RepoFactory
import com.quangph.base.mvp.action.Action

class GetRankingAction : Action<GetRankingAction.RV, Rank>() {

    class RV : Action.RequestValue {
        var from: String = ""
        var to: String = ""
        var categoryId: Int = -1
        var limit: Int = -1
        var contextName: String = ""
    }

    override fun onExecute(rv: RV): Rank {
        val dedeRepo: IDedeGameRepo = RepoFactory.getDedeGameRepo()
        return dedeRepo.getRanking(rv.from, rv.to, rv.categoryId, rv.limit)
    }

    override fun createID(): String {
        val rv = requestVal
        return GetRankingAction::class.java.getSimpleName() + rv.contextName
    }
}