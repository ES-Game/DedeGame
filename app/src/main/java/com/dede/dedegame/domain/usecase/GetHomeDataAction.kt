package com.dede.dedegame.domain.usecase

import com.quangph.base.mvp.action.Action
import com.quangph.dedegame.domain.model.Home
import com.quangph.dedegame.domain.repo.IDedeGameRepo
import com.quangph.dedegame.domain.repo.RepoFactory

class GetHomeDataAction : Action<GetHomeDataAction.RV, Home>() {

    class RV : Action.RequestValue {
        var limit: Int = -1
    }

    override fun onExecute(rv: RV): Home {
        val dedeRepo: IDedeGameRepo = RepoFactory.getDedeGameRepo()
        return dedeRepo.getHomeData(rv.limit)
    }
}