package com.dede.dedegame.presentation.story_cover

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.dede.dedegame.R
import com.quangph.base.mvp.ICommand
import com.quangph.base.mvp.action.Action
import com.quangph.base.mvp.action.ActionException
import com.quangph.base.viewbinder.Layout
import com.dede.dedegame.domain.model.StoryDetail
import com.dede.dedegame.domain.usecase.GetStoryDetailAction
import com.dede.dedegame.presentation.chapter.ChapterActivity
import com.dede.dedegame.presentation.common.tracker.DedeFirebaseTracker
import com.dede.dedegame.presentation.common.tracker.DedeFirebaseTrackerModel
import com.dede.dedegame.presentation.home.fragments.home.HomeFragment
import com.google.gson.Gson
import com.quangph.jetpack.JetActivity

@Layout(R.layout.activity_story_cover)
class StoryCoverActivity : JetActivity<StoryCoverView>() {

    override fun onPresenterReady() {
        super.onPresenterReady()
        val storyId = intent.getIntExtra("storyId", -1)
        trackingOnStoryCoverScreen(PARAM_STORY, storyId)
        getStory(storyId)
    }

    override fun onExecuteCommand(command: ICommand) {
        super.onExecuteCommand(command)
        when (command) {
//            is StoryCoverView.ChangeChapterCmd -> {
//                getChapterDetail(command.chapterId)
//            }
            is StoryCoverView.GotoChapterCmd -> {
                trackingTapEventStoryCover(EVENT_TAP_READ_NOW, PARAM_CHAPTER, command.item.id)
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
                    Toast.makeText(this@StoryCoverActivity, e.message, Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun getChapterDetail(id: Int) {
        val chapterLink = "https://www.dedegame.me/chapter/iframe/$id"
//        mvpView.loadChapterContent(chapterLink)
    }

    private fun gotoChapter(storyDetail: StoryDetail) {
        val intent = Intent(this, ChapterActivity:: class.java)
        intent.putExtra("key_data_story", Gson().toJson(storyDetail))
        startActivity(intent)
    }

    private fun trackingOnStoryCoverScreen(param: String, paramValue: Any?) {
        val fbModel = FirebaseLoginModel().apply {
            this.eventName = EVENT_ON_STORY_COVER
            this.param = "on_screen"
            this.paramValue = "on_screen"
            this.param2 = param
            this.paramValue2 = paramValue.toString()
        }
        DedeFirebaseTracker.track(fbModel)
    }

    private fun trackingTapEventStoryCover(eventName: String, param: String, paramValue: Any?) {
        val fbModel = FirebaseLoginModel().apply {
            this.eventName = eventName
            this.param = param
            this.paramValue = paramValue.toString()
        }
        DedeFirebaseTracker.track(fbModel)
    }

    inner class FirebaseLoginModel : DedeFirebaseTrackerModel() {
        override var screenName: String? = this@StoryCoverActivity.javaClass.simpleName
        var param: String = ""
        var paramValue: String = ""
        var param2: String = ""
        var paramValue2: String = ""
        override fun createParams(bundle: Bundle) {
            super.createParams(bundle)
            bundle.putString(param, paramValue)
            bundle.putString(param2, paramValue2)
        }
    }

    companion object {
        const val EVENT_ON_STORY_COVER = "event_on_story_cover"
        const val EVENT_TAP_READ_NOW = "event_tap_read_now"
        const val PARAM_STORY = "story_id"
        const val PARAM_CHAPTER = "chapter_id"
    }
}