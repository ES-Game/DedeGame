package com.dede.dedegame.repo.temp.mainGame.gameDetail

import com.dede.dedegame.domain.model.mainGame.gameDetail.OtherGame
import com.dede.dedegame.repo.convert.IConverter


class OtherGameDataToOtherGame: IConverter<OtherGameData, OtherGame> {
    override fun convert(source: OtherGameData): OtherGame {
        return OtherGame().apply {
            this.id = source.id
            this.title = source.title
            this.statusOpen = source.statusOpen
            this.image = source.image
            this.description = source.description
            this.tags = source.tags
        }
    }
}