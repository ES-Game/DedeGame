package com.dede.dedegame.repo.temp.mainGame.gameDetail

import com.dede.dedegame.domain.model.mainGame.gameDetail.GameInfo
import com.dede.dedegame.repo.convert.IConverter


class OtherGameDataToOtherGame : IConverter<OtherGameData, GameInfo> {
    override fun convert(source: OtherGameData): GameInfo {
        return GameInfo().apply {
            this.id = source.id
            this.title = source.title
            this.statusOpen =
                if (source.statusOpen == 1) GameInfo.GameStatus.OPEN else GameInfo.GameStatus.COMING
            this.image = source.image
            this.description = source.description
            this.tags = source.tags
        }
    }
}