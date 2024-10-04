package com.dede.dedegame.repo.home

import com.dede.dedegame.repo.convert.ListConverter
import com.dede.dedegame.repo.convert.IConverter
import com.dede.dedegame.domain.model.Author
import com.dede.dedegame.domain.model.Chapter
import com.dede.dedegame.domain.model.StoryDetail
import com.dede.dedegame.domain.model.Tag
import com.dede.dedegame.domain.model.comment.Comment


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
            this.comments = source.comments
            this.follows = source.follows
            when (source.followed) {
                1 -> this.followed = StoryDetail.InteractionState.INTERACTED
                0 -> this.followed = StoryDetail.InteractionState.NOT_YET_INTERACTED
                else -> this.followed = StoryDetail.InteractionState.NOT_LOGIN
            }
            when (source.liked) {
                1 -> this.liked = StoryDetail.InteractionState.INTERACTED
                0 -> this.liked = StoryDetail.InteractionState.NOT_YET_INTERACTED
                else -> this.liked = StoryDetail.InteractionState.NOT_LOGIN
            }
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