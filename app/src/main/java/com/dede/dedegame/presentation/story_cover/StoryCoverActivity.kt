package com.dede.dedegame.presentation.story_cover

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import com.dede.dedegame.AppConfig
import com.dede.dedegame.DedeSharedPref
import com.dede.dedegame.R
import com.dede.dedegame.domain.model.DataPage
import com.dede.dedegame.domain.model.Rating
import com.dede.dedegame.domain.model.StoryDetail
import com.dede.dedegame.domain.model.UserInfo
import com.dede.dedegame.domain.model.comment.Comment
import com.dede.dedegame.domain.usecase.GetCommentByStoryId
import com.dede.dedegame.domain.usecase.GetStoryDetailAction
import com.dede.dedegame.domain.usecase.LikeCommentStory
import com.dede.dedegame.domain.usecase.RatingStoryAction
import com.dede.dedegame.domain.usecase.RefreshToken
import com.dede.dedegame.domain.usecase.UnLikeCommentStory
import com.dede.dedegame.presentation.chapter.ChapterActivity
import com.dede.dedegame.presentation.common.tracker.DedeFirebaseTracker
import com.dede.dedegame.presentation.common.tracker.DedeFirebaseTrackerModel
import com.dede.dedegame.presentation.login.LoginActivity
import com.dede.dedegame.presentation.widget.dialog.CommentDetailDialog
import com.dede.dedegame.presentation.widget.dialog.ExpiredSessionDialog
import com.dede.dedegame.repo.network.APIActionException
import com.dede.dedegame.repo.user.exception.LogoutException
import com.google.gson.Gson
import com.quangph.base.mvp.ICommand
import com.quangph.base.mvp.action.Action
import com.quangph.base.mvp.action.ActionException
import com.quangph.base.viewbinder.Layout
import com.quangph.jetpack.JetActivity

@Layout(R.layout.activity_story_cover)
class StoryCoverActivity : JetActivity<StoryCoverView>() {

    private var mStoryId = -1
    private lateinit var listComment: List<Comment>
    private lateinit var mStoryDetail: StoryDetail
    override fun onPresenterReady() {
        super.onPresenterReady()
        mStoryId = intent.getIntExtra("storyId", -1)
        trackingOnStoryCoverScreen(PARAM_STORY, mStoryId)
        getStory(mStoryId)
        getComments(mStoryId, true)
    }

