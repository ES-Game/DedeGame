package com.quangph.dedegame.repo.home

import com.dede.dedegame.repo.convert.ListConverter
import com.dede.dedegame.repo.convert.IConverter
import com.quangph.dedegame.domain.model.Book
import com.quangph.dedegame.domain.model.Category
import com.quangph.dedegame.domain.model.Chapter
import com.quangph.dedegame.domain.model.Story
import com.quangph.dedegame.repo.StoryDataToStory
import com.quangph.dedegame.repo.home.CategoryData
import com.quangph.dedegame.repo.home.StoryData


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