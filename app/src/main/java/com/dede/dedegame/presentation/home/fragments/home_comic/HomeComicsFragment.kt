package com.dede.dedegame.presentation.home.fragments.home_comic

import android.content.Intent
import android.os.Bundle
import com.dede.dedegame.R
import com.dede.dedegame.presentation.common.LogUtil
import com.dede.dedegame.presentation.common.tracker.DedeFirebaseTracker
import com.dede.dedegame.presentation.common.tracker.DedeFirebaseTrackerModel
import com.dede.dedegame.presentation.home.fragments.home_comic.states.CategoryState
import com.dede.dedegame.presentation.home.fragments.home_comic.states.RankState
import com.dede.dedegame.presentation.home.fragments.home_comic.story_list.StoryListActivity
import com.dede.dedegame.presentation.story_cover.StoryCoverActivity
import com.quangph.base.mvp.ICommand
import com.quangph.base.viewbinder.Layout
import com.quangph.jetpack.JetFragment


@Layout(R.layout.fragment_home_comics)
class HomeComicsFragment : JetFragment<HomeComicsFragmentView>() {

    object StateName {
        const val RANK = "RANK"
        const val CATEGORY = "CATEGORY"
    }

    override fun onPresenterReady() {
        super.onPresenterReady()
        setupStates()
//        getHomeData()
//        getRanking()
        trackingOnMainStoryScreen()
    }

    override fun onExecuteCommand(command: ICommand) {
        super.onExecuteCommand(command)

        when (command) {
            is HomeComicsFragmentView.GotoStoryDetailCmd -> {
                when(stateMachine.currentStateName){
                    StateName.CATEGORY -> {
                        trackingTapEventMainStory(EVENT_TAP_STORY_IN_CATEGORY, PARAM_STORY, command.id)
                    }
                    StateName.RANK -> {
                        trackingTapEventMainStory(EVENT_TAP_STORY_IN_RANK, PARAM_STORY, command.id)
                    }
                }
                StoryCoverActivity.launchScreen(activity, command.id)
            }

            is HomeComicsFragmentView.GotoStoryListCmd -> {
                StoryListActivity.launchScreen(activity, command.category.id)
            }

            is HomeComicsFragmentView.OnclickCategoryCmd -> {
                transitToState(StateName.CATEGORY)
            }

            is HomeComicsFragmentView.OnclickRankCmd -> {
                transitToState(StateName.RANK)
            }
        }
    }

    private fun setupStates() {
        val categoryState = CategoryState(this, mvpView)
        addState(StateName.CATEGORY, categoryState)
        val rankState = RankState(this, mvpView)
        addState(StateName.RANK, rankState)
        initState(StateName.CATEGORY)

    }


    private fun trackingOnMainStoryScreen() {
        val fbModel = FirebaseLoginModel().apply {
            this.eventName = EVENT_ON_MAIN_STORY
            this.param = "on_screen"
            this.paramValue = "on_screen"
        }
        DedeFirebaseTracker.track(fbModel)
    }

    private fun trackingTapEventMainStory(eventName: String, param: String, paramValue: Any?) {
        val fbModel = FirebaseLoginModel().apply {
            this.eventName = eventName
            this.param = param
            this.paramValue = paramValue.toString()
        }
        DedeFirebaseTracker.track(fbModel)
    }

    inner class FirebaseLoginModel : DedeFirebaseTrackerModel() {
        override var screenName: String? = this@HomeComicsFragment.javaClass.simpleName
        var param: String = ""
        var paramValue: String = ""

        override fun createParams(bundle: Bundle) {
            super.createParams(bundle)
            bundle.putString(param, paramValue)
        }
    }

    companion object {
        const val EVENT_ON_MAIN_STORY = "event_on_main_story"
        const val EVENT_TAP_STORY_IN_CATEGORY = "event_tap_story_in_category"
        const val EVENT_TAP_STORY_IN_RANK = "event_tap_story_in_rank"
        const val PARAM_STORY = "story_id"
    }

}