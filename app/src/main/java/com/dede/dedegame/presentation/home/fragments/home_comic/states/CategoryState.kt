package com.dede.dedegame.presentation.home.fragments.home_comic.states

import com.dede.dedegame.presentation.home.fragments.home.HomeFragment
import com.dede.dedegame.presentation.home.fragments.home_comic.HomeComicsFragment
import com.dede.dedegame.presentation.home.fragments.home_comic.HomeComicsFragmentView
import com.quangph.base.mvp.action.Action
import com.quangph.base.mvp.action.ActionException
import com.quangph.base.mvp.mvpcomponent.MVPState
import com.quangph.dedegame.domain.model.Home
import com.quangph.dedegame.domain.usecase.GetHomeDataAction

class CategoryState(stateContext: HomeComicsFragment, view: HomeComicsFragmentView): MVPState<HomeComicsFragment, HomeComicsFragmentView>(stateContext, view){

    override fun onEnter() {
        super.onEnter()
    }

    private fun getHomeData() {
        mStateContext.showLoading()
        val rv = GetHomeDataAction.RV().apply {
            this.limit = 6
        }

        mStateContext.actionManager.executeAction(GetHomeDataAction(), rv, object : Action.SimpleActionCallback<Home>() {
            override fun onSuccess(responseValue: Home?) {
                super.onSuccess(responseValue)
                mStateContext.hideLoading()
                if (responseValue != null) {
                    responseValue.featuredStories?.let {

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