package com.dede.dedegame.presentation.home.fragments.main_game.groups

import android.util.Log
import android.view.View
import android.widget.Toast
import com.dede.dedegame.R
import com.dede.dedegame.presentation.widget.customTab.CustomTabView
import com.dede.dedegame.presentation.widget.customTab.TabModel
import com.quangph.base.mvp.IPresenter
import com.quangph.base.view.recyclerview.adapter.BaseRclvHolder
import com.quangph.base.view.recyclerview.adapter.group.GroupData
import com.quangph.base.view.recyclerview.adapter.group.GroupRclvVH

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

    private class TabLayoutVH(itemView: View, val homeTabGroupData: GameTabGroupData) :
        GroupRclvVH<Unit, GameTabGroupData>(itemView) {
        private var tabLayout: CustomTabView

        init {
            tabLayout = itemView.findViewById(R.id.tlHome)
            tabLayout.addItemTab(
                TabModel(
                    tabLayout.context.getString(R.string.event_title),
                    R.drawable.event,
                    R.drawable.event_unselect,
                    true
                )
            )
            tabLayout.addItemTab(
                TabModel(
                    tabLayout.context.getString(R.string.notification_title),
                    R.drawable.megaphone,
                    R.drawable.megaphone_unselect,
                    false
                )
            )
            tabLayout.addItemTab(
                TabModel(
                    tabLayout.context.getString(R.string.game_title),
                    R.drawable.gaming,
                    R.drawable.gaming_unselect,
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