package com.dede.dedegame.presentation.home.game.groups

import android.util.Log
import android.view.View
import android.widget.ImageView
import com.dede.dedegame.R
import com.dede.dedegame.domain.model.mainGame.gameDetail.OtherGame
import com.dede.dedegame.extension.loadImageFromUrl
import com.quangph.base.mvp.IPresenter
import com.quangph.base.view.recyclerview.adapter.BaseRclvHolder
import com.quangph.base.view.recyclerview.adapter.group.GroupData
import com.quangph.base.view.recyclerview.adapter.group.GroupRclvVH

class GameOtherGroupData(tags: List<OtherGame>?) :
    GroupData<List<OtherGame>>(tags) {
    var mPresenter: IPresenter? = null

    var onClickOtherGameListener: OnClickOtherGameListener? = null

    override fun getDataInGroup(position: Int): Any? {
        return data[position]
    }

    override fun getCount(): Int {
        val size = if (data != null) data.size else 0
        Log.i("Size data", size.toString())
        return size
    }

    override fun getItemViewType(positionInGroup: Int): Int {
        return GameDetailViewType.CHILD_OTHER_GAME
    }

    override fun onCreateVH(itemView: View, viewType: Int): BaseRclvHolder<*>? {
        Log.i("onCreateVH tablayout", "")
        if (viewType == GameDetailViewType.CHILD_OTHER_GAME) {
            return OtherGameVH(itemView, this)
        }
        return null
    }


    override fun getLayoutResource(viewType: Int): Int {
        if (viewType == GameDetailViewType.CHILD_OTHER_GAME) {
            return R.layout.item_child_other_game_detail
        }
        return INVALID_RESOURCE
    }

    private class OtherGameVH(itemView: View, val gameOtherGroupData: GameOtherGroupData) :
        GroupRclvVH<OtherGame, GameOtherGroupData>(itemView) {

        private val ivThumb by lazy { itemView.findViewById<ImageView>(R.id.ivThumb) }

        override fun onBind(vhData: OtherGame?) {
            super.onBind(vhData)
            vhData?.let {
                ivThumb.loadImageFromUrl(it.image)
                clickOn(itemView) {
                    groupData.onClickOtherGameListener?.onClickGameItem(vhData)
                }
            }
        }

    }

    interface OnClickOtherGameListener {
        fun onClickGameItem(item: OtherGame)
    }
}