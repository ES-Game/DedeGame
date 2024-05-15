package com.dede.dedegame.presentation.home.fragments.home_comic.groups

import android.graphics.Color
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.dede.dedegame.R
import com.dede.dedegame.presentation.widget.RoundedTextView
import com.quangph.base.mvp.IPresenter
import com.quangph.base.view.recyclerview.adapter.BaseRclvHolder
import com.quangph.base.view.recyclerview.adapter.group.GroupData
import com.quangph.base.view.recyclerview.adapter.group.GroupRclvVH
import com.dede.dedegame.domain.model.StoryDetail


class RankStoryGroupData(listStory: List<StoryDetail>?) :
    GroupData<List<StoryDetail>>(listStory) {
    var mPresenter: IPresenter? = null

    var onClickStoryItem: OnClickStoryItem? = null


    override fun getDataInGroup(position: Int): Any? {
        return data[position]
    }

    override fun getCount(): Int {
        val size = if (data != null) data.size else 0
        Log.i("Size data", size.toString())
        return size
    }

    override fun getItemViewType(positionInGroup: Int): Int {
        return HomeComicItemViewType.RANK
    }

    override fun onCreateVH(itemView: View, viewType: Int): BaseRclvHolder<*>? {
        Log.i("onCreateVH_RANK", viewType.toString())
        if (viewType == HomeComicItemViewType.RANK) {
            Log.i("onCreateVH", "RANK")
            return GridStoryVH(itemView, this)
        }
        return null
    }


    override fun getLayoutResource(viewType: Int): Int {
        return if (viewType == HomeComicItemViewType.RANK) {
            R.layout.item_story_grid_layout
        } else {
            INVALID_RESOURCE
        }
    }


    private class GridStoryVH(itemView: View, val listStoryGroupData: RankStoryGroupData) :
        GroupRclvVH<StoryDetail, RankStoryGroupData>(itemView) {

        private var ivThumb: ImageView
        private var tvDes: TextView
        private val tvRankIndex by lazy { itemView.findViewById<RoundedTextView>(R.id.tvRankIndex) }
        init {

            ivThumb = itemView.findViewById(R.id.ivThumb)

            tvDes = itemView.findViewById(R.id.tvDesc)


        }

        override fun onBind(vhData: StoryDetail?) {
            super.onBind(vhData)
            clickOn(itemView) {
                vhData?.id?.let {
                    groupData.onClickStoryItem?.onClickStoryItem(it)
                }
            }
            tvRankIndex.visibility = View.VISIBLE
            tvRankIndex.text = (adapterPosition + 1).toString()
            fillColorIndex(adapterPosition)
            vhData?.let { story ->
                Glide
                    .with(ivThumb.context)
                    .load(story.image)
                    .centerCrop()
                    .into(ivThumb)

                tvDes.text = story.description

            }

        }

        private fun fillColorIndex(positionInGroup: Int) {
            tvRankIndex.visibility = if (positionInGroup > 5) View.INVISIBLE else View.VISIBLE
            when (positionInGroup) {
                0 -> tvRankIndex.setSolidColorInt(Color.parseColor("#f04e29"))
                1 -> tvRankIndex.setSolidColorInt(Color.parseColor("#fcaf17"))
                2 -> tvRankIndex.setSolidColorInt(Color.parseColor("#2bb673"))
                3 -> tvRankIndex.setSolidColorInt(Color.parseColor("#3c9cd7"))
                4 -> tvRankIndex.setSolidColorInt(Color.parseColor("#3c9cd7"))
                5 -> tvRankIndex.setSolidColorInt(Color.parseColor("#3c9cd7"))
                else -> {}
            }
        }

    }

    interface OnClickStoryItem {
        fun onClickStoryItem(id: Int)
    }
}