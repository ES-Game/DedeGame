package com.dede.dedegame.domain.usecase

import com.dede.dedegame.domain.model.home.Home
import com.quangph.base.mvp.action.Action
import com.dede.dedegame.domain.repo.IDedeGameRepo
import com.dede.dedegame.domain.repo.RepoFactory

class GetHomeDataAction : Action<GetHomeDataAction.RV, Home>() {

    class RV : Action.RequestValue {
    }

    override fun onExecute(rv: RV): Home {
        val dedeRepo: IDedeGameRepo = RepoFactory.getDedeGameRepo()
        return dedeRepo.getHomeData()
    }
}