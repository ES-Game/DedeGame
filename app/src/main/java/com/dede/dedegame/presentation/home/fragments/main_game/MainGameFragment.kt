package com.dede.dedegame.presentation.home.fragments.main_game

import android.content.Intent
import com.dede.dedegame.R
import com.dede.dedegame.presentation.common.LogUtil
import com.dede.dedegame.presentation.common.TimeUtil
import com.dede.dedegame.presentation.story_cover.StoryCoverActivity
import com.quangph.base.mvp.ICommand
import com.quangph.base.mvp.action.Action.SimpleActionCallback
import com.quangph.base.mvp.action.ActionException
import com.quangph.base.viewbinder.Layout
import com.quangph.dedegame.domain.model.Home
import com.quangph.dedegame.domain.model.Rank
import com.quangph.dedegame.domain.model.StoryDetail
import com.dede.dedegame.domain.usecase.GetHomeDataAction
import com.dede.dedegame.domain.usecase.GetRankingAction
import com.quangph.jetpack.JetFragment
import java.util.Calendar

@Layout(R.layout.fragment_main_game)
class MainGameFragment : JetFragment<MainGameFragmentView>() {
    // TODO: Rename and change types of parameters

    override fun onPresenterReady() {
        super.onPresenterReady()

    }

    override fun onExecuteCommand(command: ICommand) {
        super.onExecuteCommand(command)

        when (command) {
            is MainGameFragmentView.GotoStoryDetailCmd -> {
            }
        }
    }


}