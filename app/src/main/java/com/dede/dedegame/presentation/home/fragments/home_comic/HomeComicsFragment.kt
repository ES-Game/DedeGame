package com.dede.dedegame.presentation.home.fragments.home_comic

import android.content.Intent
import com.dede.dedegame.R
import com.dede.dedegame.presentation.home.fragments.home_comic.states.CategoryState
import com.dede.dedegame.presentation.home.fragments.home_comic.states.RankState
import com.dede.dedegame.presentation.story_cover.StoryCoverActivity
import com.quangph.base.mvp.ICommand
import com.quangph.base.viewbinder.Layout
import com.quangph.jetpack.JetFragment


@Layout(R.layout.fragment_home_comics)
class HomeComicsFragment : JetFragment<HomeComicsFragmentView>() {

    object StateName {
        const val RANK = "RANK"
        const val CATEGORY = "CATEGORY"
    }

    override fun onPresenterReady() {
        super.onPresenterReady()
        setupStates()
//        getHomeData()
//        getRanking()
    }

    override fun onExecuteCommand(command: ICommand) {
        super.onExecuteCommand(command)

        when (command) {
            is HomeComicsFragmentView.GotoStoryDetailCmd -> {
                goToStoryDetail(command.id)
            }

            is HomeComicsFragmentView.OnclickCategoryCmd -> {
                transitToState(StateName.CATEGORY)
            }

            is HomeComicsFragmentView.OnclickRankCmd -> {
                transitToState(StateName.RANK)
            }
        }
    }

    private fun setupStates() {
        val categoryState = CategoryState(this, mvpView)
        addState(StateName.CATEGORY, categoryState)
        val rankState = RankState(this, mvpView)
        addState(StateName.RANK, rankState)
        initState(StateName.CATEGORY)

    }


    private fun goToStoryDetail(id: Int) {
        val intent = Intent(activity, StoryCoverActivity::class.java)
        intent.putExtra("storyId", id)
        activity?.startActivity(intent)
    }

}