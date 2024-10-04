package com.dede.dedegame.presentation.story_followed

import com.dede.dedegame.R
import com.dede.dedegame.domain.model.StoryDetail
import com.dede.dedegame.domain.model.StoryListDataPage
import com.dede.dedegame.domain.usecase.GetFollowedStories
import com.dede.dedegame.presentation.story_cover.StoryCoverActivity
import com.quangph.base.mvp.ICommand
import com.quangph.base.mvp.action.Action
import com.quangph.base.mvp.action.ActionException
import com.quangph.base.viewbinder.Layout
import com.quangph.jetpack.JetActivity

@Layout(R.layout.activity_story_bookmark)
class StoryBookmarkActivity : JetActivity<StoryBookmarkView>() {
    private var currentPage = 0

    override fun onStart() {
        super.onStart()
        getFollowedStories(true)
    }

    override fun onPresenterReady() {
        super.onPresenterReady()
    }

    override fun onExecuteCommand(command: ICommand) {
        super.onExecuteCommand(command)
        when (command) {
            is StoryBookmarkView.OnBackCmd -> {
                onBackPressedDispatcher.onBackPressed()
            }

            is StoryBookmarkView.LoadMoreCmd -> {
                getFollowedStories(false)
            }

            is StoryBookmarkView.GotoStoryCoverCmd -> {
                StoryCoverActivity.launchScreen(this, command.storyDetail.id)
            }
        }
    }

    private fun getFollowedStories(isInitialLoad: Boolean) {
        if (isInitialLoad) {
            showLoading()
            currentPage = 1
        } else {
            currentPage += 1
        }

        val rv = GetFollowedStories.RV().apply {
            this.page = currentPage
        }

        mActionManager.executeAction(
            GetFollowedStories(),
            rv,
            object : Action.SimpleActionCallback<StoryListDataPage<StoryDetail>>() {
                override fun onSuccess(responseValue: StoryListDataPage<StoryDetail>?) {
                    super.onSuccess(responseValue)
                    if (isInitialLoad) {
                        hideLoading()
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

}