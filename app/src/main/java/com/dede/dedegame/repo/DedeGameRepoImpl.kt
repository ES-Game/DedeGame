package com.dede.dedegame.repo

import com.dede.dedegame.DedeSharedPref
import com.dede.dedegame.domain.model.Author
import com.dede.dedegame.domain.model.Category
import com.dede.dedegame.domain.model.Chapter
import com.dede.dedegame.domain.model.DataPage
import com.dede.dedegame.domain.model.OldHome
import com.dede.dedegame.domain.model.Rank
import com.dede.dedegame.domain.model.Story
import com.dede.dedegame.domain.model.StoryDetail
import com.dede.dedegame.domain.model.Tag
import com.dede.dedegame.domain.model.UserInfo
import com.dede.dedegame.domain.model.comment.Comment
import com.dede.dedegame.domain.model.home.Article
import com.dede.dedegame.domain.model.home.Home
import com.dede.dedegame.domain.model.home.Slider
import com.dede.dedegame.domain.model.mainGame.Game
import com.dede.dedegame.domain.model.mainGame.GameType
import com.dede.dedegame.domain.model.mainGame.gameDetail.GameDetail
import com.dede.dedegame.domain.model.mainGame.gameDetail.GameInfo
import com.dede.dedegame.domain.model.news.NewsDetail
import com.dede.dedegame.domain.model.payment.Payment
import com.dede.dedegame.domain.repo.IDedeGameRepo
import com.dede.dedegame.repo.convert.ListConverter
import com.dede.dedegame.repo.home.AuthorData
import com.dede.dedegame.repo.home.AuthorDataToAuthor
import com.dede.dedegame.repo.home.CategoryData
import com.dede.dedegame.repo.home.CategoryDataToCategory
import com.dede.dedegame.repo.home.ChapterData
import com.dede.dedegame.repo.home.ChapterDataToChapter
import com.dede.dedegame.repo.home.StoryData
import com.dede.dedegame.repo.home.StoryDetailData
import com.dede.dedegame.repo.home.StoryDetailDataToStoryDetail
import com.dede.dedegame.repo.home.TagData
import com.dede.dedegame.repo.home.TagDataToTag
import com.dede.dedegame.repo.home.UserDataToUser
import com.dede.dedegame.repo.home.temp.ArticleDataToArticleConvert
import com.dede.dedegame.repo.home.temp.ComingGamesDataToComingGamesConvert
import com.dede.dedegame.repo.home.temp.OpenedGamesDataToOpenedGamesConvert
import com.dede.dedegame.repo.home.temp.SliderDataToSliderConvert
import com.dede.dedegame.repo.network.APIException
import com.dede.dedegame.repo.network.ApiService
import com.dede.dedegame.repo.network.NetworkFactory.createDefaultService
import com.dede.dedegame.repo.network.invokeApi
import com.dede.dedegame.repo.temp.comment.CommentData
import com.dede.dedegame.repo.temp.comment.CommentDataToComment
import com.dede.dedegame.repo.temp.home.ArticleData
import com.dede.dedegame.repo.temp.home.ComingGameData
import com.dede.dedegame.repo.temp.home.OpenedGameData
import com.dede.dedegame.repo.temp.home.SliderData
import com.dede.dedegame.repo.temp.mainGame.GameData
import com.dede.dedegame.repo.temp.mainGame.GameDataToGame
import com.dede.dedegame.repo.temp.mainGame.gameDetail.OtherGameData
import com.dede.dedegame.repo.temp.mainGame.gameDetail.OtherGameDataToOtherGame
import com.dede.dedegame.repo.temp.news.ArticleDataToNewsArticle
import com.dede.dedegame.repo.temp.news.RelatedArticleData
import com.dede.dedegame.repo.temp.news.RelatedArticleDataToRelateArticle
import com.dede.dedegame.repo.user.AuthenTokenDataToAuthenToken

class DedeGameRepoImpl : IDedeGameRepo {

