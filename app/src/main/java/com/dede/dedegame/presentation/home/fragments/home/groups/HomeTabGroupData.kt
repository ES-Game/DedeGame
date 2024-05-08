package com.dede.dedegame.presentation.home.fragments.home.groups

import android.util.Log
import android.view.View
import android.widget.Toast
import com.dede.dedegame.R
import com.dede.dedegame.presentation.home.fragments.main_game.groups.ItemViewType
import com.dede.dedegame.presentation.widget.customTab.CustomTabView
import com.dede.dedegame.presentation.widget.customTab.TabModel
import com.quangph.base.mvp.IPresenter
import com.quangph.base.view.recyclerview.adapter.BaseRclvHolder
import com.quangph.base.view.recyclerview.adapter.group.GroupData
import com.quangph.base.view.recyclerview.adapter.group.GroupRclvVH


class HomeTabGroupData(data: Unit?) :
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
        if (viewType == ItemViewType.TAB_LAYOUT) {
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

    private class TabLayoutVH(itemView: View, val homeTabGroupData: HomeTabGroupData) :
        GroupRclvVH<Unit, HomeTabGroupData>(itemView) {
        private var tabLayout: CustomTabView

        init {
            tabLayout = itemView.findViewById(R.id.tlHome)
            tabLayout.addItemTab(
                TabModel(
                    tabLayout.context.getString(R.string.news_title),
                    R.drawable.news_icon_fx,
                    R.drawable.news_icon,
                    true
                )
            )
            tabLayout.addItemTab(
                TabModel(
                    tabLayout.context.getString(R.string.rank_title),
                    R.drawable.trophy_icon_fx,
                    R.drawable.trophy_icon,
                    false
                )
            )
            tabLayout.addItemTab(
                TabModel(
                    tabLayout.context.getString(R.string.trending_title),
                    R.drawable.trending_icon_fx,
                    R.drawable.trending_icon,
                    false
                )
            )

            tabLayout.setEvenListener { tabModel, pos ->
                Toast.makeText(
                    tabLayout.context,
                    tabModel?.name,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        override fun onBind(vhData: Unit?) {
            super.onBind(vhData)

        }

    }
}