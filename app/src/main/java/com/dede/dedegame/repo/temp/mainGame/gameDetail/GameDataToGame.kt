package com.dede.dedegame.repo.temp.mainGame.gameDetail

import com.dede.dedegame.domain.model.mainGame.gameDetail.Game
import com.dede.dedegame.repo.convert.IConverter


class GameDataToGame: IConverter<GameData, Game> {
    override fun convert(source: GameData): Game {
        return Game().apply {
            this.id = source.id
            this.title = source.title
            this.statusOpen = source.statusOpen
            this.image = source.image
            this.shortDescription = source.shortDescription
            this.description = source.description
            this.linkSocial = source.linkSocial
            this.linkIos = source.linkIos
            this.linkAndroid = source.linkAndroid
            this.tags = source.tags
        }
    }
}