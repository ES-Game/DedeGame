package com.dede.dedegame.presentation.home.fragments.home

import android.content.Intent
import com.dede.dedegame.R
import com.dede.dedegame.presentation.home.common.TimeUtil
import com.quangph.base.mvp.ICommand
import com.quangph.base.mvp.action.Action.SimpleActionCallback
import com.quangph.base.mvp.action.ActionException
import com.quangph.base.viewbinder.Layout
import com.quangph.dedegame.domain.model.Home
import com.quangph.dedegame.domain.model.Rank
import com.quangph.dedegame.domain.model.StoryDetail
import com.quangph.dedegame.domain.usecase.GetHomeDataAction
import com.quangph.dedegame.domain.usecase.GetRankingAction
import com.quangph.dedegame.presentation.chapter.ChapterActivity
import com.quangph.jetpack.JetFragment
import java.util.Calendar


@Layout(R.layout.fragment_home)
class HomeFragment : JetFragment<HomeFragmentView>() {
    // TODO: Rename and change types of parameters

    override fun onPresenterReady() {
        super.onPresenterReady()
        getHomeData()
        getRanking()
    }

    override fun onExecuteCommand(command: ICommand) {
        super.onExecuteCommand(command)

        when (command) {
            is HomeFragmentView.GotoStoryDetailCmd -> {
                goToStoryDetail(command.item)
            }
        }
    }

    private fun getHomeData() {
        showLoading()
        val rv = GetHomeDataAction.RV().apply {
            this.limit = 6
        }

        actionManager.executeAction(GetHomeDataAction(), rv, object : SimpleActionCallback<Home>() {
            override fun onSuccess(responseValue: Home?) {
                super.onSuccess(responseValue)
                hideLoading()
                if (responseValue != null) {
                    responseValue.featuredStories?.let {
                        mvpView.showTopBanner(it)

                    }
                }
            }

            override fun onError(e: ActionException) {
                super.onError(e)
                hideLoading()
            }
        })
    }

    private fun getRanking() {
        showLoading()
        val previousDate = Calendar.getInstance()
        previousDate.add(Calendar.DAY_OF_YEAR, -14)
        val currentDate = Calendar.getInstance()

        val rv = GetRankingAction.RV().apply {
            this.from = TimeUtil.format2(previousDate.time)
            this.to = TimeUtil.format2(currentDate.time)
            this.categoryId = 0
            this.limit = 6
        }

        actionManager.executeAction(GetRankingAction(), rv, object : SimpleActionCallback<Rank>() {
            override fun onSuccess(responseValue: Rank?) {
                super.onSuccess(responseValue)
                hideLoading()
                if (responseValue != null) {
                    responseValue.all?.let { mvpView.showListStory(it) }
                }
            }

            override fun onError(e: ActionException) {
                super.onError(e)
                hideLoading()
            }
        })
    }

    private fun goToStoryDetail(storyDetail: StoryDetail) {
        val intent = Intent(activity, ChapterActivity:: class.java)
        intent.putExtra("storyId", storyDetail.id)
        activity?.startActivity(intent)
    }

}