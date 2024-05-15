package com.dede.dedegame.repo.home

import com.dede.dedegame.repo.convert.IConverter
import com.dede.dedegame.domain.model.Tag


class TagDataToTag: IConverter<TagData, Tag> {
    override fun convert(source: TagData): Tag {
        return Tag().apply {
            this.id = source.id
            this.name = source.name

        }
    }
}