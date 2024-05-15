package com.dede.dedegame.repo.home.temp

import com.dede.dedegame.domain.model.home.OpenedGame
import com.dede.dedegame.repo.convert.IConverter
import com.dede.dedegame.repo.temp.home.OpenedGameData


class OpenedGamesDataToOpenedGamesConvert : IConverter<OpenedGameData, OpenedGame> {
    override fun convert(source: OpenedGameData): OpenedGame {
        return OpenedGame().apply {
            this.description = source.description
            this.id = source.id
            this.image = source.image
            this.title = source.title

        }
    }
}