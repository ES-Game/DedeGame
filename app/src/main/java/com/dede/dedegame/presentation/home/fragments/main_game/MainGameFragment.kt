package com.dede.dedegame.presentation.home.fragments.main_game

import android.widget.Toast
import com.dede.dedegame.R
import com.dede.dedegame.domain.model.home.Home
import com.dede.dedegame.domain.model.mainGame.ComingTempGame
import com.dede.dedegame.domain.model.mainGame.ListGame
import com.dede.dedegame.domain.model.mainGame.OpenTempGame
import com.dede.dedegame.domain.usecase.GetGamesByType
import com.dede.dedegame.domain.usecase.GetHomeDataAction
import com.dede.dedegame.presentation.home.game.GameDetailActivity
import com.quangph.base.mvp.ICommand
import com.quangph.base.mvp.action.Action
import com.quangph.base.mvp.action.ActionException
import com.quangph.base.mvp.action.scheduler.AsyncTaskScheduler
import com.quangph.base.viewbinder.Layout
import com.quangph.jetpack.JetFragment

@Layout(R.layout.fragment_main_game)
class MainGameFragment : JetFragment<MainGameFragmentView>() {

    override fun onPresenterReady() {
        super.onPresenterReady()
//        getGames()
        getHomeData()
    }

    override fun onExecuteCommand(command: ICommand) {
        super.onExecuteCommand(command)

        when (command) {
            is MainGameFragmentView.GotoGameDetailCmd -> {
                GameDetailActivity.launchScreen(activity, command.id)
            }
        }
    }

    private fun getHomeData() {
        showLoading()
        val callback = object  : Action.SimpleActionCallback<Home>() {
            override fun onSuccess(responseValue: Home?) {
                super.onSuccess(responseValue)
                hideLoading()

                if (responseValue != null) {
                    responseValue.sliders?.let {
                        mvpView.showTopBanner(it)
                    }
                    responseValue.openedGames?.let {
                        val openTempGame = OpenTempGame()
                        openTempGame.title = "Game đã ra mắt"
                        openTempGame.games = it
                        mvpView.fillOpenGamesToGroup(openTempGame)
                    }
                    responseValue.comingGames?.let {
                        val comingTempGame = ComingTempGame()
                        comingTempGame.title = "Game sắp ra mắt"
                        comingTempGame.games = it
                        mvpView.fillComingGamesToGroup(comingTempGame)
                    }
                }
            }

            override fun onError(e: ActionException) {
                super.onError(e)
                hideLoading()
                Toast.makeText(activity, e.message, Toast.LENGTH_SHORT).show()
            }
        }

        val rv = GetHomeDataAction.RV()
        actionManager.executeAction(
            GetHomeDataAction(),
            rv,
            callback,
            AsyncTaskScheduler()
        )
    }

//    private fun getGames() {
//        showLoading()
//
//        val rv = GetGamesByType.RV().apply {
//            this.type = 1
//            this.page = 1
//        }
//
//        mActionManager.executeAction(
//            GetGamesByType(),
//            rv,
//            object : Action.SimpleActionCallback<ListGame>() {
//                override fun onSuccess(responseValue: ListGame?) {
//                    super.onSuccess(responseValue)
//                    hideLoading()
//                    responseValue?.let {
//                        responseValue.games?.let { games ->
//                            mvpView.fillGamesToGroup(games)
//                        }
//                    }
//                }
//
//                override fun onError(e: ActionException) {
//                    super.onError(e)
//                    hideLoading()
//                    Toast.makeText(activity, e.message, Toast.LENGTH_SHORT).show()
//                }
//            })
//    }
}