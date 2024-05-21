package com.dede.dedegame.presentation.home.fragments.main_game.groups

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.dede.dedegame.R
import com.dede.dedegame.domain.model.home.OpenedGame
import com.dede.dedegame.domain.model.mainGame.OpenTempGame
import com.dede.dedegame.extension.loadImageFromUrl
import com.dede.dedegame.presentation.common.LogUtil
import com.quangph.base.mvp.IPresenter
import com.quangph.base.view.recyclerview.adapter.BaseRclvHolder
import com.quangph.base.view.recyclerview.adapter.group.GroupData
import com.quangph.base.view.recyclerview.adapter.group.GroupRclvVH


class OpenGamesGroupData(category: OpenTempGame?) :
    GroupData<OpenTempGame?>(category) {
    var mPresenter: IPresenter? = null

    var onClickStoryItem: OnClickStoryItem? = null


    override fun getDataInGroup(position: Int): Any? {
        if (position == 0) {
            return data?.title
        }
        return data?.games?.get(position - 1)
    }

    override fun getCount(): Int {
        return (data?.games?.size ?: 0) + 1
    }

    override fun getItemViewType(positionInGroup: Int): Int {
        if (positionInGroup == 0) {
            return ItemViewType.ITEM_TITLE_GAMES
        }
        return ItemViewType.ITEM_OPEN_GAME
    }

    override fun onCreateVH(itemView: View, viewType: Int): BaseRclvHolder<*>? {
        Log.i("onCreateVH_RANK", viewType.toString())
        if (viewType == ItemViewType.ITEM_OPEN_GAME) {
            Log.i("onCreateVH", "RANK")
            return GridStoryVH(itemView, this)
        }

        if (viewType == ItemViewType.ITEM_TITLE_GAMES) {
            return CategoryNameViewHolder(itemView, this)
        }
        return null
    }


    override fun getLayoutResource(viewType: Int): Int {
        return if (viewType == ItemViewType.ITEM_OPEN_GAME) {
            R.layout.item_game_main_list_game
        } else if (viewType == ItemViewType.ITEM_TITLE_GAMES) {
            return R.layout.item_category_title
        } else {
            INVALID_RESOURCE
        }
    }


    class GridStoryVH(itemView: View, val listStoryGroupData: OpenGamesGroupData) :
        GroupRclvVH<OpenedGame, OpenGamesGroupData>(itemView) {

        private val imvAvatar by lazy { itemView.findViewById<ImageView>(R.id.imvAvatar) }
        private val imvAppleLabel by lazy { itemView.findViewById<ImageView>(R.id.imvAppleLabel) }
        private val imvAndroidLabel by lazy { itemView.findViewById<ImageView>(R.id.imvAndroidLabel) }

        override fun onBind(vhData: OpenedGame?) {
            super.onBind(vhData)
            LogUtil.getInstance().e("Tap  ====> onBind")
            clickOn(itemView) {
                LogUtil.getInstance().e("Tap")
                vhData?.id?.let {
                    groupData.onClickStoryItem?.onClickStoryItem(it)
                }
            }
            imvAppleLabel.visibility = View.GONE
            imvAndroidLabel.visibility = View.GONE
            vhData?.let { story ->
                imvAvatar.loadImageFromUrl(story.image)
            }
        }
    }

    class CategoryNameViewHolder(itemView: View, val listStoryGroupData: OpenGamesGroupData) :
        GroupRclvVH<String, OpenGamesGroupData>(itemView) {

        private var tvName: TextView


        init {
            tvName = itemView.findViewById(R.id.tvCategoryName)
        }

        override fun onBind(vhData: String) {
            super.onBind(vhData)
            tvName.text = vhData
            LogUtil.getInstance().e("Tap  ====> onBind")
            clickOn(itemView) {
                LogUtil.getInstance().e("Tap")
            }
        }

    }

    interface OnClickStoryItem {
        fun onClickStoryItem(id: Int)
    }
}