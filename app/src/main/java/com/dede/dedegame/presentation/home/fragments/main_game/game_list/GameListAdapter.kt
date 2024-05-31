package com.dede.dedegame.presentation.home.fragments.main_game.game_list

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.dede.dedegame.R
import com.dede.dedegame.domain.model.mainGame.Game
import com.dede.dedegame.extension.loadImageFromUrl
import com.dede.dedegame.presentation.home.fragments.main_game.game_list.groups.GameListViewType

class GameListAdapter(private var hasLoadMore: Boolean) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var listGame = arrayListOf<Game>()
    fun getEndPosListGame(): Int {
        return listGame.size
    }

    fun setListGame(list: List<Game>) {
        val templist = arrayListOf<Game>()
        templist.addAll(list)
        listGame = templist
        notifyDataSetChanged()
    }

    fun addItemsAndNotify(items: List<Game>) {
        val start: Int = listGame.size
        listGame.addAll(items)
        notifyItemRangeInserted(start, items.size)
    }

    fun setLoadMore(hasLoadMore: Boolean) {
        this.hasLoadMore = hasLoadMore
    }

    override fun getItemCount(): Int {
        val size = listGame.size
        Log.i("Size data", size.toString())
        return if (hasLoadMore) {
            size + 1
        } else {
            size
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (position == listGame.size) {
            if (hasLoadMore) {
                return GameListViewType.ITEM_LOAD_MORE
            } else {
                return GameListViewType.ITEM_GAME
            }
        } else {
            return GameListViewType.ITEM_GAME
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == GameListViewType.ITEM_LOAD_MORE) {
            LoadingVH(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_load_more, parent, false)
            )
        } else {
            GameGridVH(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_game_main_list_game, parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is GameGridVH) {
            val game = listGame[position]
            holder.imvAppleLabel.visibility = View.GONE
            holder.imvAndroidLabel.visibility = View.GONE
            holder.imvAvatar.loadImageFromUrl(game.image)
            holder.itemView.setOnClickListener {
                onClickListener?.onClickGameItem(game)
            }
        }
    }

    inner class LoadingVH(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
    }


    private class GameGridVH(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        val imvAvatar by lazy { itemView.findViewById<ImageView>(R.id.imvAvatar) }
        val imvAppleLabel by lazy { itemView.findViewById<ImageView>(R.id.imvAppleLabel) }
        val imvAndroidLabel by lazy { itemView.findViewById<ImageView>(R.id.imvAndroidLabel) }
    }

    private var onClickListener: OnClickListener? = null

    fun setOnClickListener(listener: OnClickListener) {
        onClickListener = listener
    }

    interface OnClickListener {
        fun onClickGameItem(item: Game)
    }
}