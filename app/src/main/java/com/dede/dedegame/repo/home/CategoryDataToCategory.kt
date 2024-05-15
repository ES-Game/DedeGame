package com.dede.dedegame.repo.home

import com.dede.dedegame.repo.convert.ListConverter
import com.dede.dedegame.repo.convert.IConverter
import com.dede.dedegame.domain.model.Category
import com.dede.dedegame.domain.model.Story
import com.dede.dedegame.repo.StoryDataToStory


class CategoryDataToCategory: IConverter<CategoryData, Category> {
    override fun convert(source: CategoryData): Category {
        return Category().apply {
            this.id = source.id
            this.name = source.name

            this.stories = source.stories?.let { ListConverter<StoryData, Story>(StoryDataToStory()).convert(it) }

        }
    }
}