package com.dede.dedegame.presentation.home.fragments.home

import android.content.Intent
import android.util.Log
import com.dede.dedegame.R
import com.dede.dedegame.domain.model.Rank
import com.dede.dedegame.domain.model.StoryDetail
import com.dede.dedegame.domain.model.home.Home
import com.dede.dedegame.domain.usecase.GetHomeDataAction
import com.dede.dedegame.domain.usecase.GetRankingAction
import com.dede.dedegame.presentation.common.LogUtil
import com.dede.dedegame.presentation.common.TimeUtil
import com.dede.dedegame.presentation.home.fragments.home.states.NewsTabState
import com.dede.dedegame.presentation.home.fragments.home.states.RankTabState
import com.dede.dedegame.presentation.home.fragments.home.states.TrendTabState
import com.dede.dedegame.presentation.home.news.NewsDetailActivity
import com.dede.dedegame.presentation.story_cover.StoryCoverActivity
import com.quangph.base.mvp.ICommand
import com.quangph.base.mvp.action.Action.SimpleActionCallback
import com.quangph.base.mvp.action.ActionException
import com.quangph.base.mvp.action.CompoundCallback
import com.quangph.base.mvp.action.scheduler.AsyncTaskScheduler
import com.quangph.base.viewbinder.Layout
import com.quangph.jetpack.JetFragment
import java.util.Calendar


@Layout(R.layout.fragment_home)
class HomeFragment : JetFragment<HomeFragmentView>() {

    object StateName {
        const val NEWS = "NEWS_TAB"
        const val RANK = "RANK_TAB"
        const val TREND = "TREND_TAB"
    }

    lateinit var newsState : NewsTabState
    lateinit var rankState : RankTabState
    lateinit var trendTabState : TrendTabState

    override fun onPresenterReady() {
        super.onPresenterReady()
        setupStates()
        getHomeData()
    }

    override fun onExecuteCommand(command: ICommand) {
        super.onExecuteCommand(command)

        when (command) {
            is HomeFragmentView.GotoStoryDetailCmd -> {
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
                NewsDetailActivity.launchScreen(activity, command.item.id)
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
        val callback = object  : SimpleActionCallback<Home>() {
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

}