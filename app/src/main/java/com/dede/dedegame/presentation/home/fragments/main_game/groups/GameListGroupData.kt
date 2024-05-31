package com.dede.dedegame.presentation.home.fragments.main_game.groups

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.dede.dedegame.R
import com.dede.dedegame.domain.model.mainGame.GameListModel
import com.dede.dedegame.domain.model.mainGame.GameType
import com.dede.dedegame.domain.model.mainGame.gameDetail.GameInfo
import com.dede.dedegame.extension.loadImageFromUrl
import com.dede.dedegame.presentation.common.LogUtil
import com.quangph.base.mvp.IPresenter
import com.quangph.base.view.recyclerview.adapter.BaseRclvHolder
import com.quangph.base.view.recyclerview.adapter.group.GroupData
import com.quangph.base.view.recyclerview.adapter.group.GroupRclvVH


class GameListGroupData(gameListModel: GameListModel?) :
    GroupData<GameListModel?>(gameListModel) {
    var mPresenter: IPresenter? = null

    var onClickStoryItem: OnClickStoryItem? = null


    override fun getDataInGroup(position: Int): Any? {
        if (position == 0) {
            return data
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
        return ItemViewType.ITEM_GAME
    }

    override fun onCreateVH(itemView: View, viewType: Int): BaseRclvHolder<*>? {
        Log.i("onCreateVH_RANK", viewType.toString())
        if (viewType == ItemViewType.ITEM_GAME) {
            Log.i("onCreateVH", "RANK")
            return GridStoryVH(itemView, this)
        }

        if (viewType == ItemViewType.ITEM_TITLE_GAMES) {
            return CategoryNameViewHolder(itemView, this)
        }
        return null
    }


    override fun getLayoutResource(viewType: Int): Int {
        return if (viewType == ItemViewType.ITEM_GAME) {
            R.layout.item_game_main_list_game
        } else if (viewType == ItemViewType.ITEM_TITLE_GAMES) {
            return R.layout.item_category_title
        } else {
            INVALID_RESOURCE
        }
    }


    class GridStoryVH(itemView: View, val listStoryGroupData: GameListGroupData) :
        GroupRclvVH<GameInfo, GameListGroupData>(itemView) {

        private val imvAvatar by lazy { itemView.findViewById<ImageView>(R.id.imvAvatar) }
        private val imvAppleLabel by lazy { itemView.findViewById<ImageView>(R.id.imvAppleLabel) }
        private val imvAndroidLabel by lazy { itemView.findViewById<ImageView>(R.id.imvAndroidLabel) }

        override fun onBind(vhData: GameInfo?) {
            super.onBind(vhData)
            LogUtil.getInstance().e("Tap  ====> onBind")
            clickOn(itemView) {
                LogUtil.getInstance().e("Tap")
                vhData?.let { game ->
                    game.id?.let { it1 -> listStoryGroupData.onClickStoryItem?.onClickStoryItem(it1, 1) }
                }
            }
            imvAppleLabel.visibility = View.GONE
            imvAndroidLabel.visibility = View.GONE
            vhData?.let { game ->
                imvAvatar.loadImageFromUrl(game.image)
            }
        }
    }

    class CategoryNameViewHolder(itemView: View, val listStoryGroupData: GameListGroupData) :
        GroupRclvVH<GameListModel, GameListGroupData>(itemView) {

        private var tvName: TextView


        init {
            tvName = itemView.findViewById(R.id.tvCategoryName)
        }

        override fun onBind(vhData: GameListModel?) {
            super.onBind(vhData)
            vhData?.let {
                tvName.text = vhData.title
                clickOn(itemView) {
                    vhData.type?.let { it1 ->
                        listStoryGroupData.onClickStoryItem?.onClickViewMore(
                            it1
                        )
                    }
                }
            }
        }

    }

    interface OnClickStoryItem {

        fun onClickViewMore(gameType: GameType)
        fun onClickStoryItem(id: Int, status: Int)
    }
}