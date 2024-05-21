package com.dede.dedegame.presentation.home.game

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import com.dede.dedegame.R
import com.dede.dedegame.domain.model.mainGame.gameDetail.GameDetail
import com.dede.dedegame.domain.usecase.GetGameDetailAction
import com.quangph.base.mvp.ICommand
import com.quangph.base.mvp.action.Action
import com.quangph.base.mvp.action.ActionException
import com.quangph.base.viewbinder.Layout
import com.quangph.jetpack.JetActivity


@Layout(R.layout.activity_game_detail)
class GameDetailActivity : JetActivity<GameDetailView>() {

    companion object {
        fun launchScreen(
            context: Context?,
            gameId: Int?
        ) {
            val intent = Intent(context, GameDetailActivity::class.java)
            intent.putExtra("gameId", gameId)
            context?.startActivity(intent)
        }
    }

    override fun onPresenterReady() {
        super.onPresenterReady()
        val gameId = intent.getIntExtra("gameId", -1)
        getGameDetailById(gameId)
    }

    override fun onExecuteCommand(command: ICommand) {
        super.onExecuteCommand(command)
        when (command) {
            is GameDetailView.OnBackCmd -> {
                onBackPressedDispatcher.onBackPressed()
            }

            is GameDetailView.GotoOtherGameDetailCmd -> {
                launchScreen(this@GameDetailActivity, command.item.id)
                finish()
            }

            is GameDetailView.DownloadIOSGameCmd -> {
                if (command.item.statusOpen == 1) {
                    val webpage = Uri.parse(command.item.linkIos)
                    val intent = Intent(Intent.ACTION_VIEW, webpage)
                    if (intent.resolveActivity(packageManager) != null) {
                        startActivity(intent)
                    } else {
                        Toast.makeText(
                            this@GameDetailActivity,
                            "No browser is installed!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(this@GameDetailActivity, "Sắp ra mắt", Toast.LENGTH_SHORT).show()
                }
            }

            is GameDetailView.DownloadAndroidGameCmd -> {
                if (command.item.statusOpen == 1) {
                    val webpage = Uri.parse(command.item.linkAndroid)
                    val intent = Intent(Intent.ACTION_VIEW, webpage)
                    if (intent.resolveActivity(packageManager) != null) {
                        startActivity(intent)
                    } else {
                        Toast.makeText(
                            this@GameDetailActivity,
                            "No browser is installed!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(this@GameDetailActivity, "Sắp ra mắt", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun getGameDetailById(id: Int) {
        showLoading()

        val rv = GetGameDetailAction.RV().apply {
            this.gameId = id
        }

        mActionManager.executeAction(
            GetGameDetailAction(),
            rv,
            object : Action.SimpleActionCallback<GameDetail>() {
                override fun onSuccess(responseValue: GameDetail?) {
                    super.onSuccess(responseValue)
                    hideLoading()
                    responseValue?.let { responseValue ->
                        responseValue.game?.title?.let {
                            mvpView.setupTitleToolbar(it)
                        }
                        mvpView.fillGameDetailToGroup(responseValue)
                    }
                }

                override fun onError(e: ActionException) {
                    super.onError(e)
                    hideLoading()
                    Toast.makeText(this@GameDetailActivity, e.message, Toast.LENGTH_SHORT).show()
                }
            })
    }
}