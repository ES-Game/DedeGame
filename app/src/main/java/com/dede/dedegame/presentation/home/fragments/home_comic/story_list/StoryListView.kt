package com.dede.dedegame.presentation.home.fragments.home_comic.story_list

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dede.dedegame.R
import com.dede.dedegame.domain.model.StoryDetail
import com.dede.dedegame.presentation.common.DimensUtil
import com.dede.dedegame.presentation.common.GridSpacingItemDecoration
import com.dede.dedegame.presentation.widget.EndlessRecyclerViewScrollListener
import com.quangph.base.mvp.ICommand
import com.quangph.base.mvp.mvpcomponent.view.BaseConstraintView

class StoryListView(context: Context?, attrs: AttributeSet?) : BaseConstraintView(context, attrs) {

    private val rcvInfo by lazy { findViewById<RecyclerView>(R.id.rcvInfo) }
    private val containerBack by lazy { findViewById<View>(R.id.containerBack) }
    private val txtStartTitle by lazy { findViewById<TextView>(R.id.txtStartTitle) }
    private var storyListAdapter = StoryListAdapter(false)
    private val layoutManager = GridLayoutManager(context, 3)
    override fun onInitView() {
        super.onInitView()
        setupToolbar()

        rcvInfo.adapter = storyListAdapter
        storyListAdapter.setOnClickListener(object : StoryListAdapter.OnClickListener {
            override fun onClickGameItem(item: StoryDetail) {
                mPresenter.executeCommand(GotoStoryCoverCmd(item))
            }
        })
        rcvInfo.layoutManager = layoutManager
        rcvInfo.addItemDecoration(GridSpacingItemDecoration(3, DimensUtil.dpToPx(16), true))
        rcvInfo.addOnScrollListener(object : EndlessRecyclerViewScrollListener(layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                mPresenter.executeCommand(LoadMoreCmd())
            }
        })
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val itemViewType = storyListAdapter.getItemViewType(position)
                return if (itemViewType == StoryListViewType.ITEM_LOAD_MORE) {
                    3
                } else {
                    1
                }
            }

        }
    }

    private fun setupToolbar() {
        containerBack.setOnClickListener {
            mPresenter.executeCommand(OnBackCmd())
        }
    }

    fun setupTitleToolbar(title: String) {
        txtStartTitle.text = title
    }

    fun fillStoryToAdapter(data: List<StoryDetail>) {
        storyListAdapter.setListStory(data)
    }

    fun loadMore(data: List<StoryDetail>, hasLoadMore: Boolean) {
        storyListAdapter.setLoadMore(hasLoadMore)
        if (hasLoadMore) {
            storyListAdapter.addItemsAndNotify(data)
        } else {
            val viewHolder =
                rcvInfo.findViewHolderForAdapterPosition(storyListAdapter.getEndPosListStory())
            if (viewHolder != null && viewHolder is StoryListAdapter.LoadingVH) {
                viewHolder.itemView.visibility = View.GONE
            }
        }
    }

    class OnBackCmd : ICommand
    class GotoStoryCoverCmd(val storyDetail: StoryDetail) : ICommand
    class LoadMoreCmd : ICommand
}


