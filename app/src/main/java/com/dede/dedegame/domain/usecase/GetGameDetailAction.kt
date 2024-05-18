package com.dede.dedegame.domain.usecase

import com.dede.dedegame.domain.model.mainGame.gameDetail.GameDetail
import com.dede.dedegame.domain.repo.IDedeGameRepo
import com.dede.dedegame.domain.repo.RepoFactory
import com.quangph.base.mvp.action.Action

class GetGameDetailAction : Action<GetGameDetailAction.RV, GameDetail>() {

    class RV : RequestValue {
        var gameId: Int = -1
    }

    override fun onExecute(rv: RV): GameDetail {
        val dedeRepo: IDedeGameRepo = RepoFactory.getDedeGameRepo()
        return dedeRepo.getGameDetail(rv.gameId)
    }
}