package com.dede.dedegame.presentation.home.fragments.home

import android.content.Intent
import android.util.Log
import com.dede.dedegame.R
import com.dede.dedegame.domain.usecase.GetHomeDataAction
import com.dede.dedegame.domain.usecase.GetRankingAction
import com.dede.dedegame.presentation.common.TimeUtil
import com.dede.dedegame.presentation.story_cover.StoryCoverActivity
import com.quangph.base.mvp.ICommand
import com.quangph.base.mvp.action.Action.SimpleActionCallback
import com.quangph.base.mvp.action.ActionException
import com.quangph.base.viewbinder.Layout
import com.dede.dedegame.domain.model.Rank
import com.dede.dedegame.domain.model.StoryDetail
import com.dede.dedegame.domain.model.home.Article
import com.dede.dedegame.domain.model.home.Home
import com.dede.dedegame.presentation.common.LogUtil
import com.dede.dedegame.presentation.home.fragments.home.states.NewsState
import com.dede.dedegame.presentation.home.fragments.home.states.RankState
import com.dede.dedegame.presentation.home.fragments.home.states.TrendState
import com.google.gson.Gson
import com.quangph.base.mvp.action.Action
import com.quangph.base.mvp.action.CompoundCallback
import com.quangph.base.mvp.action.scheduler.AsyncTaskScheduler

import com.quangph.jetpack.JetFragment
import java.util.Calendar


@Layout(R.layout.fragment_home)
class HomeFragment : JetFragment<HomeFragmentView>() {

    object StateName {
        const val NEWS = "NEWS_TAB"
        const val RANK = "RANK_TAB"
        const val TREND = "TREND_TAB"
    }

    override fun onPresenterReady() {
        super.onPresenterReady()
        getData()
    }

    private fun setupStates() {
        val newsState = NewsState(this@HomeFragment, mvpView)
        newsState.setData(listNews)
        addState(StateName.NEWS, newsState)
        val rankState = RankState(this@HomeFragment, mvpView)
        rankState.setData(listRanks)
        addState(StateName.RANK, rankState)
        val trendState = TrendState(this@HomeFragment, mvpView)
        trendState.setData(listRanks)
        addState(StateName.TREND, trendState)
        initState(StateName.NEWS)
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
        }
    }

    var listNews = listOf<Article>()
    var listRanks = listOf<StoryDetail>()

    private fun getData(){
        val homeDataCallback = object : SimpleActionCallback<Home>() {
            override fun onSuccess(responseValue: Home?) {
                super.onSuccess(responseValue)
                hideLoading()
                LogUtil.getInstance().e("homeDataCallback ===================================================================================")
                if (responseValue != null) {
                    responseValue.sliders?.let {
                        mvpView.showTopBanner(it)
                    }
                    responseValue.articles?.let {
                        listNews = it
                    }
                }
            }

            override fun onError(e: ActionException) {
                super.onError(e)
                hideLoading()
            }
        }

        val rankDataCallback = object : SimpleActionCallback<Rank>() {
            override fun onSuccess(responseValue: Rank?) {
                super.onSuccess(responseValue)
                hideLoading()
                LogUtil.getInstance().e("rankDataCallback ===================================================================================")
                if (responseValue != null) {
                    responseValue.all?.let {
                        listRanks = it
                    }
                }
            }

            override fun onError(e: ActionException) {
                super.onError(e)
                hideLoading()
            }
        }

        val rvHome = GetHomeDataAction.RV().apply {
            this.limit = 6
        }

        val previousDate = Calendar.getInstance()
        previousDate.add(Calendar.DAY_OF_YEAR, -14)
        val currentDate = Calendar.getInstance()

        val rvRank = GetRankingAction.RV().apply {
            this.from = TimeUtil.format2(previousDate.time)
            this.to = TimeUtil.format2(currentDate.time)
            this.categoryId = 0
            this.limit = 6
        }
        val callbackCompound = object : CompoundCallback(){
            override fun onCompletedAll() {
                super.onCompletedAll()
                LogUtil.getInstance().e("===================================================================================")
                Log.e("Api service", "Done")
                setupStates()
            }
        }
        actionManager.executeAction(GetHomeDataAction(), rvHome, homeDataCallback, AsyncTaskScheduler())
            .add(GetRankingAction(), rvRank, rankDataCallback, AsyncTaskScheduler()).onCompound(callbackCompound)

    }

    private fun goToStoryDetail(storyDetail: StoryDetail) {
        val intent = Intent(activity, StoryCoverActivity:: class.java)
        intent.putExtra("storyId", storyDetail.id)
        activity?.startActivity(intent)
    }

}