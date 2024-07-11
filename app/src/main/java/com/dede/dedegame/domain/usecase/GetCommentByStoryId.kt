package com.dede.dedegame.domain.usecase

import com.dede.dedegame.domain.model.DataPage
import com.dede.dedegame.domain.model.comment.Comment
import com.dede.dedegame.domain.repo.IDedeGameRepo
import com.dede.dedegame.domain.repo.RepoFactory
import com.quangph.base.mvp.action.Action

class GetCommentByStoryId : Action<GetCommentByStoryId.RV, DataPage<Comment>>() {

    class RV : Action.RequestValue {
        var storyId: Int = -1
        var page: Int = 1
    }

    override fun onExecute(rv: RV): DataPage<Comment> {
        val dedeRepo: IDedeGameRepo = RepoFactory.getDedeGameRepo()
        return dedeRepo.getCommentByStoryId(rv.storyId, rv.page)
    }
}