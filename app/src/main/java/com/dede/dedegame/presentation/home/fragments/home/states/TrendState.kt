package com.dede.dedegame.presentation.home.fragments.home.states

import com.dede.dedegame.domain.model.StoryDetail
import com.dede.dedegame.presentation.home.fragments.home.HomeFragment
import com.dede.dedegame.presentation.home.fragments.home.HomeFragmentView
import com.quangph.base.mvp.mvpcomponent.MVPState


class TrendState(stateContext: HomeFragment, view: HomeFragmentView) :
    MVPState<HomeFragment, HomeFragmentView>(stateContext, view) {

    override fun onEnter() {
        super.onEnter()
    }

    var dataRank = listOf<StoryDetail>()

    fun setData(list: List<StoryDetail>) {
        this.dataRank = list
    }
}