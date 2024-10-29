package com.dede.dedegame.presentation.story_search

import android.widget.Toast
import com.dede.dedegame.R
import com.dede.dedegame.domain.model.StoryDetail
import com.dede.dedegame.domain.model.StoryListDataPage
import com.dede.dedegame.domain.usecase.SearchStoryAction
import com.dede.dedegame.presentation.story_cover.StoryCoverActivity
import com.quangph.base.mvp.ICommand
import com.quangph.base.mvp.action.Action
import com.quangph.base.mvp.action.ActionException
import com.quangph.base.viewbinder.Layout
import com.quangph.jetpack.JetActivity

@Layout(R.layout.activity_story_search)
class StorySearchActivity : JetActivity<StorySearchView>() {

    override fun onStart() {
        super.onStart()
    }

    override fun onPresenterReady() {
        super.onPresenterReady()
    }

    override fun onExecuteCommand(command: ICommand) {
        super.onExecuteCommand(command)
        when (command) {
            is StorySearchView.OnBackCmd -> {
                onBackPressedDispatcher.onBackPressed()
            }

            is StorySearchView.SearchStoryCmd -> {
                if (mvpView.getCurrentKeyword().trim() != command.keyword.trim()) {
                    mvpView.setCurrentKeyword(command.keyword)
                    mvpView.resetListView()
                    searchStory(command.keyword, true)
                } else {
                    mvpView.clearFocusEditText()
                }
            }

            is StorySearchView.LoadMoreCmd -> {
                searchStory(mvpView.getCurrentKeyword(), false)
            }

            is StorySearchView.GotoStoryCoverCmd -> {
                StoryCoverActivity.launchScreen(this, command.storyDetail.id)
            }
        }
    }

    private fun searchStory(keyword: String, isInitialLoad: Boolean) {
        if (isInitialLoad) {
            mvpView.setCurrentPage(1)
        } else {
            mvpView.setCurrentPage(mvpView.currentPage() + 1)
        }
        mvpView.setLoading(true)
        mvpView.addLoadingFooter()
        val rv = SearchStoryAction.RV().apply {
            this.keyword = keyword
            this.page = mvpView.currentPage()
        }

        mActionManager.executeAction(
            SearchStoryAction(),
            rv,
            object : Action.SimpleActionCallback<StoryListDataPage<StoryDetail>>() {
                override fun onSuccess(responseValue: StoryListDataPage<StoryDetail>?) {
                    super.onSuccess(responseValue)
                    mvpView.removeLoadingFooter()
                    responseValue?.dataList?.let { dataList ->
                        if (mvpView.currentPage() == 1) {
                            mvpView.showEmptyView(dataList.isEmpty())
                        }
                        mvpView.setLoading(false)
                        mvpView.setLastPage(responseValue.lastPage)
                        if (isInitialLoad) {
                            mvpView.fillStoryToAdapter(dataList)
                        } else {
                            mvpView.loadMore(dataList)
                        }
                    }
                }

                override fun onError(e: ActionException) {
                    super.onError(e)
                    mvpView.removeLoadingFooter()
                    Toast.makeText(this@StorySearchActivity, e.message, Toast.LENGTH_SHORT).show()
                }
            })
    }

}