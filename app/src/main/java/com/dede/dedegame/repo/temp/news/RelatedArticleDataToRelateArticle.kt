package com.dede.dedegame.repo.temp.news

import com.dede.dedegame.domain.model.news.RelatedArticle
import com.dede.dedegame.repo.convert.IConverter


class RelatedArticleDataToRelateArticle: IConverter<RelatedArticleData, RelatedArticle> {
    override fun convert(source: RelatedArticleData): RelatedArticle {
        return RelatedArticle().apply {
            this.id = source.id
            this.title = source.title
            this.date = source.date
            this.image = source.image
            this.category = source.category
        }
    }
}