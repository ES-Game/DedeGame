package com.dede.dedegame.presentation.home.fragments.main_game

import android.content.Context
import android.util.AttributeSet
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dede.dedegame.R
import com.dede.dedegame.domain.model.mainGame.Game
import com.dede.dedegame.presentation.common.CustomItemMainGameDecoration
import com.dede.dedegame.presentation.home.fragments.main_game.groups.GameTabGroupData
import com.dede.dedegame.presentation.home.fragments.main_game.groups.ItemViewType
import com.dede.dedegame.presentation.home.fragments.main_game.groups.ListGameGroupData
import com.dede.dedegame.presentation.home.fragments.main_game.groups.TopBannerGroupData
import com.quangph.base.mvp.ICommand
import com.quangph.base.mvp.mvpcomponent.view.BaseRelativeView
import com.quangph.base.view.recyclerview.adapter.group.GroupRclvAdapter

class MainGameFragmentView(context: Context?, attrs: AttributeSet?) :
    BaseRelativeView(context, attrs) {

    private val rvContent by lazy { findViewById<RecyclerView>(R.id.rvContent) }
    private val homeContentAdapter = GroupRclvAdapter()
    private val topBannerGroupData = TopBannerGroupData(null)
    private val homeTabGroupData = GameTabGroupData(null)
    private var listGameGroupData = ListGameGroupData(null)

    override fun onInitView() {
        super.onInitView()
        val layoutManager =
            GridLayoutManager(context, 2)
        rvContent.layoutManager = layoutManager
        rvContent.adapter = homeContentAdapter
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val itemViewType = homeContentAdapter.getItemViewType(position)
                return if (itemViewType == ItemViewType.LIST_GAME) {
                    1
                } else {
                    2
                }
            }

        }
        val space = resources.getDimensionPixelSize(R.dimen.margin_left_right_layout)
        val decoration = CustomItemMainGameDecoration(space)
        rvContent.addItemDecoration(decoration)

        homeContentAdapter.addGroup(topBannerGroupData)
        homeContentAdapter.addGroup(homeTabGroupData)
        homeTabGroupData.show()

        listGameGroupData.onClickGameListener = object : ListGameGroupData.OnClickGameListener {
            override fun onClickGameItem(item: Game) {
                mPresenter.executeCommand(GotoGameDetailCmd(item))
            }

            override fun onClickIOSGame(item: Game) {
                mPresenter.executeCommand(DownloadIOSGameCmd(item))
            }

            override fun onClickAndroidGame(item: Game) {
                mPresenter.executeCommand(DownloadAndroidGameCmd(item))
            }

        }
        homeContentAdapter.addGroup(listGameGroupData)
    }

    fun fillGamesToGroup(games: List<Game>) {
        listGameGroupData.reset(games)
        listGameGroupData.show()
    }

    class GotoGameDetailCmd(val item: Game) : ICommand
    class DownloadIOSGameCmd(val item: Game) : ICommand
    class DownloadAndroidGameCmd(val item: Game) : ICommand
}


