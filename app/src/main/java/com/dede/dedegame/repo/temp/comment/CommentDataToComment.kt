package com.dede.dedegame.repo.temp.comment

import com.dede.dedegame.domain.model.comment.Comment
import com.dede.dedegame.repo.convert.IConverter
import com.dede.dedegame.repo.convert.ListConverter


class CommentDataToComment : IConverter<CommentData, Comment> {
    override fun convert(source: CommentData): Comment {
        return Comment().apply {
            this.id = source.id
            this.user = source.user
            this.comment = source.comment
            this.children = source.children?.let {
                ListConverter<CommentData, Comment>(CommentDataToComment()).convert(
                    it
                )
            }
            this.likes = source.likes
            this.liked = source.liked
            this.createdAt = source.createdAt
            this.updatedAt = source.updatedAt
        }
    }
}