    override fun getHomeData(): Home {
        val service =
            createDefaultService(ApiService::class.java) ?: throw APIException("Api config error")
        return service.getHomeData().invokeApi { response ->
            Home().apply {
                if (response.data?.articles != null) {
                    this.articles =
                        com.dede.dedegame.repo.convert.ListConverter<ArticleData, Article>(
                            ArticleDataToArticleConvert()
                        ).convert(response.data?.articles!!)
                }
                if (response.data?.comingGames != null) {
                    this.comingGames =
                        com.dede.dedegame.repo.convert.ListConverter<ComingGameData, GameInfo>(
                            ComingGamesDataToComingGamesConvert()
                        ).convert(response.data?.comingGames!!)
                }
                if (response.data?.openedGames != null) {
                    this.openedGames =
                        com.dede.dedegame.repo.convert.ListConverter<OpenedGameData, GameInfo>(
                            OpenedGamesDataToOpenedGamesConvert()
                        ).convert(response.data?.openedGames!!)
                }
                if (response.data?.sliders != null) {
                    this.sliders =
                        com.dede.dedegame.repo.convert.ListConverter<SliderData, Slider>(
                            SliderDataToSliderConvert()
                        ).convert(response.data?.sliders!!)
                }
            }
        }
    }

    override fun fetchPayment(): Payment {
        val service =
            createDefaultService(ApiService::class.java) ?: throw APIException("Api config error")
        return service.fetchPayment("Bearer " + DedeSharedPref.getUserInfo()?.authen?.accessToken!!)
            .invokeApi { response ->
                Payment().apply {
                    this.link = response.data?.link
                }
            }
    }

    override fun getCategies(limit: Int): OldHome {
        val service =
            createDefaultService(ApiService::class.java) ?: throw APIException("Api config error")
        return service.getCategoriesData(limit).invokeApi {
            OldHome().apply {
                if (it.data?.featuredStories != null) {
                    this.featuredStories =
                        com.dede.dedegame.repo.convert.ListConverter<StoryData, Story>(
                            StoryDataToStory()
                        ).convert(it.data?.featuredStories!!)
                }

                if (it.data?.categories != null) {
                    this.categories =
                        com.dede.dedegame.repo.convert.ListConverter<CategoryData, Category>(
                            CategoryDataToCategory()
                        ).convert(it.data?.categories!!)
                }
            }
        }
    }

