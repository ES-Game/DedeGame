package com.dede.dedegame.repo.home

import com.dede.dedegame.repo.convert.ListConverter
import com.dede.dedegame.repo.convert.IConverter
import com.dede.dedegame.domain.model.Author
import com.dede.dedegame.domain.model.Chapter
import com.dede.dedegame.domain.model.StoryDetail
import com.dede.dedegame.domain.model.Tag


class StoryDetailDataToStoryDetail: IConverter<StoryDetailData, StoryDetail> {
    override fun convert(source: StoryDetailData): StoryDetail {
        return StoryDetail().apply {
            this.id = source.id
            this.title = source.title
            this.description = source.description
            this.postedBy = source.postedBy
            this.imageHorizontal = source.imageHorizontal
            this.image = source.image
            this.views = source.views
            this.score = source.ratings?.score
            this.count = source.ratings?.count
            this.likes = source.likes
            this.follows = source.follows
            this.followed = source.followed
            this.liked = source.liked
            this.publishedAt = source.publishedAt
            this.createdAt = source.createdAt
            this.updatedAt = source.updatedAt
            this.chapters = source.chapters?.let {
                ListConverter<ChapterData, Chapter>(ChapterDataToChapter()).convert(
                    it
                )
            }
            this.authors = source.authors?.let {
                ListConverter<AuthorData, Author>(AuthorDataToAuthor()).convert(
                    it
                )
            }
            this.tags = source.tags?.let {
                ListConverter<TagData, Tag>(TagDataToTag()).convert(
                    it
                )
            }

        }
    }
}