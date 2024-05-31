package com.dede.dedegame.presentation.home.fragments.main_game

import android.os.Bundle
import android.widget.Toast
import com.dede.dedegame.R
import com.dede.dedegame.domain.model.home.Home
import com.dede.dedegame.domain.model.home.Slider
import com.dede.dedegame.domain.model.mainGame.GameListModel
import com.dede.dedegame.domain.model.mainGame.GameType
import com.dede.dedegame.domain.usecase.GetHomeDataAction
import com.dede.dedegame.presentation.common.tracker.DedeFirebaseTracker
import com.dede.dedegame.presentation.common.tracker.DedeFirebaseTrackerModel
import com.dede.dedegame.presentation.home.fragments.home_comic.story_list.StoryListActivity
import com.dede.dedegame.presentation.home.fragments.main_game.game_list.GameListActivity
import com.dede.dedegame.presentation.home.game.GameDetailActivity
import com.google.gson.Gson
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
                trackingTapEventMainGame(
                    EVENT_TAP_GAME_ITEM,
                    PARAM_GAME_STATUS_OPEN,
                    command.status,
                    PARAM_GAME,
                    command.id
                )
                GameDetailActivity.launchScreen(activity, command.id)
            }

            is MainGameFragmentView.GotoGameListCmd -> {
                GameListActivity.launchScreen(activity, Gson().toJson(command.gameType))
            }

            is MainGameFragmentView.GotoScreenByTypeCmd -> {
                when (command.item.type) {
                    Slider.Type.COMIC_CATEGORY -> {
                        trackingTapEventMainGame(
                            EVENT_TAP_SLIDER_ITEM,
                            PARAM_SLIDER_COMIC_CATEGORY, command.item.sid
                        )
                        StoryListActivity.launchScreen(activity, command.item.sid)
                    }

                    else -> {
                        trackingTapEventMainGame(
                            EVENT_TAP_SLIDER_ITEM,
                            PARAM_SLIDER_GAME_DETAIL, command.item.sid
                        )
                        GameDetailActivity.launchScreen(activity, command.item.sid)
                    }
                }
            }
        }
    }

    private fun getHomeData() {
        showLoading()
        val callback = object : Action.SimpleActionCallback<Home>() {
            override fun onSuccess(responseValue: Home?) {
                super.onSuccess(responseValue)
                hideLoading()

                if (responseValue != null) {
                    responseValue.sliders?.let {
                        mvpView.showTopBanner(it)
                    }
                    val listContainerGame = arrayListOf<GameListModel>()
                    responseValue.openedGames?.let {
                        val openGame = GameListModel()
                        openGame.title = getString(R.string.title_open_game)
                        openGame.type = GameType.OPEN
                        openGame.games = it
                        listContainerGame.add(openGame)
                    }
                    responseValue.comingGames?.let {
                        val comingGame = GameListModel()
                        comingGame.title = getString(R.string.title_coming_game)
                        comingGame.type = GameType.COMING
                        comingGame.games = it
                        listContainerGame.add(comingGame)
                    }
                    if (listContainerGame.isNotEmpty()) {
                        listContainerGame.forEach { gameContainer ->
                            mView.fillContainerGamesToGroup(gameContainer)
                        }
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

    private fun trackingTapEventMainGame(
        eventName: String,
        param: String,
        paramValue: Any?,
        param2: String,
        paramValue2: Any?
    ) {
        val fbModel = FirebaseLoginModel().apply {
            this.eventName = eventName
            this.param = param
            this.paramValue = paramValue.toString()
            this.param2 = param2
            this.paramValue2 = paramValue2.toString()
        }
        DedeFirebaseTracker.track(fbModel)
    }

    private fun trackingTapEventMainGame(eventName: String, param: String, paramValue: Any?) {
        val fbModel = FirebaseLoginModel().apply {
            this.eventName = eventName
            this.param = param
            this.paramValue = paramValue.toString()
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
        const val EVENT_TAP_SLIDER_ITEM = "event_tap_slider_item"
        const val PARAM_SLIDER_GAME_DETAIL = "slider_game_detail_sid"
        const val PARAM_SLIDER_COMIC_CATEGORY = "slider_comic_category_sid"
    }
}