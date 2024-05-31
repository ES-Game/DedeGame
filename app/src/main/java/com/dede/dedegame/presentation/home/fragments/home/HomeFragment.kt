package com.dede.dedegame.presentation.home.fragments.home

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.dede.dedegame.R
import com.dede.dedegame.domain.model.StoryDetail
import com.dede.dedegame.domain.model.home.Home
import com.dede.dedegame.domain.model.home.Slider
import com.dede.dedegame.domain.usecase.GetHomeDataAction
import com.dede.dedegame.presentation.common.tracker.DedeFirebaseTracker
import com.dede.dedegame.presentation.common.tracker.DedeFirebaseTrackerModel
import com.dede.dedegame.presentation.home.fragments.home.states.NewsTabState
import com.dede.dedegame.presentation.home.fragments.home.states.RankTabState
import com.dede.dedegame.presentation.home.fragments.home.states.TrendTabState
import com.dede.dedegame.presentation.home.fragments.home_comic.story_list.StoryListActivity
import com.dede.dedegame.presentation.home.game.GameDetailActivity
import com.dede.dedegame.presentation.home.news.NewsDetailActivity
import com.dede.dedegame.presentation.story_cover.StoryCoverActivity
import com.quangph.base.mvp.ICommand
import com.quangph.base.mvp.action.Action.SimpleActionCallback
import com.quangph.base.mvp.action.ActionException
import com.quangph.base.mvp.action.scheduler.AsyncTaskScheduler
import com.quangph.base.viewbinder.Layout
import com.quangph.jetpack.JetFragment


@Layout(R.layout.fragment_home)
class HomeFragment : JetFragment<HomeFragmentView>() {

    object StateName {
        const val NEWS = "NEWS_TAB"
        const val RANK = "RANK_TAB"
        const val TREND = "TREND_TAB"
    }

    lateinit var newsState: NewsTabState
    lateinit var rankState: RankTabState
    lateinit var trendTabState: TrendTabState

    override fun onPresenterReady() {
        super.onPresenterReady()
        setupStates()
        getHomeData()
        trackingOnHomeScreen()
    }

    override fun onExecuteCommand(command: ICommand) {
        super.onExecuteCommand(command)

        when (command) {
            is HomeFragmentView.GotoStoryDetailCmd -> {
                trackingTapEventHome(EVENT_TAP_STORY_ITEM, PARAM_STORY, command.item.id)
                goToStoryDetail(command.item)
            }

            is HomeFragmentView.GotoTabCmd -> {
                when (command.position) {
                    0 -> {
                        transitToState(StateName.NEWS)
                    }

                    1 -> {
                        transitToState(StateName.RANK)
                    }

                    2 -> {
                        transitToState(StateName.TREND)
                    }
                }
            }

            is HomeFragmentView.GotoNewsDetailCmd -> {
                trackingTapEventHome(EVENT_TAP_NEWS_ITEM, PARAM_NEWS, command.item.id)
                NewsDetailActivity.launchScreen(activity, command.item.id)
            }

            is HomeFragmentView.GotoScreenByTypeCmd -> {
                when (command.item.type) {
                    Slider.Type.COMIC_CATEGORY -> {
                        trackingTapEventHome(EVENT_TAP_SLIDER_ITEM, PARAM_SLIDER_COMIC_CATEGORY, command.item.sid)
                        StoryListActivity.launchScreen(activity, command.item.sid)
                    }

                    else -> {
                        trackingTapEventHome(EVENT_TAP_SLIDER_ITEM, PARAM_SLIDER_GAME_DETAIL, command.item.sid)
                        GameDetailActivity.launchScreen(activity, command.item.sid)
                    }
                }
            }
        }
    }

    private fun setupStates() {
        newsState = NewsTabState(this@HomeFragment, mvpView)
        rankState = RankTabState(this@HomeFragment, mvpView)
        trendTabState = TrendTabState(this@HomeFragment, mvpView)
        addState(StateName.NEWS, newsState)
        addState(StateName.RANK, rankState)
        addState(StateName.TREND, trendTabState)
        initState(StateName.NEWS)
    }

    private fun getHomeData() {
        showLoading()
        val callback = object : SimpleActionCallback<Home>() {
            override fun onSuccess(responseValue: Home?) {
                super.onSuccess(responseValue)
                hideLoading()

                if (responseValue != null) {
                    responseValue.sliders?.let {
                        mvpView.showTopBanner(it)
                    }
                    responseValue.articles?.let {
                        newsState.setData(it)
                    }
                }
            }

            override fun onError(e: ActionException) {
                super.onError(e)
                hideLoading()
                Toast.makeText(activity, e.message, Toast.LENGTH_SHORT).show()
            }
        }

        val rv = GetHomeDataAction.RV()
        actionManager.executeAction(
            GetHomeDataAction(),
            rv,
            callback,
            AsyncTaskScheduler()
        )
    }

    private fun goToStoryDetail(storyDetail: StoryDetail) {
        val intent = Intent(activity, StoryCoverActivity::class.java)
        intent.putExtra("storyId", storyDetail.id)
        activity?.startActivity(intent)
    }

    private fun trackingOnHomeScreen() {
        val fbModel = FirebaseLoginModel().apply {
            this.eventName = EVENT_ON_HOME
            this.param = "on_screen"
            this.paramValue = "on_screen"
        }
        DedeFirebaseTracker.track(fbModel)
    }

    private fun trackingTapEventHome(eventName: String, param: String, paramValue: Any?) {
        val fbModel = FirebaseLoginModel().apply {
            this.eventName = eventName
            this.param = param
            this.paramValue = paramValue.toString()
        }
        DedeFirebaseTracker.track(fbModel)
    }

    inner class FirebaseLoginModel : DedeFirebaseTrackerModel() {
        override var screenName: String? = this@HomeFragment.javaClass.simpleName
        var param: String = ""
        var paramValue: String = ""

        override fun createParams(bundle: Bundle) {
            super.createParams(bundle)
            bundle.putString(param, paramValue)
        }
    }

    companion object {
        const val EVENT_ON_HOME = "event_on_home"
        const val EVENT_TAP_SLIDER_ITEM = "event_tap_slider_item"
        const val EVENT_TAP_STORY_ITEM = "event_tap_story_item"
        const val EVENT_TAP_NEWS_ITEM = "event_tap_news_item"
        const val PARAM_STORY = "story_id"
        const val PARAM_SLIDER_GAME_DETAIL = "slider_game_detail_sid"
        const val PARAM_SLIDER_COMIC_CATEGORY = "slider_comic_category_sid"
        const val PARAM_NEWS = "news_id"
    }

}