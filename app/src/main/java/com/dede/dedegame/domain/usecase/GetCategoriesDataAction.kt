package com.dede.dedegame.domain.usecase

import com.dede.dedegame.domain.model.OldHome
import com.dede.dedegame.domain.model.home.Home
import com.quangph.base.mvp.action.Action
import com.dede.dedegame.domain.repo.IDedeGameRepo
import com.dede.dedegame.domain.repo.RepoFactory
import com.dede.dedegame.repo.home.OldHomeData

class GetCategoriesDataAction : Action<GetCategoriesDataAction.RV, OldHome>() {

    class RV : Action.RequestValue {
        var limit: Int = -1
    }

    override fun onExecute(rv: RV): OldHome {
        val dedeRepo: IDedeGameRepo = RepoFactory.getDedeGameRepo()
        return dedeRepo.getCategies(rv.limit)
    }
}