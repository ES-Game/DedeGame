package com.quangph.dedegame.repo.home

import com.dede.dedegame.repo.convert.ListConverter
import com.dede.dedegame.repo.convert.IConverter
import com.quangph.dedegame.domain.model.Author
import com.quangph.dedegame.domain.model.Book
import com.quangph.dedegame.domain.model.Category
import com.quangph.dedegame.domain.model.Chapter
import com.quangph.dedegame.domain.model.Story
import com.quangph.dedegame.domain.model.Tag
import com.quangph.dedegame.repo.StoryDataToStory
import com.quangph.dedegame.repo.home.CategoryData
import com.quangph.dedegame.repo.home.StoryData


class TagDataToTag: IConverter<TagData, Tag> {
    override fun convert(source: TagData): Tag {
        return Tag().apply {
            this.id = source.id
            this.name = source.name

        }
    }
}