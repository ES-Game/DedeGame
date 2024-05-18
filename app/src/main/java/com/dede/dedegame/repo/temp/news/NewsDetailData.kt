package com.dede.dedegame.repo.temp.news


import com.google.gson.annotations.SerializedName

data class NewsDetailData(
    @SerializedName("article")
    var article: ArticleData?,
    @SerializedName("related_articles")
    var relatedArticles: List<RelatedArticleData?>?
)