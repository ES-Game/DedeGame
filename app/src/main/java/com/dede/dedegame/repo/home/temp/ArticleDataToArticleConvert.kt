package com.dede.dedegame.repo.home.temp

import com.dede.dedegame.domain.model.home.Article
import com.dede.dedegame.repo.convert.IConverter
import com.dede.dedegame.repo.temp.home.ArticleData


class ArticleDataToArticleConvert : IConverter<ArticleData, Article> {
    override fun convert(source: ArticleData): Article {
        return Article().apply {
            this.category = source.category
            this.date = source.date
            this.description = source.description
            this.id = source.id
            this.image = source.image
            this.title = source.title

        }
    }
}