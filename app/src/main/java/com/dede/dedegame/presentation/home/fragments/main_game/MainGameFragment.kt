package com.dede.dedegame.presentation.home.fragments.main_game

import com.dede.dedegame.R
import com.dede.dedegame.domain.model.StoryDetail
import com.dede.dedegame.domain.model.mainGame.Game
import com.dede.dedegame.domain.model.mainGame.ListGame
import com.dede.dedegame.domain.usecase.GetGamesByType
import com.dede.dedegame.domain.usecase.GetStoryDetailAction
import com.dede.dedegame.presentation.home.game.GameDetailActivity
import com.dede.dedegame.presentation.home.game.GameDetailView
import com.dede.dedegame.presentation.home.news.NewsDetailActivity
import com.quangph.base.mvp.ICommand
import com.quangph.base.mvp.action.Action
import com.quangph.base.mvp.action.ActionException
import com.quangph.base.viewbinder.Layout
import com.quangph.jetpack.JetFragment

@Layout(R.layout.fragment_main_game)
class MainGameFragment : JetFragment<MainGameFragmentView>() {

    override fun onPresenterReady() {
        super.onPresenterReady()
        getGames()
    }

    override fun onExecuteCommand(command: ICommand) {
        super.onExecuteCommand(command)

        when (command) {
            is MainGameFragmentView.GotoGameDetailCmd -> {
                GameDetailActivity.launchScreen(activity, command.item.id)
            }
            is MainGameFragmentView.DownloadIOSGameCmd -> {
            }
            is MainGameFragmentView.DownloadIOSGameCmd -> {
            }
        }
    }

    private fun getGames() {
        showLoading()

        val rv = GetGamesByType.RV().apply {
            this.type = 1
            this.page = 1
        }

        mActionManager.executeAction(
            GetGamesByType(),
            rv,
            object : Action.SimpleActionCallback<ListGame>() {
                override fun onSuccess(responseValue: ListGame?) {
                    super.onSuccess(responseValue)
                    hideLoading()
                    responseValue?.let {
                        responseValue.games?.let { games ->
                            mvpView.fillGamesToGroup(games)
                        }
                    }
                }

                override fun onError(e: ActionException) {
                    super.onError(e)
                    hideLoading()
                }
            })
    }
}