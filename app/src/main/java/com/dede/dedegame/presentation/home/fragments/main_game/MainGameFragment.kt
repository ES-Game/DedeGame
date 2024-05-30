package com.dede.dedegame.presentation.home.fragments.main_game

import android.os.Bundle
import android.widget.Toast
import com.dede.dedegame.R
import com.dede.dedegame.domain.model.home.Home
import com.dede.dedegame.domain.model.mainGame.ComingTempGame
import com.dede.dedegame.domain.model.mainGame.ListGame
import com.dede.dedegame.domain.model.mainGame.OpenTempGame
import com.dede.dedegame.domain.usecase.GetGamesByType
import com.dede.dedegame.domain.usecase.GetHomeDataAction
import com.dede.dedegame.presentation.common.tracker.DedeFirebaseTracker
import com.dede.dedegame.presentation.common.tracker.DedeFirebaseTrackerModel
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
        trackingOnMainGameScreen()
        getHomeData()
    }

    override fun onExecuteCommand(command: ICommand) {
        super.onExecuteCommand(command)

        when (command) {
            is MainGameFragmentView.GotoGameDetailCmd -> {
                trackingTapEventMainGame(EVENT_TAP_GAME_ITEM, PARAM_GAME_STATUS_OPEN, command.status, PARAM_GAME, command.id)
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

    private fun trackingOnMainGameScreen() {
        val fbModel = FirebaseLoginModel().apply {
            this.eventName = EVENT_ON_MAIN_GAME
            this.param = "on_screen"
            this.paramValue = "on_screen"
        }
        DedeFirebaseTracker.track(fbModel)
    }

    private fun trackingTapEventMainGame(eventName: String, param: String, paramValue: Any?, param2: String, paramValue2: Any?) {
        val fbModel = FirebaseLoginModel().apply {
            this.eventName = eventName
            this.param = param
            this.paramValue = paramValue.toString()
            this.param2 = param2
            this.paramValue2 = paramValue2.toString()
        }
        DedeFirebaseTracker.track(fbModel)
    }

    inner class FirebaseLoginModel : DedeFirebaseTrackerModel() {
        override var screenName: String? = this@MainGameFragment.javaClass.simpleName
        var param: String = ""
        var paramValue: String = ""
        var param2: String = ""
        var paramValue2: String = ""

        override fun createParams(bundle: Bundle) {
            super.createParams(bundle)
            bundle.putString(param, paramValue)
            bundle.putString(param2, paramValue2)
        }
    }

    companion object {
        const val EVENT_ON_MAIN_GAME = "event_on_main_game"
        const val EVENT_TAP_GAME_ITEM = "event_tap_game_item"
        const val PARAM_GAME_STATUS_OPEN = "status_open"
        const val PARAM_GAME = "game_id"
    }
}