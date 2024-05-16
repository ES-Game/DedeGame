package com.dede.dedegame.presentation.home.fragments.home_comic.states


import com.dede.dedegame.domain.usecase.GetRankingAction
import com.dede.dedegame.presentation.common.TimeUtil
import com.dede.dedegame.presentation.home.fragments.home_comic.HomeComicsFragment
import com.dede.dedegame.presentation.home.fragments.home_comic.HomeComicsFragmentView
import com.quangph.base.mvp.action.Action
import com.quangph.base.mvp.action.ActionException
import com.quangph.base.mvp.mvpcomponent.MVPState
import com.dede.dedegame.domain.model.Rank
import java.util.Calendar

class RankState(stateContext: HomeComicsFragment, view: HomeComicsFragmentView): MVPState<HomeComicsFragment, HomeComicsFragmentView>(stateContext, view){

    override fun onEnter() {
        super.onEnter()
        mView.setupRankLayout()
        getRanking()
    }

    private fun getRanking() {
        mStateContext.showLoading()
        mStateContext.actionManager.stopAction(GetRankingAction::class.java)
        val previousDate = Calendar.getInstance()
        previousDate.add(Calendar.DAY_OF_YEAR, -14)
        val currentDate = Calendar.getInstance()

        val rv = GetRankingAction.RV().apply {
            contextName = RankState::class.java.name
            this.from = TimeUtil.format2(previousDate.time)
            this.to = TimeUtil.format2(currentDate.time)
            this.categoryId = 0
            this.limit = 6
        }

        mStateContext.actionManager.executeAction(GetRankingAction(), rv, object : Action.SimpleActionCallback<Rank>() {
            override fun onSuccess(responseValue: Rank?) {
                super.onSuccess(responseValue)
                mStateContext.hideLoading()
                if (responseValue != null) {
                    responseValue.all?.let { mView.showStoryRankList(it) }
                }
            }

            override fun onError(e: ActionException) {
                super.onError(e)
                mStateContext.hideLoading()
            }
        })
    }
}