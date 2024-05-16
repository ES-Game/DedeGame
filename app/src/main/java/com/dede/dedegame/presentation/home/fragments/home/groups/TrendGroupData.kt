package com.dede.dedegame.presentation.home.fragments.home.groups

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.dede.dedegame.R
import com.quangph.base.mvp.IPresenter
import com.quangph.base.view.recyclerview.adapter.BaseRclvHolder
import com.quangph.base.view.recyclerview.adapter.group.GroupData
import com.quangph.base.view.recyclerview.adapter.group.GroupRclvVH
import com.dede.dedegame.domain.model.StoryDetail


class TrendGroupData(listTrend: List<StoryDetail>?) :
    GroupData<List<StoryDetail>>(listTrend) {
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
        return ItemViewType.TAB_TREND
    }

    override fun onCreateVH(itemView: View, viewType: Int): BaseRclvHolder<*>? {
        Log.i("onCreateVH_LIST_STORY", viewType.toString())
        if (viewType == ItemViewType.TAB_TREND) {
            Log.i("onCreateVH", "LIST_STORY")
            return TrendItemVH(itemView, this)
        }
        return null
    }


    override fun getLayoutResource(viewType: Int): Int {
        return if (viewType == ItemViewType.TAB_TREND) {
            R.layout.item_home_story
        } else {
            INVALID_RESOURCE
        }
    }


    private class TrendItemVH(itemView: View, val trendGroupData: TrendGroupData) :
        GroupRclvVH<StoryDetail, TrendGroupData>(itemView) {

        private var ivThumb: ImageView
        private var tvName: TextView
        private var tvCreatedAt: TextView
        private var tvDes: TextView

        init {

            ivThumb = itemView.findViewById(R.id.ivThumb)
            tvName = itemView.findViewById(R.id.tvStoryName)
            tvCreatedAt = itemView.findViewById(R.id.tvCreateAt)
            tvDes = itemView.findViewById(R.id.tvDesc)


        }

        override fun onBind(vhData: StoryDetail?) {
            super.onBind(vhData)
            clickOn(itemView) {
                if (vhData != null) {
                    groupData.onClickStoryItem?.onClickStoryItem(vhData)
                }
            }
            vhData?.let { story ->
                Glide
                    .with(ivThumb.context)
                    .load(story.image)
                    .centerCrop()
                    .into(ivThumb);
                tvName.text = story.title
                tvDes.text = story.description
                tvCreatedAt.text = story.createdAt
            }

        }

    }

    interface OnClickStoryItem {
        fun onClickStoryItem(item: StoryDetail)
    }
}