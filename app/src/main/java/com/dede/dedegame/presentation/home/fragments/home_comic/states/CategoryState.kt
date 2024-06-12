package com.dede.dedegame.presentation.home.fragments.home_comic.states

import android.util.Log
import com.dede.dedegame.domain.model.OldHome
import com.dede.dedegame.domain.usecase.GetCategoriesDataAction
import com.dede.dedegame.presentation.home.fragments.home_comic.HomeComicsFragment
import com.dede.dedegame.presentation.home.fragments.home_comic.HomeComicsFragmentView
import com.dede.dedegame.presentation.home.fragments.home_comic.story_list.StoryListActivity
import com.dede.dedegame.presentation.story_cover.StoryCoverActivity
import com.quangph.base.mvp.ICommand
import com.quangph.base.mvp.action.Action
import com.quangph.base.mvp.action.ActionException
import com.quangph.base.mvp.mvpcomponent.MVPState


class CategoryState(stateContext: HomeComicsFragment, view: HomeComicsFragmentView) :
    MVPState<HomeComicsFragment, HomeComicsFragmentView>(stateContext, view) {

    override fun onEnter() {
        super.onEnter()
        mView.setupCategoryLayout()
        getHomeData()
    }

    override fun onExecuteCommand(command: ICommand): Boolean {
        when (command) {
            is HomeComicsFragmentView.GotoStoryDetailCmd -> {
                mStateContext.trackingTapEventMainStory(
                    HomeComicsFragment.EVENT_TAP_STORY_IN_CATEGORY,
                    HomeComicsFragment.PARAM_STORY, command.id
                )
                StoryCoverActivity.launchScreen(mStateContext.activity, command.id)
                return true
            }

            is HomeComicsFragmentView.GotoStoryListCmd -> {
                mStateContext.trackingTapEventMainStory(
                    HomeComicsFragment.EVENT_TAP_STORY_VIEW_MORE,
                    HomeComicsFragment.PARAM_CATEGORY, command.category.id
                )
                StoryListActivity.launchScreen(mStateContext.activity, command.category.id)
                return true
            }
        }
        return super.onExecuteCommand(command)
    }

    private fun getHomeData() {

        mStateContext.showLoading()
        val rv = GetCategoriesDataAction.RV().apply {
            this.limit = 6
        }

        mStateContext.actionManager.executeAction(
            GetCategoriesDataAction(),
            rv,
            object : Action.SimpleActionCallback<OldHome>() {
                override fun onSuccess(responseValue: OldHome?) {
                    super.onSuccess(responseValue)
                    mStateContext.hideLoading()
                    if (responseValue != null) {
                        responseValue.categories?.let {
                            it.forEach { category ->
                                Log.e("Iterator List", "")
                                mView.showStoryCategories(category)
                            }

                        }
                    }
                }

                override fun onError(e: ActionException) {
                    super.onError(e)
                    mStateContext.hideLoading()
                }
            })
    }
}