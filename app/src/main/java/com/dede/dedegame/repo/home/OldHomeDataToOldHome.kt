package com.dede.dedegame.repo.home

import com.dede.dedegame.repo.convert.IConverter
import com.dede.dedegame.repo.convert.ListConverter
import com.dede.dedegame.domain.model.Category
import com.dede.dedegame.domain.model.OldHome
import com.dede.dedegame.domain.model.Story
import com.dede.dedegame.repo.StoryDataToStory


class OldHomeDataToOldHome: IConverter<OldHomeData, OldHome> {
    override fun convert(source: OldHomeData): OldHome {
        return OldHome().apply {
            if (source.featuredStories != null) {
                this.featuredStories = ListConverter<StoryData, Story>(StoryDataToStory()).convert(source.featuredStories!!)
            }

            if (source.categories != null) {
                this.categories = ListConverter<CategoryData, Category>(CategoryDataToCategory()).convert(source.categories!!)
            }
        }
    }

}