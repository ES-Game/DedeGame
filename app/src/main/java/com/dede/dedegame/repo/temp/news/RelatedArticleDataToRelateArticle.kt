package com.dede.dedegame.repo.temp.news

import com.dede.dedegame.domain.model.home.Article
import com.dede.dedegame.repo.convert.IConverter


class RelatedArticleDataToRelateArticle: IConverter<RelatedArticleData, Article> {
    override fun convert(source: RelatedArticleData): Article {
        return Article().apply {
            this.id = source.id
            this.title = source.title
            this.date = source.date
            this.image = source.image
            this.category = source.category
        }
    }
}