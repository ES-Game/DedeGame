package com.dede.dedegame.presentation.home.game

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dede.dedegame.R
import com.dede.dedegame.domain.model.mainGame.gameDetail.Game
import com.dede.dedegame.domain.model.mainGame.gameDetail.GameDetail
import com.dede.dedegame.domain.model.mainGame.gameDetail.OtherGame
import com.dede.dedegame.presentation.home.game.groups.GameDetailGroupData
import com.quangph.base.mvp.ICommand
import com.quangph.base.mvp.mvpcomponent.view.BaseConstraintView
import com.quangph.base.view.recyclerview.adapter.group.GroupRclvAdapter

class GameDetailView(context: Context?, attrs: AttributeSet?) : BaseConstraintView(context, attrs) {
    private val rcvInfo by lazy { findViewById<RecyclerView>(R.id.rcvInfo) }
    private val containerBack by lazy { findViewById<View>(R.id.containerBack) }
    private val txtStartTitle by lazy { findViewById<TextView>(R.id.txtStartTitle) }
    private val txtCenterTitle by lazy { findViewById<TextView>(R.id.txtCenterTitle) }


    private val gameDetailAdapter = GroupRclvAdapter()
    private var gameDetailGroupData: GameDetailGroupData = GameDetailGroupData(null)

    override fun onInitView() {
        super.onInitView()
        setupToolbar()
        rcvInfo?.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rcvInfo?.adapter = gameDetailAdapter
    }

    private fun setupToolbar() {
        txtStartTitle.text = "Game Detail"
        containerBack.setOnClickListener {
            mPresenter.executeCommand(OnBackCmd())
        }
    }

    fun setupTitleToolbar(title: String) {
        txtStartTitle.text = title
    }

    fun fillGameDetailToGroup(gameDetail: GameDetail) {
        gameDetailGroupData = GameDetailGroupData(gameDetail)
        gameDetailGroupData.onEvenGameDetailListener =
            object : GameDetailGroupData.OnEvenGameDetailListener {
                override fun onClickOtherGameItem(item: OtherGame) {
                    mPresenter.executeCommand(GotoOtherGameDetailCmd(item))
                }

                override fun onClickAndroidDownload(item: Game) {
                    mPresenter.executeCommand(DownloadAndroidGameCmd(item))
                }

                override fun onClickIOSDownload(item: Game) {
                    mPresenter.executeCommand(DownloadIOSGameCmd(item))
                }

            }
        gameDetailAdapter.addGroup(gameDetailGroupData)
        gameDetailGroupData.reset(gameDetail)
        gameDetailGroupData.show()
    }

    class OnBackCmd() : ICommand

    class GotoOtherGameDetailCmd(val item: OtherGame) : ICommand
    class DownloadIOSGameCmd(val item: Game) : ICommand
    class DownloadAndroidGameCmd(val item: Game) : ICommand
}


