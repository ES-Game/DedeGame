package com.quangph.dedegame.presentation.chapter

import com.quangph.base.mvp.ICommand
import com.quangph.base.viewbinder.Layout
import com.quangph.jetpack.JetActivity
import com.dede.dedegame.R
import com.quangph.base.mvp.action.Action
import com.quangph.base.mvp.action.ActionException
import com.quangph.dedegame.domain.model.StoryDetail
import com.quangph.dedegame.domain.usecase.GetStoryDetailAction

@Layout(R.layout.activity_chapter)
class ChapterActivity : JetActivity<ChapterView>() {

    override fun onPresenterReady() {
        super.onPresenterReady()
        val storyId = intent.getIntExtra("storyId", -1)
        getStory(storyId)
    }

    override fun onExecuteCommand(command: ICommand) {
        super.onExecuteCommand(command)
        when (command) {
            is ChapterView.ChangeChapterCmd -> {
                getChapterDetail(command.chapterId)
            }
        }
    }


    private fun getStory(id: Int) {
        showLoading()

        val rv = GetStoryDetailAction.RV().apply {
            this.storyId = id
        }

        mActionManager.executeAction(GetStoryDetailAction(), rv, object : Action.SimpleActionCallback<StoryDetail>() {
            override fun onSuccess(responseValue: StoryDetail?) {
                super.onSuccess(responseValue)
                hideLoading()
                responseValue?.title?.let { mvpView.setStoryName(it) }
                responseValue?.chapters?.reversed()?.first()?.id?.let {
                    getChapterDetail(it)
                }
                responseValue?.chapters?.reversed()?.let {
                    mvpView.fillDataToSpinner(it)
                }
            }

            override fun onError(e: ActionException) {
                super.onError(e)
                hideLoading()
            }
        })
    }

    private fun getChapterDetail(id: Int) {
        val chapterLink = "https://www.dedegame.me/chapter/iframe/$id"
        mvpView.loadChapterContent(chapterLink)
    }


}