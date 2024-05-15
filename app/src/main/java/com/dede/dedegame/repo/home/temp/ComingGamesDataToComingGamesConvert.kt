package com.dede.dedegame.repo.home.temp

import com.dede.dedegame.domain.model.home.ComingGame
import com.dede.dedegame.repo.convert.IConverter
import com.dede.dedegame.repo.temp.home.ComingGameData


class ComingGamesDataToComingGamesConvert : IConverter<ComingGameData, ComingGame> {
    override fun convert(source: ComingGameData): ComingGame {
        return ComingGame().apply {
            this.description = source.description
            this.id = source.id
            this.image = source.image
            this.title = source.title

        }
    }
}