package com.dede.dedegame.presentation.home.game.groups

import android.util.Log
import android.view.View
import android.widget.TextView
import com.dede.dedegame.R
import com.dede.dedegame.domain.model.StoryDetail
import com.quangph.base.mvp.IPresenter
import com.quangph.base.view.recyclerview.adapter.BaseRclvHolder
import com.quangph.base.view.recyclerview.adapter.group.GroupData
import com.quangph.base.view.recyclerview.adapter.group.GroupRclvVH

class GameTagGroupData(tags: List<String>?) :
    GroupData<List<String>>(tags) {
    var mPresenter: IPresenter? = null

    var onClickTopCoverItem: OnClickTopCoverItem? = null

    override fun getDataInGroup(position: Int): Any? {
        return data[position]
    }

    override fun getCount(): Int {
        val size = if (data != null) data.size else 0
        Log.i("Size data", size.toString())
        return size
    }

    override fun getItemViewType(positionInGroup: Int): Int {
        return GameDetailViewType.CHILD_GAME_TAG
    }

    override fun onCreateVH(itemView: View, viewType: Int): BaseRclvHolder<*>? {
        Log.i("onCreateVH tablayout", "")
        if (viewType == GameDetailViewType.CHILD_GAME_TAG) {
            return OwnNewsVH(itemView, this)
        }
        return null
    }


    override fun getLayoutResource(viewType: Int): Int {
        if (viewType == GameDetailViewType.CHILD_GAME_TAG) {
            return R.layout.item_child_game_detail_tag
        }
        return INVALID_RESOURCE
    }

    private class OwnNewsVH(itemView: View, val gameDetailGroupData: GameTagGroupData) :
        GroupRclvVH<String, GameTagGroupData>(itemView) {

        private val txtTag by lazy { itemView.findViewById<TextView>(R.id.txtTag) }

        override fun onBind(vhData: String?) {
            super.onBind(vhData)
            vhData?.let {
                txtTag.text = it
            }
        }

    }

    interface OnClickTopCoverItem {
        fun onClickReadNow(item: StoryDetail)
        fun onClickReadLater(item: StoryDetail)
    }
}