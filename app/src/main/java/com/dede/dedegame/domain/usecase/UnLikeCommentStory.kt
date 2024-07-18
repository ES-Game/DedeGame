package com.dede.dedegame.domain.usecase

import com.dede.dedegame.domain.repo.IDedeGameRepo
import com.dede.dedegame.domain.repo.RepoFactory
import com.quangph.base.mvp.action.Action

class UnLikeCommentStory : Action<UnLikeCommentStory.RV, Int>() {

    class RV : Action.RequestValue {
        var storyId: Int = -1
        var commentId: Int = -1
    }

    override fun onExecute(rv: RV): Int {
        val dedeRepo: IDedeGameRepo = RepoFactory.getDedeGameRepo()
        return dedeRepo.unlikeCommentStory(rv.storyId, rv.commentId)
    }
}