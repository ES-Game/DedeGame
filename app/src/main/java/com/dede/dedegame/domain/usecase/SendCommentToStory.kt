package com.dede.dedegame.domain.usecase

import com.dede.dedegame.domain.model.comment.Comment
import com.dede.dedegame.domain.repo.IDedeGameRepo
import com.dede.dedegame.domain.repo.RepoFactory
import com.quangph.base.mvp.action.Action

class SendCommentToStory : Action<SendCommentToStory.RV, Comment>() {

    class RV : Action.RequestValue {
        var storyId: Int = -1
        var comment: String = ""
    }

    override fun onExecute(rv: RV): Comment {
        val dedeRepo: IDedeGameRepo = RepoFactory.getDedeGameRepo()
        return dedeRepo.sendCommentToStory(rv.storyId, rv.comment)
    }
}