    override fun getRanking(from: String, to: String, categoryId: Int, limit: Int): Rank {
        val service =
            createDefaultService(ApiService::class.java) ?: throw APIException("Api config error")
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
        val service =
            createDefaultService(ApiService::class.java) ?: throw APIException("Api config error")
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
                this.comments = it.data?.comments
                this.follows = it.data?.follows
                this.followed = it.data?.followed
                this.liked = it.data?.liked
                this.publishedAt = it.data?.publishedAt
                this.createdAt = it.data?.createdAt
                this.updatedAt = it.data?.updatedAt
                this.chapters = it.data?.chapters?.let { it1 ->
                    com.dede.dedegame.repo.convert.ListConverter<ChapterData, Chapter>(
                        ChapterDataToChapter()
                    ).convert(
                        it1
                    )
                }
                this.authors = it.data?.authors?.let { it1 ->
                    com.dede.dedegame.repo.convert.ListConverter<AuthorData, Author>(
                        AuthorDataToAuthor()
                    ).convert(it1)
                }
                this.tags = it.data?.tags?.let { it1 ->
                    com.dede.dedegame.repo.convert.ListConverter<TagData, Tag>(TagDataToTag())
                        .convert(it1)
                }
            }
        }
    }

    override fun login(
        userNameOrMail: String,
        password: String,
        lang: String,
        clientId: Int,
        clientSecret: String
    ): UserInfo {
        val service =
            createDefaultService(ApiService::class.java) ?: throw APIException("Api config error")
        return service.login(
            userNameOrMail, password,
            lang,
            clientId,
            clientSecret
        ).invokeApi {
            UserInfo().apply {
                this.authen = it.data?.authen?.let { it1 ->
                    AuthenTokenDataToAuthenToken().convert(it1)
                }
                this.user = it.data?.user?.let { it1 ->
                    UserDataToUser().convert(it1)
                }
            }
        }
    }

    override fun register(
        email: String,
        name: String,
        password: String,
        rePassword: String,
        lang: String,
        clientId: Int,
        clientSecret: String
    ): UserInfo {
        val service =
            createDefaultService(ApiService::class.java) ?: throw APIException("Api config error")
        return service.register(
            email,
            name,
            password,
            rePassword,
            lang,
            clientId,
            clientSecret
        ).invokeApi {
            UserInfo().apply {
                this.authen = it.data?.authen?.let { it1 ->
                    AuthenTokenDataToAuthenToken().convert(it1)
                }
                this.user = it.data?.user?.let { it1 ->
                    UserDataToUser().convert(it1)
                }
            }
        }
    }

    override fun getNewsDetail(articleId: Int): NewsDetail {
        val service =
            createDefaultService(ApiService::class.java) ?: throw APIException("Api config error")
        return service.getNewsDetail(articleId).invokeApi {
            NewsDetail().apply {
                this.article = it.data?.article?.let { it1 ->
                    ArticleDataToNewsArticle().convert(it1)
                }
                this.relatedArticles = it.data?.relatedArticles?.let { it1 ->
                    com.dede.dedegame.repo.convert.ListConverter<RelatedArticleData, Article>(
                        RelatedArticleDataToRelateArticle()
                    ).convert(it1)
                }
            }
        }
    }

    override fun getGamesByType(type: GameType, page: Int): DataPage<Game> {
        val service =
            createDefaultService(ApiService::class.java) ?: throw APIException("Api config error")
        val gameType = if (type == GameType.OPEN) {
            1
        } else {
            0
        }
        return service.getGamesByType(gameType, page).invokeApi {
            DataPage<Game>().apply {
                this.currentPage = it.data?.pagination?.currentPage!!
                this.lastPage = it.data!!.pagination!!.lastPage!!
                this.perPage = it.data!!.pagination!!.perPage!!
                this.dataList = it.data?.games?.let { it1 ->
                    com.dede.dedegame.repo.convert.ListConverter<GameData, Game>(
                        GameDataToGame()
                    ).convert(it1)
                }!!
                this.hasNextPage = this.currentPage <= this.lastPage
            }
        }
    }

    override fun getStoryByType(categoryId: Int, page: Int): DataPage<StoryDetail> {
        val service =
            createDefaultService(ApiService::class.java) ?: throw APIException("Api config error")
        return service.getStoryById(categoryId, page).invokeApi {
            DataPage<StoryDetail>().apply {
                this.currentPage = it.data?.pagination?.currentPage!!
                this.lastPage = it.data!!.pagination!!.lastPage!!
                this.perPage = it.data!!.pagination!!.perPage!!
                this.title = it.data!!.category.name!!
                this.dataList = it.data?.stories?.let { it1 ->
                    com.dede.dedegame.repo.convert.ListConverter<StoryDetailData, StoryDetail>(
                        StoryDetailDataToStoryDetail()
                    ).convert(it1)
                }!!
                this.hasNextPage = this.currentPage <= this.lastPage
            }
        }
    }

    override fun getGameDetail(gameId: Int): GameDetail {
        val service =
            createDefaultService(ApiService::class.java) ?: throw APIException("Api config error")
        return service.getGameDetail(gameId).invokeApi {
            GameDetail().apply {
                this.game = it.data?.game?.let { it1 ->
                    com.dede.dedegame.repo.temp.mainGame.gameDetail.GameDataToGame().convert(it1)
                }
                this.otherGames = it.data?.otherGames?.let { it1 ->
                    com.dede.dedegame.repo.convert.ListConverter<OtherGameData, GameInfo>(
                        OtherGameDataToOtherGame()
                    ).convert(it1)
                }
            }
        }
    }

    override fun getCommentByStoryId(storyId: Int, page: Int): DataPage<Comment> {
        val service =
            createDefaultService(ApiService::class.java) ?: throw APIException("Api config error")
        return service.getCommentByIdStory(
            "Bearer " + DedeSharedPref.getUserInfo()?.authen?.accessToken!!,
            storyId,
            page
        ).invokeApi {
            DataPage<Comment>().apply {
                this.currentPage = it.data?.pagination?.currentPage!!
                this.lastPage = it.data!!.pagination!!.lastPage!!
                this.perPage = it.data!!.pagination!!.perPage!!
                this.dataList = it.data?.comments?.let { it1 ->
                    com.dede.dedegame.repo.convert.ListConverter<CommentData, Comment>(
                        CommentDataToComment()
                    ).convert(it1)
                }!!
                this.hasNextPage = this.currentPage <= this.lastPage
            }
        }
    }


    override fun sendCommentToStory(storyId: Int, comment: String): Comment {
        val service =
            createDefaultService(ApiService::class.java) ?: throw APIException("Api config error")
        return service.sendCommentToStory(
            "Bearer " + DedeSharedPref.getUserInfo()?.authen?.accessToken!!,
            storyId,
            comment
        ).invokeApi { source ->
            Comment().apply {
                this.id = source.data?.id
                this.user = source.data?.user
                this.comment = source.data?.comment
                this.children = source.data?.children?.let {
                    ListConverter<CommentData, Comment>(CommentDataToComment()).convert(
                        it
                    )
                }
                this.likes = source.data?.likes!!
                when (source.data?.liked) {
                    1 -> this.statusLike = Comment.LikeStatus.LIKED
                    0 -> this.statusLike = Comment.LikeStatus.NOT_YET_LIKED
                    else -> this.statusLike = Comment.LikeStatus.NOT_LOGIN
                }
                this.createdAt = source.data?.createdAt
                this.updatedAt = source.data?.updatedAt
            }
        }
    }

    override fun replyComment(storyId: Int, comment: String, parentId: Int): Comment {
        val service =
            createDefaultService(ApiService::class.java) ?: throw APIException("Api config error")
        return service.replyComment(
            "Bearer " + DedeSharedPref.getUserInfo()?.authen?.accessToken!!,
            storyId,
            comment,
            parentId
        ).invokeApi { source ->
            Comment().apply {
                this.id = source.data?.id
                this.user = source.data?.user
                this.comment = source.data?.comment
                this.children = source.data?.children?.let {
                    ListConverter<CommentData, Comment>(CommentDataToComment()).convert(
                        it
                    )
                }
                this.likes = source.data?.likes!!
                when (source.data?.liked) {
                    1 -> this.statusLike = Comment.LikeStatus.LIKED
                    0 -> this.statusLike = Comment.LikeStatus.NOT_YET_LIKED
                    else -> this.statusLike = Comment.LikeStatus.NOT_LOGIN
                }
                this.createdAt = source.data?.createdAt
                this.updatedAt = source.data?.updatedAt
            }
        }
    }

    override fun likeCommentStory(storyId: Int, commentId: Int): Int {
        val service =
            createDefaultService(ApiService::class.java) ?: throw APIException("Api config error")
        return service.likeCommentStory(
            "Bearer " + DedeSharedPref.getUserInfo()?.authen?.accessToken!!,
            storyId,
            commentId
        ).invokeApi {
            it.data!!
        }
    }

    override fun unlikeCommentStory(storyId: Int, commentId: Int): Int {
        val service =
            createDefaultService(ApiService::class.java) ?: throw APIException("Api config error")
        return service.unlikeCommentStory(
            "Bearer " + DedeSharedPref.getUserInfo()?.authen?.accessToken!!,
            storyId,
            commentId
        ).invokeApi {
            it.data!!
        }
    }
}