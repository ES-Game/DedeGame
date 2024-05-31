package com.dede.dedegame.presentation.home.fragments.main_game

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dede.dedegame.R
import com.dede.dedegame.domain.model.home.Slider
import com.dede.dedegame.domain.model.mainGame.GameListModel
import com.dede.dedegame.domain.model.mainGame.GameType
import com.dede.dedegame.presentation.common.CustomItemMainGameDecoration
import com.dede.dedegame.presentation.home.fragments.home.HomeFragmentView
import com.dede.dedegame.presentation.home.fragments.main_game.groups.GameListGroupData
import com.dede.dedegame.presentation.home.fragments.main_game.groups.ItemViewType
import com.dede.dedegame.presentation.home.fragments.main_game.groups.TopBannerGroupData
import com.quangph.base.mvp.ICommand
import com.quangph.base.mvp.mvpcomponent.view.BaseRelativeView
import com.quangph.base.view.recyclerview.adapter.group.GroupRclvAdapter

class MainGameFragmentView(context: Context?, attrs: AttributeSet?) :
    BaseRelativeView(context, attrs) {

    private val rvContent by lazy { findViewById<RecyclerView>(R.id.rvContent) }
    private val homeContentAdapter = GroupRclvAdapter()
    private val topBannerGroupData = TopBannerGroupData(null)
    private lateinit var layoutManager: GridLayoutManager
    override fun onInitView() {
        super.onInitView()
        layoutManager =
            GridLayoutManager(context, 2)
        rvContent.layoutManager = layoutManager
        rvContent.adapter = homeContentAdapter
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val itemViewType = homeContentAdapter.getItemViewType(position)
                return if (itemViewType == ItemViewType.ITEM_GAME) {
                    1
                } else {
                    2
                }
            }

        }
        val space = resources.getDimensionPixelSize(R.dimen.margin_left_right_layout)
        val decoration = CustomItemMainGameDecoration(space)
        rvContent.addItemDecoration(decoration)

        topBannerGroupData.onEvenSliderListener = object : TopBannerGroupData.OnEvenSliderListener{
            override fun onClickSliderItem(item: Slider) {
                mPresenter.executeCommand(GotoScreenByTypeCmd(item))
            }
        }
        homeContentAdapter.addGroup(topBannerGroupData)
    }

    fun showTopBanner(data: List<Slider>) {
        topBannerGroupData.reset(data)
        topBannerGroupData.show()
    }

    fun fillContainerGamesToGroup(gameListModel: GameListModel) {
        val gameListGroupData = GameListGroupData(null)
        homeContentAdapter.addGroup(gameListGroupData)
        gameListGroupData.reset(gameListModel)
        gameListGroupData.show()
        gameListGroupData.onClickStoryItem = object : GameListGroupData.OnClickStoryItem {
            override fun onClickViewMore(gameType: GameType) {
                mPresenter.executeCommand(GotoGameListCmd(gameType))
            }

            override fun onClickStoryItem(id: Int, status: Int) {
                mPresenter.executeCommand(GotoGameDetailCmd(id, status))
            }
        }
    }

    private var scrollPosition = 0

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        scrollPosition = layoutManager.findFirstVisibleItemPosition()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        layoutManager.scrollToPosition(scrollPosition)
    }

    class GotoGameDetailCmd(val id: Int, val status: Int) : ICommand
    class GotoGameListCmd(val gameType: GameType) : ICommand
    class GotoScreenByTypeCmd(val item: Slider) : ICommand
}


