package com.dede.dedegame.presentation.home.fragments.main_game

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dede.dedegame.R
import com.dede.dedegame.domain.model.home.Slider
import com.dede.dedegame.domain.model.mainGame.ComingTempGame
import com.dede.dedegame.domain.model.mainGame.Game
import com.dede.dedegame.domain.model.mainGame.OpenTempGame
import com.dede.dedegame.presentation.common.CustomItemMainGameDecoration
import com.dede.dedegame.presentation.home.fragments.main_game.groups.ComingGamesGroupData
import com.dede.dedegame.presentation.home.fragments.main_game.groups.ItemViewType
import com.dede.dedegame.presentation.home.fragments.main_game.groups.ListGameGroupData
import com.dede.dedegame.presentation.home.fragments.main_game.groups.OpenGamesGroupData
import com.dede.dedegame.presentation.home.fragments.main_game.groups.TopBannerGroupData
import com.quangph.base.mvp.ICommand
import com.quangph.base.mvp.mvpcomponent.view.BaseRelativeView
import com.quangph.base.view.recyclerview.adapter.group.GroupRclvAdapter

class MainGameFragmentView(context: Context?, attrs: AttributeSet?) :
    BaseRelativeView(context, attrs) {

    private val rvContent by lazy { findViewById<RecyclerView>(R.id.rvContent) }
    private val homeContentAdapter = GroupRclvAdapter()
    private val topBannerGroupData = TopBannerGroupData(null)
    private var listGameGroupData = ListGameGroupData(null)
    val comingGamesGroupData = ComingGamesGroupData(null)
    val openGamesGroupData = OpenGamesGroupData(null)
    override fun onInitView() {
        super.onInitView()
        val layoutManager =
            GridLayoutManager(context, 2)
        rvContent.layoutManager = layoutManager
        rvContent.adapter = homeContentAdapter
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val itemViewType = homeContentAdapter.getItemViewType(position)
                return if (itemViewType == ItemViewType.ITEM_OPEN_GAME || itemViewType == ItemViewType.ITEM_COMING_GAME) {
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

//        listGameGroupData.onClickGameListener = object : ListGameGroupData.OnClickGameListener {
//            override fun onClickGameItem(id: Int) {
//                mPresenter.executeCommand(GotoGameDetailCmd(id))
//            }
//        }
//        homeContentAdapter.addGroup(listGameGroupData)
    }

    fun showTopBanner(data: List<Slider>) {
        topBannerGroupData.reset(data)
        topBannerGroupData.show()
    }

    fun fillOpenGamesToGroup(openTempGame: OpenTempGame){
        homeContentAdapter.addGroup(openGamesGroupData)
        openGamesGroupData.reset(openTempGame)
        openGamesGroupData.show()
        openGamesGroupData.onClickStoryItem = object : OpenGamesGroupData.OnClickStoryItem {
            override fun onClickStoryItem(id: Int) {
                mPresenter.executeCommand(GotoGameDetailCmd(id))
            }
        }
    }
    fun fillComingGamesToGroup(comingGame: ComingTempGame){
        homeContentAdapter.addGroup(comingGamesGroupData)
        comingGamesGroupData.reset(comingGame)
        comingGamesGroupData.show()
        comingGamesGroupData.onClickStoryItem = object : ComingGamesGroupData.OnClickStoryItem {
            override fun onClickStoryItem(id: Int) {
                mPresenter.executeCommand(GotoGameDetailCmd(id))
            }
        }
    }

    fun fillGamesToGroup(games: List<Game>) {
        listGameGroupData.reset(games)
        listGameGroupData.show()
    }

    class GotoGameDetailCmd(val id: Int) : ICommand
}


