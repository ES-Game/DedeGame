package com.dede.dedegame.domain.usecase

import com.dede.dedegame.domain.model.DataPage
import com.dede.dedegame.domain.model.mainGame.Game
import com.dede.dedegame.domain.model.mainGame.GameType
import com.dede.dedegame.domain.repo.IDedeGameRepo
import com.dede.dedegame.domain.repo.RepoFactory
import com.quangph.base.mvp.action.Action

class GetGamesByType : Action<GetGamesByType.RV, DataPage<Game>>() {

    class RV : Action.RequestValue {
        var type: GameType = GameType.OPEN
        var page: Int = 1
    }

    override fun onExecute(rv: RV): DataPage<Game> {
        val dedeRepo: IDedeGameRepo = RepoFactory.getDedeGameRepo()
        return dedeRepo.getGamesByType(rv.type, rv.page)
    }
}