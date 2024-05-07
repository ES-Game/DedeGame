package com.dede.dedegame.presentation.main_game.fragments.groups

import android.util.Log
import android.view.View

import com.quangph.base.mvp.IPresenter
import com.quangph.base.view.recyclerview.adapter.BaseRclvHolder
import com.quangph.base.view.recyclerview.adapter.group.GroupData
import com.quangph.base.view.recyclerview.adapter.group.GroupRclvVH
import com.dede.dedegame.R

import com.google.android.material.tabs.TabLayout

class GameTabGroupData(data: Unit?) :
    GroupData<Unit>(data) {
    var mPresenter: IPresenter? = null

    override fun getDataInGroup(position: Int): Any? {
        return data
    }

    override fun getCount(): Int {
        return 1
    }

    override fun getItemViewType(positionInGroup: Int): Int {
        return ItemViewType.TAB_LAYOUT
    }

    override fun onCreateVH(itemView: View, viewType: Int): BaseRclvHolder<*>? {
        Log.i("onCreateVH tablayout", "")
        if ( viewType == ItemViewType.TAB_LAYOUT) {
            Log.i("onCreateVH tablayout", "TAB_LAYOUT")
            return TabLayoutVH(itemView, this)
        }
        return null
    }


    override fun getLayoutResource(viewType: Int): Int {
        if (viewType == ItemViewType.TAB_LAYOUT) {
            return R.layout.item_home_tab
        }
        return INVALID_RESOURCE
    }

    private class TabLayoutVH(itemView: View, val homeTabGroupData: GameTabGroupData) :
        GroupRclvVH<Unit, GameTabGroupData>(itemView) {
        private var tabLayout: TabLayout
        init {
            tabLayout = itemView.findViewById(R.id.tlHome)
            val firstTab = tabLayout.newTab()
            firstTab.setText(R.string.news_title)

            val secondTab = tabLayout.newTab()
            secondTab.setText(R.string.rank_title)

            val thirdTab = tabLayout.newTab()
            thirdTab.setText(R.string.trending_title)

            tabLayout.addTab(firstTab)

            tabLayout.addTab(secondTab)

            tabLayout.addTab(thirdTab)


        }

        override fun onBind(vhData: Unit?) {
            super.onBind(vhData)

        }

    }
}