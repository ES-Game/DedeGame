package com.dede.dedegame.presentation.home.fragments.home_comic.states

import android.util.Log
import com.dede.dedegame.domain.model.OldHome
import com.dede.dedegame.domain.usecase.GetCategoriesDataAction
import com.dede.dedegame.presentation.home.fragments.home_comic.HomeComicsFragment
import com.dede.dedegame.presentation.home.fragments.home_comic.HomeComicsFragmentView
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