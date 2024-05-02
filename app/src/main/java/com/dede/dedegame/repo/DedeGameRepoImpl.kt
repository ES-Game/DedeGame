package com.quangph.dedegame.repo

import com.quangph.base.common.converter.ListConverter
import com.quangph.dedegame.domain.model.Author
import com.quangph.dedegame.domain.model.Book
import com.quangph.dedegame.domain.model.BookPage
import com.quangph.dedegame.domain.model.Category
import com.quangph.dedegame.domain.model.Chapter
import com.quangph.dedegame.domain.model.Home
import com.quangph.dedegame.domain.model.Rank
import com.quangph.dedegame.domain.model.Story
import com.quangph.dedegame.domain.model.StoryDetail
import com.quangph.dedegame.domain.model.Tag
import com.quangph.dedegame.domain.repo.IDedeGameRepo
import com.quangph.dedegame.repo.home.AuthorData
import com.quangph.dedegame.repo.home.AuthorDataToAuthor
import com.quangph.dedegame.repo.home.CategoryData
import com.quangph.dedegame.repo.home.CategoryDataToCategory
import com.quangph.dedegame.repo.home.ChapterData
import com.quangph.dedegame.repo.home.ChapterDataToChapter
import com.quangph.dedegame.repo.home.StoryData
import com.quangph.dedegame.repo.home.StoryDetailData
import com.quangph.dedegame.repo.home.StoryDetailDataToStoryDetail
import com.quangph.dedegame.repo.home.TagData
import com.quangph.dedegame.repo.home.TagDataToTag
import com.quangph.dedegame.repo.network.APIException
import com.quangph.dedegame.repo.network.ApiService
import com.quangph.dedegame.repo.network.NetworkFactory.createDefaultService
import com.quangph.dedegame.repo.network.invokeApi

class DedeGameRepoImpl : IDedeGameRepo {

    override fun getHomeData(limit: Int): Home {
        val service = createDefaultService(ApiService::class.java) ?: throw  APIException("Api config error")
        return service.getHomeData(limit).invokeApi {
            Home().apply {
                if (it.data?.featuredStories != null) {
                    this.featuredStories = com.dede.dedegame.repo.convert.ListConverter<StoryData, Story>(
                        StoryDataToStory()
                    ).convert(it.data?.featuredStories!!)
                }

                if (it.data?.categories != null) {
                    this.categories = com.dede.dedegame.repo.convert.ListConverter<CategoryData, Category>(
                        CategoryDataToCategory()
                    ).convert(it.data?.categories!!)
                }
            }
        }
    }

    override fun getRanking(from: String, to: String, categoryId: Int, limit: Int): Rank {
        val service = createDefaultService(ApiService::class.java) ?: throw  APIException("Api config error")
        return service.getRanking(from, to, categoryId, limit).invokeApi {
            Rank().apply {
                this.all = it.data?.all?.let { it1 ->
                    com.dede.dedegame.repo.convert.ListConverter<StoryDetailData, StoryDetail>(
                        StoryDetailDataToStoryDetail()
                    ).convert(it1)
                }

                this.views = it.data?.views?.let { it1 ->
                    com.dede.dedegame.repo.convert.ListConverter<StoryDetailData, StoryDetail>(
                        StoryDetailDataToStoryDetail()
                    ).convert(it1)
                }

                this.reactions = it.data?.reactions?.let { it1 ->
                    com.dede.dedegame.repo.convert.ListConverter<StoryDetailData, StoryDetail>(
                        StoryDetailDataToStoryDetail()
                    ).convert(it1)
                }
            }
        }
    }

    override fun getStoryDetail(storyId: Int): StoryDetail {
        val service = createDefaultService(ApiService::class.java) ?: throw  APIException("Api config error")
        return service.getStoryDetail(storyId).invokeApi {
            StoryDetail().apply {
                this.id = it.data?.id
                this.title = it.data?.title
                this.description = it.data?.description
                this.postedBy = it.data?.postedBy
                this.imageHorizontal = it.data?.imageHorizontal
                this.image = it.data?.image
                this.views = it.data?.views
                this.score = it.data?.ratings?.score
                this.count = it.data?.ratings?.count
                this.likes = it.data?.likes
                this.follows = it.data?.follows
                this.followed = it.data?.followed
                this.liked = it.data?.liked
                this.publishedAt = it.data?.publishedAt
                this.createdAt = it.data?.createdAt
                this.updatedAt = it.data?.updatedAt
                this.chapters = it.data?.chapters?.let { it1 ->
                    com.dede.dedegame.repo.convert.ListConverter<ChapterData, Chapter>(ChapterDataToChapter()).convert(
                        it1
                    )
                }
                this.authors = it.data?.authors?.let { it1->
                    com.dede.dedegame.repo.convert.ListConverter<AuthorData, Author>(AuthorDataToAuthor()).convert(it1)
                }
                this.tags = it.data?.tags?.let { it1->
                    com.dede.dedegame.repo.convert.ListConverter<TagData, Tag>(TagDataToTag()).convert(it1)
                }
            }
        }
    }
}