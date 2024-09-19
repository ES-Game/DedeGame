package com.dede.dedegame.repo.story

import com.dede.dedegame.domain.model.Rating
import com.dede.dedegame.repo.convert.IConverter
import com.dede.dedegame.repo.home.RatingData


class RatingDataToRating : IConverter<RatingData, Rating> {
    override fun convert(source: RatingData): Rating {
        return Rating().apply {
            this.count = source.count
            this.score = source.score
        }
    }
}