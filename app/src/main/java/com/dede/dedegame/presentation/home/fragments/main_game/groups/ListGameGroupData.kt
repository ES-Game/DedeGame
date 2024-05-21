package com.dede.dedegame.presentation.home.fragments.main_game.groups

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.dede.dedegame.R
import com.dede.dedegame.domain.model.mainGame.Game
import com.quangph.base.mvp.IPresenter
import com.quangph.base.view.recyclerview.adapter.BaseRclvHolder
import com.quangph.base.view.recyclerview.adapter.group.GroupData
import com.quangph.base.view.recyclerview.adapter.group.GroupRclvVH


class ListGameGroupData(listGame: List<Game>?) :
    GroupData<List<Game>>(listGame) {
    var mPresenter: IPresenter? = null

    var onClickGameListener: OnClickGameListener? = null


    override fun getDataInGroup(position: Int): Any? {
        return data[position]
    }

    override fun getCount(): Int {
        val size = if (data != null) data.size else 0
        Log.i("Size data", size.toString())
        return size
    }

    override fun getItemViewType(positionInGroup: Int): Int {
        return ItemViewType.LIST_GAME
    }

    override fun onCreateVH(itemView: View, viewType: Int): BaseRclvHolder<*>? {
        Log.i("onCreateVH_LIST_STORY", viewType.toString())
        if (viewType == ItemViewType.LIST_GAME) {
            Log.i("onCreateVH", "LIST_STORY")
            return ListStoryVH(itemView, this)
        }
        return null
    }


    override fun getLayoutResource(viewType: Int): Int {
        return if (viewType == ItemViewType.LIST_GAME) {
            R.layout.item_game_main_list_game
        } else {
            INVALID_RESOURCE
        }
    }


    private class ListStoryVH(itemView: View, val listStoryGroupData: ListGameGroupData) :
        GroupRclvVH<Game, ListGameGroupData>(itemView) {

        private val imvAvatar by lazy { itemView.findViewById<ImageView>(R.id.imvAvatar) }
        private val imvAppleLabel by lazy { itemView.findViewById<ImageView>(R.id.imvAppleLabel) }
        private val imvAndroidLabel by lazy { itemView.findViewById<ImageView>(R.id.imvAndroidLabel) }

        override fun onBind(vhData: Game?) {
            super.onBind(vhData)
            imvAppleLabel.visibility = View.GONE
            imvAndroidLabel.visibility = View.GONE
            clickOn(itemView) {
                if (vhData != null) {
                    vhData.id?.let { it1 -> groupData.onClickGameListener?.onClickGameItem(it1) }
                }
            }
            vhData?.let { story ->
                Glide
                    .with(imvAvatar.context)
                    .load(story.image)
                    .centerCrop()
                    .into(imvAvatar)
            }

        }

    }

    interface OnClickGameListener {
        fun onClickGameItem(item: Int)
    }
}