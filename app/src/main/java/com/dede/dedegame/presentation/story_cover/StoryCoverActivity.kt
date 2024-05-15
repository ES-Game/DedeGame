package com.dede.dedegame.presentation.story_cover

import android.content.Intent
import com.dede.dedegame.R
import com.quangph.base.mvp.ICommand
import com.quangph.base.mvp.action.Action
import com.quangph.base.mvp.action.ActionException
import com.quangph.base.viewbinder.Layout
import com.dede.dedegame.domain.model.StoryDetail
import com.dede.dedegame.domain.usecase.GetStoryDetailAction
import com.dede.dedegame.presentation.chapter.ChapterActivity
import com.quangph.jetpack.JetActivity

@Layout(R.layout.activity_story_cover)
class StoryCoverActivity : JetActivity<StoryCoverView>() {

    override fun onPresenterReady() {
        super.onPresenterReady()
        val storyId = intent.getIntExtra("storyId", -1)
        getStory(storyId)
    }

    override fun onExecuteCommand(command: ICommand) {
        super.onExecuteCommand(command)
        when (command) {
//            is StoryCoverView.ChangeChapterCmd -> {
//                getChapterDetail(command.chapterId)
//            }
            is StoryCoverView.GotoChapterCmd -> {
                gotoChapter(command.item)
            }
            is StoryCoverView.OnBackCmd -> {
                onBackPressedDispatcher.onBackPressed()
            }
        }

    }


    private fun getStory(id: Int) {
        showLoading()

        val rv = GetStoryDetailAction.RV().apply {
            this.storyId = id
        }

        mActionManager.executeAction(
            GetStoryDetailAction(),
            rv,
            object : Action.SimpleActionCallback<StoryDetail>() {
                override fun onSuccess(responseValue: StoryDetail?) {
                    super.onSuccess(responseValue)
                    hideLoading()
                    responseValue?.let {
                        mvpView.fillDataToTopGroup(it)
                        mvpView.fillDataToSummary(it)
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
//        mvpView.loadChapterContent(chapterLink)
    }

    private fun gotoChapter(storyDetail: StoryDetail) {
        val intent = Intent(this, ChapterActivity:: class.java)
        intent.putExtra("key_data_story", storyDetail)
        startActivity(intent)
    }
}