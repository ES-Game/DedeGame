package com.dede.dedegame.presentation.home.fragments.home

import android.content.Context
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dede.dedegame.R
import com.dede.dedegame.domain.model.StoryDetail
import com.dede.dedegame.domain.model.home.Article
import com.dede.dedegame.domain.model.home.Slider
import com.dede.dedegame.presentation.common.CustomDividerItemDecoration
import com.dede.dedegame.presentation.common.DimensUtil
import com.dede.dedegame.presentation.home.fragments.home.groups.HomeTabGroupData
import com.dede.dedegame.presentation.home.fragments.home.groups.NewsGroupData
import com.dede.dedegame.presentation.home.fragments.home.groups.RankGroupData
import com.dede.dedegame.presentation.home.fragments.home.groups.TopBannerGroupData
import com.dede.dedegame.presentation.home.fragments.home.groups.TrendGroupData
import com.quangph.base.mvp.ICommand
import com.quangph.base.mvp.mvpcomponent.view.BaseRelativeView
import com.quangph.base.view.recyclerview.adapter.group.GroupRclvAdapter

class
HomeFragmentView(context: Context?, attrs: AttributeSet?) : BaseRelativeView(context, attrs) {

    private val rvContent by lazy { findViewById<RecyclerView>(R.id.rvContent) }
    private val homeContentAdapter = GroupRclvAdapter()
    private val topBannerGroupData = TopBannerGroupData(null)
    private val homeTabGroupData = HomeTabGroupData(null)

    private val newsGroupData = NewsGroupData(null)
    private val rankGroupData = RankGroupData(null)
    private val listTrendGroupData = TrendGroupData(null)

    override fun onInitView() {
        super.onInitView()

        val layoutManager = GridLayoutManager(context, 1)
//        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
//            override fun getSpanSize(position: Int): Int {
//                return if (position == 0 || position == 1 || position == 2) {
//                    2
//                } else {
//                    1
//                }
//            }
//
//        }
        rvContent.layoutManager = layoutManager
        rvContent.adapter = homeContentAdapter
        val positionsToHideDivider = intArrayOf(0)
        val marginStart = DimensUtil.dpToPx(16)
        val marginEnd = DimensUtil.dpToPx(16)
        val decoration = CustomDividerItemDecoration(
            ContextCompat.getColor(
                context,
                R.color.color_grey_divider_list
            ), DimensUtil.dpToPx(1), positionsToHideDivider, marginStart, marginEnd
        )
        rvContent.addItemDecoration(decoration)


        homeContentAdapter.addGroup(topBannerGroupData)
        homeContentAdapter.addGroup(homeTabGroupData)
        homeTabGroupData.show()

        newsGroupData.onClickNewsListener = object : NewsGroupData.OnClickNewsListener {
            override fun onClickNewsItem(item: Article) {
                mPresenter.executeCommand(GotoNewsDetailCmd(item))
            }
        }
        rankGroupData.onClickStoryItem = object : RankGroupData.OnClickStoryItem {
            override fun onClickStoryItem(item: StoryDetail) {
                mPresenter.executeCommand(GotoStoryDetailCmd(item))
            }
        }
        homeTabGroupData.onClickListener = object : HomeTabGroupData.OnClickItemListener {
            override fun onClickTab(position: Int) {
                mPresenter.executeCommand(GotoTabCmd(position))
            }
        }
    }

    fun showTopBanner(data: List<Slider>) {
        topBannerGroupData.reset(data)
        topBannerGroupData.show()
    }

    fun validateNewsGroup() {
        if (rankGroupData.isAttached) {
            homeContentAdapter.removeGroup(rankGroupData)
        }

        if (listTrendGroupData.isAttached) {
            homeContentAdapter.removeGroup(listTrendGroupData)
        }
        homeContentAdapter.addGroup(newsGroupData)
    }

    fun showNewsData(data: List<Article>) {
        newsGroupData.reset(data)
        newsGroupData.show()
        rvContent.scrollToPosition(0);
    }

    fun validateRankGroup() {
        if (newsGroupData.isAttached) {
            homeContentAdapter.removeGroup(newsGroupData)
        }

        if (listTrendGroupData.isAttached) {
            homeContentAdapter.removeGroup(listTrendGroupData)
        }
        homeContentAdapter.addGroup(rankGroupData)
    }

    fun showRankData(data: List<StoryDetail>) {
        rankGroupData.reset(data)
        rankGroupData.show()

    }

    fun validateTrendingGroup() {
        if (rankGroupData.isAttached) {
            homeContentAdapter.removeGroup(rankGroupData)
        }

        if (rankGroupData.isAttached) {
            homeContentAdapter.removeGroup(rankGroupData)
        }
        homeContentAdapter.addGroup(listTrendGroupData)
    }

    fun showTrendData(data: List<StoryDetail>) {
        listTrendGroupData.reset(data)
        listTrendGroupData.show()
    }

    class GotoNewsDetailCmd(val item: Article) : ICommand
    class GotoStoryDetailCmd(val item: StoryDetail) : ICommand
    class GotoTabCmd(val position: Int) : ICommand
}


