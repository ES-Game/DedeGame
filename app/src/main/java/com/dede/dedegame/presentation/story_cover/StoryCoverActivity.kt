package com.dede.dedegame.presentation.story_cover

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.dede.dedegame.DedeSharedPref
import com.dede.dedegame.R
import com.dede.dedegame.domain.model.DataPage
import com.dede.dedegame.domain.model.StoryDetail
import com.dede.dedegame.domain.model.comment.Comment
import com.dede.dedegame.domain.usecase.GetCommentByStoryId
import com.dede.dedegame.domain.usecase.GetStoryDetailAction
import com.dede.dedegame.presentation.chapter.ChapterActivity
import com.dede.dedegame.presentation.common.tracker.DedeFirebaseTracker
import com.dede.dedegame.presentation.common.tracker.DedeFirebaseTrackerModel
import com.dede.dedegame.presentation.widget.dialog.CommentDetailDialog
import com.google.gson.Gson
import com.quangph.base.mvp.ICommand
import com.quangph.base.mvp.action.Action
import com.quangph.base.mvp.action.ActionException
import com.quangph.base.viewbinder.Layout
import com.quangph.jetpack.JetActivity

@Layout(R.layout.activity_story_cover)
class StoryCoverActivity : JetActivity<StoryCoverView>() {

    private var storyId = -1

    override fun onPresenterReady() {
        super.onPresenterReady()
        storyId = intent.getIntExtra("storyId", -1)
        trackingOnStoryCoverScreen(PARAM_STORY, storyId)
        getStory(storyId)
        getComments(storyId)
    }

    override fun onExecuteCommand(command: ICommand) {
        super.onExecuteCommand(command)
        when (command) {
            is StoryCoverView.OnclickLikedCmd -> {
                if (DedeSharedPref.getUserInfo()?.authen?.accessToken != null && !DedeSharedPref.getUserInfo()?.authen?.accessToken?.isEmpty()!!) {

                } else {
                    Toast.makeText(
                        this,
                        getString(R.string.story_cover_login_to_interaction),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            is StoryCoverView.OnclickReplyCmd -> {
                if (DedeSharedPref.getUserInfo()?.authen?.accessToken != null && !DedeSharedPref.getUserInfo()?.authen?.accessToken?.isEmpty()!!) {
                    gotoCommentDetail(storyId, command.comment, command.comments)
                } else {
                    Toast.makeText(
                        this,
                        getString(R.string.story_cover_login_to_interaction),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            is StoryCoverView.ViewCommentCmd -> {
                if (DedeSharedPref.getUserInfo()?.authen?.accessToken != null && !DedeSharedPref.getUserInfo()?.authen?.accessToken?.isEmpty()!!) {
                    gotoCommentDetail(storyId, null, command.comments)
                } else {
                    Toast.makeText(
                        this,
                        getString(R.string.story_cover_login_to_comment),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            is StoryCoverView.GotoChapterCmd -> {
                trackingTapEventStoryCover(EVENT_TAP_READ_NOW, PARAM_STORY, command.item.id)
                gotoChapter(command.item)
            }

            is StoryCoverView.GotoChapterBySelectChapterCmd -> {
                trackingTapEventStoryCover(EVENT_TAP_CHAPTER_ITEM, PARAM_CHAPTER, command.chapterId)
                gotoChapter(command.item, command.chapterId)
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
                        if (!it.chapters.isNullOrEmpty()) {
                            mvpView.fillDataToLatestChapter(it, it.chapters!!)
                        }
                    }
                }

                override fun onError(e: ActionException) {
                    super.onError(e)
                    hideLoading()
                    Toast.makeText(this@StoryCoverActivity, e.message, Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun getComments(id: Int) {
        showLoading()

        val rv = GetCommentByStoryId.RV().apply {
            this.storyId = id
        }

        mActionManager.executeAction(
            GetCommentByStoryId(),
            rv,
            object : Action.SimpleActionCallback<DataPage<Comment>>() {
                override fun onSuccess(responseValue: DataPage<Comment>?) {
                    super.onSuccess(responseValue)
                    hideLoading()
                    responseValue?.dataList?.let {
                        mvpView.fillDataToComment(flattenComments(it))
                    }
                }

                override fun onError(e: ActionException) {
                    super.onError(e)
                    hideLoading()
                    Toast.makeText(this@StoryCoverActivity, e.message, Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun gotoCommentDetail(storyId: Int, comment: Comment?, comments: List<Comment>) {
        val commentDetailDialog = if (comment != null) {
            CommentDetailDialog.newInstance(storyId, comment, comments)
        } else {
            CommentDetailDialog.newInstance(storyId, null, comments)
        }
        commentDetailDialog.show(supportFragmentManager, commentDetailDialog.tag)
    }

    private fun gotoChapter(storyDetail: StoryDetail, chapterId: Int) {
        val intent = Intent(this, ChapterActivity::class.java)
        intent.putExtra("key_data_story", Gson().toJson(storyDetail))
        intent.putExtra("chapter_id", chapterId)
        startActivity(intent)
    }

    private fun gotoChapter(storyDetail: StoryDetail) {
        val intent = Intent(this, ChapterActivity::class.java)
        intent.putExtra("key_data_story", Gson().toJson(storyDetail))
        startActivity(intent)
    }

    private fun flattenComments(comments: List<Comment>, level: Int = 0): List<Comment> {
        val flatList = mutableListOf<Comment>()
        for (comment in comments) {
            comment.tab = level
            flatList.add(comment)
            comment.children?.let {
                flatList.addAll(flattenComments(it, level + 1))
            }
        }
        return flatList
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
        const val EVENT_TAP_CHAPTER_ITEM = "event_tap_chapter"
        const val PARAM_STORY = "story_id"
        const val PARAM_CHAPTER = "chapter_id"

        fun launchScreen(
            context: Context?,
            storyId: Int?
        ) {
            val intent = Intent(context, StoryCoverActivity::class.java)
            intent.putExtra("storyId", storyId)
            context?.startActivity(intent)
        }
    }
}