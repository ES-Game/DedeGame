package com.dede.dedegame.presentation.home.fragments.home_comic.story_list

import android.content.Context
import android.content.Intent
import com.dede.dedegame.R
import com.dede.dedegame.domain.model.Category
import com.dede.dedegame.domain.model.DataPage
import com.dede.dedegame.domain.model.StoryDetail
import com.dede.dedegame.domain.usecase.GetStoryByCategoryId
import com.dede.dedegame.presentation.story_cover.StoryCoverActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
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
            object : Action.SimpleActionCallback<DataPage<StoryDetail>>() {
                override fun onSuccess(responseValue: DataPage<StoryDetail>?) {
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
}