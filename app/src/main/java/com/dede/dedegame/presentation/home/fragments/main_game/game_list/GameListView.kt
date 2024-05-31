package com.dede.dedegame.presentation.home.fragments.main_game.game_list

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dede.dedegame.R
import com.dede.dedegame.domain.model.mainGame.Game
import com.dede.dedegame.domain.model.mainGame.GameType
import com.dede.dedegame.presentation.common.DimensUtil
import com.dede.dedegame.presentation.common.GridSpacingItemDecoration
import com.dede.dedegame.presentation.home.fragments.main_game.game_list.groups.GameListViewType
import com.dede.dedegame.presentation.widget.EndlessRecyclerViewScrollListener
import com.quangph.base.mvp.ICommand
import com.quangph.base.mvp.mvpcomponent.view.BaseConstraintView

class GameListView(context: Context?, attrs: AttributeSet?) : BaseConstraintView(context, attrs) {

    private val rcvInfo by lazy { findViewById<RecyclerView>(R.id.rcvInfo) }
    private val containerBack by lazy { findViewById<View>(R.id.containerBack) }
    private val txtStartTitle by lazy { findViewById<TextView>(R.id.txtStartTitle) }
    private var gameListAdapter = GameListAdapter(false)
    private val layoutManager = GridLayoutManager(context, 2)
    override fun onInitView() {
        super.onInitView()
        setupToolbar()

        rcvInfo.adapter = gameListAdapter
        gameListAdapter.setOnClickListener(object : GameListAdapter.OnClickListener {
            override fun onClickGameItem(item: Game) {
                mPresenter.executeCommand(GotoGameDetailCmd(item))
            }
        })
        rcvInfo.layoutManager = layoutManager
        rcvInfo.addItemDecoration(GridSpacingItemDecoration(2, DimensUtil.dpToPx(16), true))
        rcvInfo.addOnScrollListener(object : EndlessRecyclerViewScrollListener(layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                mPresenter.executeCommand(LoadMoreCmd())
            }
        })
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val itemViewType = gameListAdapter.getItemViewType(position)
                return if (itemViewType == GameListViewType.ITEM_LOAD_MORE) {
                    3
                } else {
                    1
                }
            }

        }
    }

    fun setupTitleToolbar(gameType: GameType) {
        when (gameType) {
            GameType.COMING -> {
                txtStartTitle.text = context.getString(R.string.title_coming_game)
            }

            GameType.OPEN -> {
                txtStartTitle.text = context.getString(R.string.title_open_game)
            }
        }
    }

    fun fillGamesToAdapter(data: List<Game>) {
        gameListAdapter.setListGame(data)
    }

    fun loadMore(data: List<Game>, hasLoadMore: Boolean) {
        gameListAdapter.setLoadMore(hasLoadMore)
        if (hasLoadMore) {
            gameListAdapter.addItemsAndNotify(data)
        } else {
            val viewHolder =
                rcvInfo.findViewHolderForAdapterPosition(gameListAdapter.getEndPosListGame())
            if (viewHolder != null && viewHolder is GameListAdapter.LoadingVH) {
                viewHolder.itemView.visibility = View.GONE
            }
        }
    }

    private fun setupToolbar() {
        containerBack.setOnClickListener {
            mPresenter.executeCommand(OnBackCmd())
        }
    }

    class OnBackCmd() : ICommand
    class GotoGameDetailCmd(val game: Game) : ICommand

    class LoadMoreCmd : ICommand
}


