package com.dede.dedegame.repo.temp.mainGame

import com.dede.dedegame.domain.model.mainGame.Game
import com.dede.dedegame.repo.convert.IConverter


class GameDataToGame: IConverter<GameData, Game> {
    override fun convert(source: GameData): Game {
        return Game().apply {
            this.id = source.id
            this.title = source.title
            this.statusOpen = source.statusOpen
            this.image = source.image
            this.description = source.description
            this.tags = source.tags
        }
    }
}