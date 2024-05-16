package com.dede.dedegame.presentation.home.fragments.home.states

import com.dede.dedegame.domain.model.home.Article
import com.dede.dedegame.presentation.home.fragments.home.HomeFragment
import com.dede.dedegame.presentation.home.fragments.home.HomeFragmentView
import com.quangph.base.mvp.mvpcomponent.MVPState


class NewsTabState(stateContext: HomeFragment, view: HomeFragmentView) :
    MVPState<HomeFragment, HomeFragmentView>(stateContext, view) {

    override fun onEnter() {
        super.onEnter()
        mView.validateNewsGroup()
    }

    fun setData(listArticle: List<Article>) {

        mView.showNewsData(listArticle)
    }
}