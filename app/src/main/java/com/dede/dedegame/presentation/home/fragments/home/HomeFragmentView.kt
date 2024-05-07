package com.dede.dedegame.presentation.home.fragments.home

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.quangph.base.mvp.ICommand
import com.dede.dedegame.R
import com.dede.dedegame.presentation.home.fragments.home.groups.HomeTabGroupData

import com.dede.dedegame.presentation.home.fragments.main_game.groups.ListStoryGroupData

import com.dede.dedegame.presentation.home.fragments.main_game.groups.TopBannerGroupData
import com.quangph.base.mvp.mvpcomponent.view.BaseRelativeView
import com.quangph.base.view.recyclerview.adapter.group.GroupRclvAdapter
import com.quangph.dedegame.domain.model.Story
import com.quangph.dedegame.domain.model.StoryDetail

class HomeFragmentView(context: Context?, attrs: AttributeSet?) : BaseRelativeView(context, attrs) {

    private var rvContent: RecyclerView? = null

    private val homeContentAdapter = GroupRclvAdapter()
    private val topBannerGroupData = TopBannerGroupData(null)
    private val homeTabGroupData = HomeTabGroupData(null)
    private val listStoryGroupData = ListStoryGroupData(null)

    override fun onInitView() {
        super.onInitView()

        rvContent = findViewById<RecyclerView>(R.id.rvContent)
        rvContent!!.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL ,false)
        rvContent!!.adapter = homeContentAdapter
        homeContentAdapter.addGroup(topBannerGroupData)
        homeContentAdapter.addGroup(homeTabGroupData)
        homeTabGroupData.show()
        homeContentAdapter.addGroup(listStoryGroupData)
        listStoryGroupData.onClickStoryItem = object : ListStoryGroupData.OnClickStoryItem {
            override fun onClickStoryItem(item: StoryDetail) {
                mPresenter.executeCommand(GotoStoryDetailCmd(item))
            }
        }

    }

    fun showTopBanner(data: List<Story>) {
        topBannerGroupData.reset(data)
        topBannerGroupData.show()
    }

    fun showListStory(data: List<StoryDetail>) {
        listStoryGroupData.reset(data)
        listStoryGroupData.show()
    }

    class GotoStoryDetailCmd(val item: StoryDetail) : ICommand
}


