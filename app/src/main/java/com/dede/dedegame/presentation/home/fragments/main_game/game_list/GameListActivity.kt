package com.dede.dedegame.presentation.home.fragments.main_game.game_list

import android.content.Context
import android.content.Intent
import com.dede.dedegame.R
import com.dede.dedegame.domain.model.DataPage
import com.dede.dedegame.domain.model.mainGame.Game
import com.dede.dedegame.domain.model.mainGame.GameType
import com.dede.dedegame.domain.usecase.GetGamesByType
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
                }
            })
    }

}