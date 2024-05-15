package com.dede.dedegame.repo.home

import com.dede.dedegame.repo.convert.IConverter
import com.dede.dedegame.domain.model.Chapter


class ChapterDataToChapter: IConverter<ChapterData, Chapter> {
    override fun convert(source: ChapterData): Chapter {
        return Chapter().apply {
            this.id = source.id
            this.title = source.title
            this.publishedAt = source.publishedAt
            this.view = source.view

        }
    }
}