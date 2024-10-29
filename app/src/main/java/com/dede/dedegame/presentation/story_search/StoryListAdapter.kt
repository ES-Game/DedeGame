package com.dede.dedegame.presentation.story_search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dede.dedegame.R
import com.dede.dedegame.domain.model.StoryDetail
import com.dede.dedegame.extension.loadImageFromUrl
import com.dede.dedegame.presentation.home.fragments.home_comic.story_list.StoryListViewType
import com.dede.dedegame.presentation.widget.RoundedTextView

class StoryListAdapter :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var listStory = arrayListOf<StoryDetail>()
    private var isLoadingAdded = false

    fun getStories(): List<StoryDetail> {
        return listStory
    }

    fun setListStory(newComments: List<StoryDetail>) {
        listStory.clear()
        listStory.addAll(newComments)
        notifyDataSetChanged()
    }

    fun addItems(newItems: List<StoryDetail>) {
        val startPosition = listStory.size
        listStory.addAll(newItems)
        notifyItemRangeInserted(startPosition, newItems.size)
    }

    fun addLoadingFooter() {
        if (!isLoadingAdded) {
            isLoadingAdded = true
            listStory.add(StoryDetail())
            notifyItemInserted(listStory.size - 1)
        }
    }

    fun removeLoadingFooter() {
        if (isLoadingAdded) {
            isLoadingAdded = false
            val position = listStory.size - 1
            if (position >= 0) {
                listStory.removeAt(position)
                notifyItemRemoved(position)
            }
        }
    }

    override fun getItemCount(): Int {
        return listStory.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == StoryListViewType.ITEM_LOAD_MORE) {
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
        return if (position == listStory.size - 1 && isLoadingAdded) StoryListViewType.ITEM_LOAD_MORE else StoryListViewType.ITEM_STORY
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