    override fun onExecuteCommand(command: ICommand) {
        super.onExecuteCommand(command)
        when (command) {
            is StoryCoverView.OnclickLikedCmd -> {
                if (DedeSharedPref.getUserInfo()?.authen?.accessToken != null && !DedeSharedPref.getUserInfo()?.authen?.accessToken?.isEmpty()!!) {
                    when (command.comment?.statusLike) {
                        Comment.LikeStatus.LIKED -> {
                            unLikeCommentStory(mStoryId, command.comment.id!!)
                        }

                        Comment.LikeStatus.NOT_YET_LIKED -> {
                            likeCommentStory(mStoryId, command.comment.id!!)
                        }

                        else -> {
                            refreshToken(
                                DedeSharedPref.getUserInfo()?.authen?.refreshToken!!,
                                AppConfig.clientId,
                                AppConfig.clientSecret
                            ) {
                                getComments(mStoryId, true)
                            }
                        }
                    }
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
                    gotoCommentDetail(mStoryId, command.comment, command.comments)
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
                    gotoCommentDetail(mStoryId, null, command.comments)
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

            is StoryCoverView.RatingStoryCmd -> {
                ratingStory(command.newRate, command.oldRate, command.count)
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
                        mStoryDetail = it
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

    private fun getComments(id: Int, loading: Boolean) {
        if (loading) {
            showLoading()
        }

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
                        listComment = it
                        mvpView.fillDataToComment(listComment)
                    }
                }

                override fun onError(e: ActionException) {
                    super.onError(e)
                    hideLoading()
                    Toast.makeText(this@StoryCoverActivity, e.message, Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun likeCommentStory(id: Int, commentId: Int) {
        val rv = LikeCommentStory.RV().apply {
            this.storyId = id
            this.commentId = commentId
        }

        mActionManager.executeAction(
            LikeCommentStory(),
            rv,
            object : Action.SimpleActionCallback<Int>() {
                override fun onSuccess(responseValue: Int?) {
                    super.onSuccess(responseValue)
                    responseValue?.let {
                        listComment.find { cmt -> cmt.id == commentId }?.let {
                            it.statusLike = Comment.LikeStatus.LIKED
                            it.likes += 1
                            mvpView.fillDataToComment(listComment)
                        }
                    }
                }

                override fun onError(e: ActionException) {
                    super.onError(e)
                    if (e.cause is LogoutException) {
                        logOut()
                    } else {
                        Toast.makeText(this@StoryCoverActivity, e.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            })
    }

    private fun unLikeCommentStory(id: Int, commentId: Int) {
        val rv = UnLikeCommentStory.RV().apply {
            this.storyId = id
            this.commentId = commentId
        }

        mActionManager.executeAction(
            UnLikeCommentStory(),
            rv,
            object : Action.SimpleActionCallback<Int>() {
                override fun onSuccess(responseValue: Int?) {
                    super.onSuccess(responseValue)
                    responseValue?.let {
                        listComment.find { cmt -> cmt.id == commentId }?.let {
                            it.statusLike = Comment.LikeStatus.NOT_YET_LIKED
                            it.likes -= 1
                            mvpView.fillDataToComment(listComment)
                        }
                    }
                }

                override fun onError(e: ActionException) {
                    super.onError(e)
                    if (e.cause is LogoutException) {
                        logOut()
                    } else {
                        Toast.makeText(this@StoryCoverActivity, e.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            })
    }

    private fun gotoCommentDetail(storyId: Int, comment: Comment?, comments: List<Comment>) {
        val commentDetailDialog = if (comment != null) {
            CommentDetailDialog.newInstance(storyId, comment, comments)
        } else {
            CommentDetailDialog.newInstance(storyId, null, comments)
        }
        commentDetailDialog.setOnEventDialogListener(object :
            CommentDetailDialog.OnEventDialogListener {
            override fun onRefreshData(update: Boolean) {
                if (update) {
                    getComments(storyId, false)
                }
            }
        })
        commentDetailDialog.show(supportFragmentManager, commentDetailDialog.tag)
    }

    private fun refreshToken(
        token: String,
        clientId: Int,
        clientSecret: String,
        callback: () -> Unit
    ) {
        showLoading()
        val rv = RefreshToken.RV().apply {
            this.token = token
            this.clientId = clientId
            this.clientSecret = clientSecret
        }

        mActionManager.executeAction(
            RefreshToken(),
            rv,
            object : Action.SimpleActionCallback<UserInfo>() {
                override fun onSuccess(responseValue: UserInfo?) {
                    super.onSuccess(responseValue)
                    responseValue?.let {
                        callback()
                    } ?: run {
                        hideLoading()
                    }
                }

                override fun onError(e: ActionException) {
                    super.onError(e)
                    hideLoading()
                    if (e.cause is APIActionException) {
                        if ((e.cause as APIActionException).code == 801) {
                            Toast.makeText(
                                this@StoryCoverActivity,
                                (e.cause as APIActionException).message,
                                Toast.LENGTH_SHORT
                            )
                                .show()
                            logOut()
                        } else {
                            Toast.makeText(this@StoryCoverActivity, e.message, Toast.LENGTH_SHORT)
                                .show()
                        }
                    } else {
                        Toast.makeText(this@StoryCoverActivity, e.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            })
    }

    private fun ratingStory(newRate: Float, oldRate: Float, count: Int) {
        val rv = RatingStoryAction.RV().apply {
            this.storyId = mStoryId
            this.rating = newRate.toInt()
        }
        actionManager.executeAction(RatingStoryAction(), rv,
            object : Action.SimpleActionCallback<Rating>() {
                override fun onSuccess(responseValue: Rating?) {
                    super.onSuccess(responseValue)
                    if (responseValue != null) {
                        mStoryDetail.count = responseValue.count
                        mStoryDetail.score = newRate
                    }
                    mStoryDetail?.let {
                        mvpView.fillDataToTopGroup(it)
                    }
                    Toast.makeText(this@StoryCoverActivity, getString(R.string.story_cover_rating_success), Toast.LENGTH_SHORT)
                        .show()
                }

                override fun onError(e: ActionException) {
                    super.onError(e)
                    if (e.cause is LogoutException) {
                        logOut()
                    } else {
                        Toast.makeText(this@StoryCoverActivity, e.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                    mStoryDetail?.let {
                        mvpView.fillDataToTopGroup(it)
                    }
                }
            }
        )
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

    private fun logOut() {
        val fm: FragmentManager = supportFragmentManager
        val dialog: ExpiredSessionDialog = ExpiredSessionDialog.newInstance()
        dialog.setOnEventDialogListener(object : ExpiredSessionDialog.OnEventDialogListener {
            override fun onClickApply() {
                dialog.dismiss()
                DedeSharedPref.saveUserInfo(null)
                val intent = Intent(this@StoryCoverActivity, LoginActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
            }
        })
        dialog.show(fm, dialog.tag)
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