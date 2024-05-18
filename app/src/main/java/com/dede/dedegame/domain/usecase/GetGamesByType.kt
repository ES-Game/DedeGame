package com.dede.dedegame.domain.usecase

import com.dede.dedegame.domain.model.mainGame.ListGame
import com.dede.dedegame.domain.repo.IDedeGameRepo
import com.dede.dedegame.domain.repo.RepoFactory
import com.quangph.base.mvp.action.Action

class GetGamesByType : Action<GetGamesByType.RV, ListGame>() {

    class RV : Action.RequestValue {
        var type: Int = -1
        var page: Int = 1
    }

    override fun onExecute(rv: RV): ListGame {
        val dedeRepo: IDedeGameRepo = RepoFactory.getDedeGameRepo()
        return dedeRepo.getGamesByType(rv.type, rv.page)
    }
}