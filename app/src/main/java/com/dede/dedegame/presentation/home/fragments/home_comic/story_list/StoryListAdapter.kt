package com.dede.dedegame.presentation.home.fragments.home_comic.story_list

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dede.dedegame.R
import com.dede.dedegame.domain.model.StoryDetail
import com.dede.dedegame.extension.loadImageFromUrl
import com.dede.dedegame.presentation.common.LogUtil
import com.dede.dedegame.presentation.home.fragments.home_comic.story_list.StoryListViewType.Companion.ITEM_LOAD_MORE
import com.dede.dedegame.presentation.home.fragments.home_comic.story_list.StoryListViewType.Companion.ITEM_STORY
import com.dede.dedegame.presentation.widget.RoundedTextView

class StoryListAdapter(private var hasLoadMore: Boolean) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var listStory = arrayListOf<StoryDetail>()

    fun getEndPosListStory(): Int {
        return listStory.size
    }

    fun setListStory(list: List<StoryDetail>) {
        val tempList = arrayListOf<StoryDetail>()
        tempList.addAll(list)
        listStory = tempList
        notifyDataSetChanged()
    }

    fun addItemsAndNotify(items: List<StoryDetail>) {
        val start: Int = listStory.size
        listStory.addAll(items)
        notifyItemRangeInserted(start, items.size)
    }

    fun setLoadMore(hasLoadMore: Boolean) {
        this.hasLoadMore = hasLoadMore
    }

    override fun getItemCount(): Int {
        val size = if (listStory != null) listStory.size else 0
        Log.i("Size data", size.toString())
        return if (hasLoadMore) {
            size + 1
        } else {
            size
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == ITEM_LOAD_MORE) {
            LoadingVH(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_load_more, parent, false)
            )
        } else {
            StoryGridVH(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_story_grid_layout, parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is StoryGridVH) {
            val storyDetail = listStory[position]
            holder.tvRankIndex.visibility = View.INVISIBLE
            storyDetail?.let { story ->
                holder.ivThumb.loadImageFromUrl(story.image)
                holder.tvDes.text = story.title
                holder.tvLiked.text = story.likes.toString()
                holder.tvViewed.text = story.views.toString()
                holder.itemView.setOnClickListener {
                    onClickListener?.onClickGameItem(story)
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (position == listStory.size) {
            if (hasLoadMore) {
                return ITEM_LOAD_MORE
            } else {
                return ITEM_STORY
            }
        } else {
            return ITEM_STORY
        }
    }

    inner class LoadingVH(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
    }

    inner class StoryGridVH(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        val ivThumb by lazy { itemView.findViewById<ImageView>(R.id.ivThumb) }
        val tvDes by lazy { itemView.findViewById<TextView>(R.id.tvDesc) }
        val tvRankIndex by lazy { itemView.findViewById<RoundedTextView>(R.id.tvRankIndex) }
        val tvLiked by lazy { itemView.findViewById<TextView>(R.id.tvLiked) }
        val tvViewed by lazy { itemView.findViewById<TextView>(R.id.tvViewed) }
    }

    private var onClickListener: OnClickListener? = null

    fun setOnClickListener(listener: OnClickListener) {
        onClickListener = listener
    }

    interface OnClickListener {
        fun onClickGameItem(item: StoryDetail)
    }
}