package com.dede.dedegame.presentation.home.fragments.home.states

import com.dede.dedegame.domain.model.home.Article
import com.dede.dedegame.presentation.common.LogUtil
import com.dede.dedegame.presentation.home.fragments.home.HomeFragment
import com.dede.dedegame.presentation.home.fragments.home.HomeFragmentView
import com.google.gson.Gson
import com.quangph.base.mvp.mvpcomponent.MVPState


class NewsState(stateContext: HomeFragment, view: HomeFragmentView) :
    MVPState<HomeFragment, HomeFragmentView>(stateContext, view) {

    override fun onEnter() {
        super.onEnter()
    }

    fun setData(listArticle: List<Article>) {

        mView.showNewsData(listArticle)
    }
}