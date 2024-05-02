package com.quangph.dedegame.repo.home

import com.dede.dedegame.repo.convert.IConverter
import com.dede.dedegame.repo.convert.ListConverter
import com.quangph.dedegame.domain.model.Book
import com.quangph.dedegame.domain.model.Category
import com.quangph.dedegame.domain.model.Home
import com.quangph.dedegame.domain.model.Story
import com.quangph.dedegame.repo.StoryDataToStory
import com.quangph.dedegame.repo.home.CategoryData
import com.quangph.dedegame.repo.home.StoryData


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