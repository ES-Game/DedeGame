package com.dede.dedegame.repo


import com.dede.dedegame.repo.convert.IConverter
import com.dede.dedegame.domain.model.Story
import com.dede.dedegame.repo.home.StoryData

class StoryDataToStory: IConverter<StoryData, Story> {
    override fun convert(source: StoryData): Story {
        return Story().apply {
            this.id = source.id
            this.title = source.title
            this.views = source.views
            this.featured = source.featured
            this.ended = source.ended
            this.urlImage = source.urlImage
            this.urlImageHorizontal = source.urlImageHorizontal
        }
    }
}