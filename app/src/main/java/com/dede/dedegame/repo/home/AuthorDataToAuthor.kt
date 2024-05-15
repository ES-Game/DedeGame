package com.dede.dedegame.repo.home

import com.dede.dedegame.repo.convert.IConverter
import com.dede.dedegame.domain.model.Author


class AuthorDataToAuthor: IConverter<AuthorData, Author> {
    override fun convert(source: AuthorData): Author {
        return Author().apply {
            this.id = source.id
            this.name = source.name

        }
    }
}