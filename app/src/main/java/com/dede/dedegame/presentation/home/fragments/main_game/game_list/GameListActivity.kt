package com.dede.dedegame.presentation.home.fragments.main_game.game_list

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.dede.dedegame.R
import com.dede.dedegame.domain.model.DataPage
import com.dede.dedegame.domain.model.mainGame.Game
import com.dede.dedegame.domain.model.mainGame.GameType
import com.dede.dedegame.domain.usecase.GetGamesByType
import com.dede.dedegame.presentation.common.tracker.DedeFirebaseTracker
import com.dede.dedegame.presentation.common.tracker.DedeFirebaseTrackerModel
import com.dede.dedegame.presentation.home.game.GameDetailActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.quangph.base.mvp.ICommand
import com.quangph.base.mvp.action.Action
import com.quangph.base.mvp.action.ActionException
import com.quangph.base.viewbinder.Layout
import com.quangph.jetpack.JetActivity

@Layout(R.layout.activity_game_list)
class GameListActivity : JetActivity<GameListView>() {

    var currentPage = 0
    var gameType: GameType? = null

    companion object {
        const val EVENT_ON_GAME_LIST = "event_on_game_list"
        const val PARAM_GAME = "game_id"
        const val EVENT_TAP_GAME_ITEM = "event_tap_game_item"
        fun launchScreen(
            context: Context?,
            gameType: String
        ) {
            val intent = Intent(context, GameListActivity::class.java)
            intent.putExtra("gameType", gameType)
            context?.startActivity(intent)
        }
    }

    override fun onPresenterReady() {
        super.onPresenterReady()
        trackingOnGameListScreen()
        gameType = Gson().fromJson(
            intent.getStringExtra("gameType"),
            object : TypeToken<GameType>() {}.type
        )
        gameType?.let { mvpView.setupTitleToolbar(it) }
        gameType?.let { getGamesByType(it, true) }
    }

    override fun onExecuteCommand(command: ICommand) {
        super.onExecuteCommand(command)
        when (command) {
            is GameListView.OnBackCmd -> {
                onBackPressedDispatcher.onBackPressed()
            }

            is GameListView.GotoGameDetailCmd -> {
                trackingTapEventGameList(EVENT_TAP_GAME_ITEM, PARAM_GAME, command.game.id)
                GameDetailActivity.launchScreen(this, command.game.id)
            }

            is GameListView.LoadMoreCmd -> {
                gameType?.let { getGamesByType(it, false) }
            }
        }
    }

    private fun getGamesByType(gameType: GameType, isInitialLoad: Boolean) {
        if (isInitialLoad) {
            showLoading()
            currentPage = 1
        } else {
            currentPage += 1
        }

        val rv = GetGamesByType.RV().apply {
            this.type = gameType
            this.page = currentPage
        }
        mActionManager.executeAction(
            GetGamesByType(),
            rv,
            object : Action.SimpleActionCallback<DataPage<Game>>() {
                override fun onSuccess(responseValue: DataPage<Game>?) {
                    super.onSuccess(responseValue)
                    if (isInitialLoad) {
                        hideLoading()
                        responseValue?.dataList?.let {
                            mvpView.fillGamesToAdapter(it)
                        }
                    } else {
                        responseValue?.dataList?.let {
                            mvpView.loadMore(it, responseValue.hasNextPage)
                        }
                    }
                }

                override fun onError(e: ActionException) {
                    super.onError(e)
                    hideLoading()
                    Toast.makeText(this@GameListActivity, e.message, Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun trackingOnGameListScreen() {
        val fbModel = FirebaseGameListModel().apply {
            this.eventName = EVENT_ON_GAME_LIST
            this.param = "on_screen"
            this.paramValue = "on_screen"
        }
        DedeFirebaseTracker.track(fbModel)
    }

    private fun trackingTapEventGameList(eventName: String, param: String, paramValue: Any?) {
        val fbModel = FirebaseGameListModel().apply {
            this.eventName = eventName
            this.param = param
            this.paramValue = paramValue.toString()
        }
        DedeFirebaseTracker.track(fbModel)
    }

    inner class FirebaseGameListModel : DedeFirebaseTrackerModel() {
        override var screenName: String? = this@GameListActivity.javaClass.simpleName
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

}