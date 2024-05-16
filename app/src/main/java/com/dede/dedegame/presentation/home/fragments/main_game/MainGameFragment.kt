package com.dede.dedegame.presentation.home.fragments.main_game

import com.dede.dedegame.R
import com.quangph.base.mvp.ICommand
import com.quangph.base.viewbinder.Layout
import com.quangph.jetpack.JetFragment

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