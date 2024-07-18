package com.dede.dedegame.domain.usecase

import com.dede.dedegame.domain.model.OldHome
import com.dede.dedegame.domain.repo.IDedeGameRepo
import com.dede.dedegame.domain.repo.RepoFactory
import com.quangph.base.mvp.action.Action

class GetCategoriesDataAction : Action<GetCategoriesDataAction.RV, OldHome>() {

    class RV : Action.RequestValue {
        var limit: Int = -1
    }

    override fun onExecute(rv: RV): OldHome {
        val dedeRepo: IDedeGameRepo = RepoFactory.getDedeGameRepo()
        return dedeRepo.getCategories(rv.limit)
    }
}