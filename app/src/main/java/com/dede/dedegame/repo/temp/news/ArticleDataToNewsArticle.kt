package com.dede.dedegame.repo.temp.news

import com.dede.dedegame.domain.model.news.Article
import com.dede.dedegame.repo.convert.IConverter


class ArticleDataToNewsArticle: IConverter<ArticleData, Article> {
    override fun convert(source: ArticleData): Article {
        return Article().apply {
            this.id = source.id
            this.title = source.title
            this.date = source.date
            this.image = source.image
            this.content = source.content
        }
    }
}