package com.dede.dedegame.presentation.home.game

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import com.dede.dedegame.R
import com.dede.dedegame.domain.model.mainGame.gameDetail.GameDetail
import com.dede.dedegame.domain.usecase.GetGameDetailAction
import com.dede.dedegame.presentation.common.tracker.DedeFirebaseTracker
import com.dede.dedegame.presentation.common.tracker.DedeFirebaseTrackerModel
import com.quangph.base.mvp.ICommand
import com.quangph.base.mvp.action.Action
import com.quangph.base.mvp.action.ActionException
import com.quangph.base.viewbinder.Layout
import com.quangph.jetpack.JetActivity


@Layout(R.layout.activity_game_detail)
class GameDetailActivity : JetActivity<GameDetailView>() {

    companion object {

        const val EVENT_ON_GAME_DETAIL = "event_on_game_detail"
        const val EVENT_TAP_OTHER_GAME_ITEM = "event_tap_other_game_item"
        const val EVENT_TAP_DOWNLOAD_IOS = "event_tap_download_ios"
        const val EVENT_TAP_DOWNLOAD_ANDROID = "event_tap_download_android"
        const val PARAM_GAME = "game_id"
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
        trackingOnGameDetailScreen()
    }

    override fun onExecuteCommand(command: ICommand) {
        super.onExecuteCommand(command)
        when (command) {
            is GameDetailView.OnBackCmd -> {
                onBackPressedDispatcher.onBackPressed()
            }

            is GameDetailView.GotoOtherGameDetailCmd -> {
                trackingTapEventGameDetail(EVENT_TAP_OTHER_GAME_ITEM, PARAM_GAME, command.item.id)
                launchScreen(this@GameDetailActivity, command.item.id)
            }

            is GameDetailView.DownloadIOSGameCmd -> {
                trackingTapEventGameDetail(EVENT_TAP_DOWNLOAD_IOS, PARAM_GAME, command.item.id)
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
                trackingTapEventGameDetail(EVENT_TAP_DOWNLOAD_ANDROID, PARAM_GAME, command.item.id)
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

    private fun trackingOnGameDetailScreen() {
        val fbModel = FirebaseLoginModel().apply {
            this.eventName = EVENT_ON_GAME_DETAIL
            this.param = "on_screen"
            this.paramValue = "on_screen"
        }
        DedeFirebaseTracker.track(fbModel)
    }

    private fun trackingTapEventGameDetail(eventName: String, param: String, paramValue: Any?) {
        val fbModel = FirebaseLoginModel().apply {
            this.eventName = eventName
            this.param = param
            this.paramValue = paramValue.toString()
        }
        DedeFirebaseTracker.track(fbModel)
    }

    inner class FirebaseLoginModel : DedeFirebaseTrackerModel() {
        override var screenName: String? = this@GameDetailActivity.javaClass.simpleName
        var param: String = ""
        var paramValue: String = ""

        override fun createParams(bundle: Bundle) {
            super.createParams(bundle)
            bundle.putString(param, paramValue)
        }
    }

}