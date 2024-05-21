package com.dede.dedegame.presentation.home.game.groups

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dede.dedegame.R
import com.dede.dedegame.domain.model.mainGame.gameDetail.Game
import com.dede.dedegame.domain.model.mainGame.gameDetail.GameDetail
import com.dede.dedegame.domain.model.mainGame.gameDetail.OtherGame
import com.dede.dedegame.extension.loadImageFromUrl
import com.dede.dedegame.presentation.common.JustifiedTextView
import com.dede.dedegame.presentation.widget.htmltextview.HtmlTextView
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxItemDecoration
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.quangph.base.mvp.IPresenter
import com.quangph.base.view.recyclerview.adapter.BaseRclvHolder
import com.quangph.base.view.recyclerview.adapter.group.GroupData
import com.quangph.base.view.recyclerview.adapter.group.GroupRclvAdapter
import com.quangph.base.view.recyclerview.adapter.group.GroupRclvVH


class GameDetailGroupData(gameDetail: GameDetail?) :
    GroupData<GameDetail>(gameDetail) {
    var mPresenter: IPresenter? = null

    var onEvenGameDetailListener: OnEvenGameDetailListener? = null

    override fun getDataInGroup(position: Int): Any? {
        return data
    }

    override fun getCount(): Int {
        return 1
    }

    override fun getItemViewType(positionInGroup: Int): Int {
        return GameDetailViewType.GAME_DETAIL_ITEM
    }

    override fun onCreateVH(itemView: View, viewType: Int): BaseRclvHolder<*>? {
        Log.i("onCreateVH tablayout", "")
        if (viewType == GameDetailViewType.GAME_DETAIL_ITEM) {
            return OwnNewsVH(itemView, this)
        }
        return null
    }


    override fun getLayoutResource(viewType: Int): Int {
        if (viewType == GameDetailViewType.GAME_DETAIL_ITEM) {
            return R.layout.item_game_detail
        }
        return INVALID_RESOURCE
    }

    private class OwnNewsVH(itemView: View, val gameDetailGroupData: GameDetailGroupData) :
        GroupRclvVH<GameDetail, GameDetailGroupData>(itemView) {

        private val tvGameName by lazy { itemView.findViewById<TextView>(R.id.tvGameName) }
        private val imvGameThumb by lazy { itemView.findViewById<ImageView>(R.id.imvGameThumb) }
        private val tvShortDes by lazy { itemView.findViewById<JustifiedTextView>(R.id.tvShortDes) }
        private val rvTags by lazy { itemView.findViewById<RecyclerView>(R.id.rvTags) }
        private val htmlTvDes by lazy { itemView.findViewById<HtmlTextView>(R.id.htmlTvDes) }
        private val rvOtherGames by lazy { itemView.findViewById<RecyclerView>(R.id.rvOtherGames) }
        private val btnDownloadIOS by lazy { itemView.findViewById<View>(R.id.btnDownloadIOS) }
        private val btnDownloadAndroid by lazy { itemView.findViewById<View>(R.id.btnDownloadAndroid) }

        private val tagsAdapter = GroupRclvAdapter()
        private lateinit var gameTagGroupData: GameTagGroupData

        private val otherGamesAdapter = GroupRclvAdapter()
        private lateinit var gameOtherGroupData: GameOtherGroupData

        override fun onBind(vhData: GameDetail?) {
            super.onBind(vhData)
            vhData?.let {
                tvGameName.text = it.game?.title
                imvGameThumb.loadImageFromUrl(it.game?.image)
                tvShortDes.text = it.game?.shortDescription
                it.game?.description?.let { it1 -> htmlTvDes.setHtml(it1) }


                rvTags.layoutManager = LinearLayoutManager(rvOtherGames.context, RecyclerView.HORIZONTAL, false)
                rvTags.adapter = tagsAdapter
                gameTagGroupData = GameTagGroupData(null)
                tagsAdapter.addGroup(gameTagGroupData)
                gameTagGroupData.reset(it.game?.tags)
                gameTagGroupData.show()

                rvOtherGames.layoutManager =
                    LinearLayoutManager(rvOtherGames.context, RecyclerView.HORIZONTAL, false)
                rvOtherGames.adapter = otherGamesAdapter
                gameOtherGroupData = GameOtherGroupData(null)
                gameOtherGroupData.onClickOtherGameListener =
                    object : GameOtherGroupData.OnClickOtherGameListener {
                        override fun onClickGameItem(item: OtherGame) {
                            gameDetailGroupData.onEvenGameDetailListener?.onClickOtherGameItem(item)
                        }
                    }
                otherGamesAdapter.addGroup(gameOtherGroupData)
                gameOtherGroupData.reset(it.otherGames)
                gameOtherGroupData.show()

                clickOn(btnDownloadIOS) {
                    vhData.game?.let { it1 ->
                        gameDetailGroupData.onEvenGameDetailListener?.onClickIOSDownload(
                            it1
                        )
                    }
                }

                clickOn(btnDownloadAndroid) {
                    vhData.game?.let { it1 ->
                        gameDetailGroupData.onEvenGameDetailListener?.onClickAndroidDownload(
                            it1
                        )
                    }
                }

            }
        }

    }

    interface OnEvenGameDetailListener {
        fun onClickOtherGameItem(item: OtherGame)
        fun onClickAndroidDownload(item: Game)
        fun onClickIOSDownload(item: Game)
    }
}