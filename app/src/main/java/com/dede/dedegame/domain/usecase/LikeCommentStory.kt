package com.dede.dedegame.domain.usecase

import com.dede.dedegame.domain.repo.IDedeGameRepo
import com.dede.dedegame.domain.repo.RepoFactory
import com.quangph.base.mvp.action.Action

class LikeCommentStory : Action<LikeCommentStory.RV, Int>() {

    class RV : Action.RequestValue {
        var storyId: Int = -1
        var commentId: Int = -1
    }

    override fun onExecute(rv: RV): Int {
        val dedeRepo: IDedeGameRepo = RepoFactory.getDedeGameRepo()
        return dedeRepo.likeCommentStory(rv.storyId, rv.commentId)
    }
}