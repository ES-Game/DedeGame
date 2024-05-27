package com.dede.dedegame.repo.home.temp

import com.dede.dedegame.domain.model.mainGame.gameDetail.GameInfo
import com.dede.dedegame.repo.convert.IConverter
import com.dede.dedegame.repo.temp.home.ComingGameData


class ComingGamesDataToComingGamesConvert : IConverter<ComingGameData, GameInfo> {
    override fun convert(source: ComingGameData): GameInfo {
        return GameInfo().apply {
            this.description = source.description
            this.id = source.id
            this.image = source.image
            this.title = source.title

        }
    }
}