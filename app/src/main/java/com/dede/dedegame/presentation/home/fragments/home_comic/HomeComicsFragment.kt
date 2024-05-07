package com.dede.dedegame.presentation.home.fragments.home_comic

import android.content.Intent
import com.dede.dedegame.R
import com.dede.dedegame.presentation.chapter.ChapterActivity

import com.dede.dedegame.presentation.home.fragments.home_comic.states.CategoryState
import com.dede.dedegame.presentation.home.fragments.home_comic.states.RankState
import com.quangph.base.mvp.ICommand
import com.quangph.base.mvp.action.Action.SimpleActionCallback
import com.quangph.base.mvp.action.ActionException
import com.quangph.base.viewbinder.Layout
import com.quangph.dedegame.domain.model.Home
import com.quangph.dedegame.domain.model.Rank
import com.quangph.dedegame.domain.model.StoryDetail

import com.quangph.jetpack.JetFragment
import java.util.Calendar


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
                goToStoryDetail(command.item)
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





    private fun goToStoryDetail(storyDetail: StoryDetail) {
        val intent = Intent(activity, ChapterActivity:: class.java)
        intent.putExtra("storyId", storyDetail.id)
        activity?.startActivity(intent)
    }

}