package com.quangph.dedegame.repo.home

import com.dede.dedegame.repo.convert.ListConverter
import com.dede.dedegame.repo.convert.IConverter
import com.quangph.dedegame.domain.model.Book
import com.quangph.dedegame.domain.model.Category
import com.quangph.dedegame.domain.model.Story
import com.quangph.dedegame.repo.StoryDataToStory
import com.quangph.dedegame.repo.home.CategoryData
import com.quangph.dedegame.repo.home.StoryData


class CategoryDataToCategory: IConverter<CategoryData, Category> {
    override fun convert(source: CategoryData): Category {
        return Category().apply {
            this.id = source.id
            this.name = source.name

            this.stories = source.stories?.let { ListConverter<StoryData, Story>(StoryDataToStory()).convert(it) }

        }
    }
}