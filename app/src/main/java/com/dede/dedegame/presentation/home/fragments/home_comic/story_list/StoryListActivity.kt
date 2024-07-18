package com.dede.dedegame.presentation.home.fragments.home_comic.story_list

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.dede.dedegame.R
import com.dede.dedegame.domain.model.StoryDetail
import com.dede.dedegame.domain.model.StoryListDataPage
import com.dede.dedegame.domain.usecase.GetStoryByCategoryId
import com.dede.dedegame.presentation.common.tracker.DedeFirebaseTracker
import com.dede.dedegame.presentation.common.tracker.DedeFirebaseTrackerModel
import com.dede.dedegame.presentation.story_cover.StoryCoverActivity
import com.quangph.base.mvp.ICommand
import com.quangph.base.mvp.action.Action
import com.quangph.base.mvp.action.ActionException
import com.quangph.base.viewbinder.Layout
import com.quangph.jetpack.JetActivity

@Layout(R.layout.activity_story_list)
class StoryListActivity : JetActivity<StoryListView>() {
    private var currentPage = 0
    var categoryId: Int? = null

    companion object {
        const val EVENT_ON_STORY_LIST = "event_on_story_list"
        const val EVENT_TAP_STORY = "event_tap_story"
        const val PARAM_STORY = "story_id"
        fun launchScreen(
            context: Context?,
            categoryId: Int?
        ) {
            val intent = Intent(context, StoryListActivity::class.java)
            intent.putExtra("category_id", categoryId)
            context?.startActivity(intent)
        }
    }

    override fun onPresenterReady() {
        super.onPresenterReady()
        trackingOnStoryListScreen()
        categoryId = intent.getIntExtra("category_id", -1)
        categoryId?.let {
            getStoriesById(it, true)
        }
    }

    override fun onExecuteCommand(command: ICommand) {
        super.onExecuteCommand(command)
        when (command) {
            is StoryListView.OnBackCmd -> {
                onBackPressedDispatcher.onBackPressed()
            }

            is StoryListView.LoadMoreCmd -> {
                categoryId?.let { getStoriesById(it, false) }
            }

            is StoryListView.GotoStoryCoverCmd -> {
                trackingTapEventStoryList(EVENT_TAP_STORY, PARAM_STORY, command.storyDetail.id)
                StoryCoverActivity.launchScreen(this, command.storyDetail.id)
            }
        }
    }

    private fun getStoriesById(categoryId: Int, isInitialLoad: Boolean) {
        if (isInitialLoad) {
            showLoading()
            currentPage = 1
        } else {
            currentPage += 1
        }

        val rv = GetStoryByCategoryId.RV().apply {
            this.categoryId = categoryId
            this.page = currentPage
        }

        mActionManager.executeAction(
            GetStoryByCategoryId(),
            rv,
            object : Action.SimpleActionCallback<StoryListDataPage<StoryDetail>>() {
                override fun onSuccess(responseValue: StoryListDataPage<StoryDetail>?) {
                    super.onSuccess(responseValue)
                    if (isInitialLoad) {
                        hideLoading()
                        responseValue?.title?.let {
                            mvpView.setupTitleToolbar(it)
                        }
                        responseValue?.dataList?.let {
                            mvpView.fillStoryToAdapter(it)
                        }
                    } else {
                        responseValue?.dataList?.let {
                            mvpView.loadMore(it, responseValue.hasNextPage)
                        }
                    }
                }

                override fun onError(e: ActionException) {
                    super.onError(e)
                    hideLoading()
                }
            })
    }

    private fun trackingOnStoryListScreen() {
        val fbModel = FirebaseStoryListModel().apply {
            this.eventName = EVENT_ON_STORY_LIST
            this.param = "on_screen"
            this.paramValue = "on_screen"
        }
        DedeFirebaseTracker.track(fbModel)
    }

    private fun trackingTapEventStoryList(eventName: String, param: String, paramValue: Any?) {
        val fbModel = FirebaseStoryListModel().apply {
            this.eventName = eventName
            this.param = param
            this.paramValue = paramValue.toString()
        }
        DedeFirebaseTracker.track(fbModel)
    }

    inner class FirebaseStoryListModel : DedeFirebaseTrackerModel() {
        override var screenName: String? = this@StoryListActivity.javaClass.simpleName
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
}