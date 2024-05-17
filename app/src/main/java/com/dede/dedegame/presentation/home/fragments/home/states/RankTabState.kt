package com.dede.dedegame.presentation.home.fragments.home.states


import com.dede.dedegame.domain.model.Rank
import com.dede.dedegame.domain.model.StoryDetail
import com.dede.dedegame.domain.usecase.GetHomeDataAction
import com.dede.dedegame.domain.usecase.GetRankingAction
import com.dede.dedegame.presentation.common.TimeUtil
import com.dede.dedegame.presentation.home.fragments.home.HomeFragment
import com.dede.dedegame.presentation.home.fragments.home.HomeFragmentView
import com.quangph.base.mvp.action.Action
import com.quangph.base.mvp.action.ActionException
import com.quangph.base.mvp.action.scheduler.AsyncTaskScheduler
import com.quangph.base.mvp.mvpcomponent.MVPState
import java.util.Calendar

class RankTabState(stateContext: HomeFragment, view: HomeFragmentView) :
    MVPState<HomeFragment, HomeFragmentView>(stateContext, view) {

    override fun onEnter() {
        super.onEnter()
        mView.validateRankGroup()
        getRanking()
    }
    private fun getRanking() {
        mStateContext.showLoading()

        val rankDataCallback = object : Action.SimpleActionCallback<Rank>() {
            override fun onSuccess(responseValue: Rank?) {
                super.onSuccess(responseValue)
                mStateContext.hideLoading()

                if (responseValue != null) {
                    responseValue.all?.let {
                        mView.showRankData(it)
                    }
                }
            }

            override fun onError(e: ActionException) {
                super.onError(e)
                    mStateContext.hideLoading()
            }
        }

        val rvHome = GetHomeDataAction.RV()

        val previousDate = Calendar.getInstance()
        previousDate.add(Calendar.DAY_OF_YEAR, -14)
        val currentDate = Calendar.getInstance()

        val rvRank = GetRankingAction.RV().apply {
            contextName = HomeFragment::class.java.name
            this.from = TimeUtil.format2(previousDate.time)
            this.to = TimeUtil.format2(currentDate.time)
            this.categoryId = 0
            this.limit = 6
        }

        mStateContext.actionManager.executeAction(GetRankingAction(), rvRank, rankDataCallback, AsyncTaskScheduler())
    }

}