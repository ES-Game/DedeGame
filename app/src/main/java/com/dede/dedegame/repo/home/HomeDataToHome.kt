package com.dede.dedegame.repo.home

import com.dede.dedegame.repo.convert.IConverter
import com.dede.dedegame.repo.convert.ListConverter
import com.dede.dedegame.domain.model.Category
import com.dede.dedegame.domain.model.Home
import com.dede.dedegame.domain.model.Story
import com.dede.dedegame.repo.StoryDataToStory


class HomeDataToHome: IConverter<HomeData, Home> {
    override fun convert(source: HomeData): Home {
        return Home().apply {
            if (source.featuredStories != null) {
                this.featuredStories = ListConverter<StoryData, Story>(StoryDataToStory()).convert(source.featuredStories!!)
            }

            if (source.categories != null) {
                this.categories = ListConverter<CategoryData, Category>(CategoryDataToCategory()).convert(source.categories!!)
            }
        }
    }

}