package com.dede.dedegame.presentation.story_cover.groups

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide

import com.quangph.base.mvp.IPresenter
import com.quangph.base.view.recyclerview.adapter.BaseRclvHolder
import com.quangph.base.view.recyclerview.adapter.group.GroupData
import com.quangph.base.view.recyclerview.adapter.group.GroupRclvVH
import com.dede.dedegame.R

import com.dede.dedegame.domain.model.StoryDetail

class TopCoverGroupData(data: StoryDetail?) :
    GroupData<StoryDetail>(data) {
    var mPresenter: IPresenter? = null

    var onClickTopCoverItem: OnClickTopCoverItem? = null

    override fun getDataInGroup(position: Int): Any? {
        return data
    }

    override fun getCount(): Int {
        return 1
    }

    override fun getItemViewType(positionInGroup: Int): Int {
        return CoverStoryViewType.TOP_COVER
    }

    override fun onCreateVH(itemView: View, viewType: Int): BaseRclvHolder<*>? {
        Log.i("onCreateVH tablayout", "")
        if (viewType == CoverStoryViewType.TOP_COVER) {
            return TopCoverVH(itemView, this)
        }
        return null
    }


    override fun getLayoutResource(viewType: Int): Int {
        if (viewType == CoverStoryViewType.TOP_COVER) {
            return R.layout.item_story_cover_top
        }
        return INVALID_RESOURCE
    }

    private class TopCoverVH(itemView: View, val homeTabGroupData: TopCoverGroupData) :
        GroupRclvVH<StoryDetail, TopCoverGroupData>(itemView) {

        private var imvThumbnail: ImageView
        private var txtStoryName: TextView
        private var txtReadNow: TextView
        private var txtReadLater: TextView
        private var txtFavCount: TextView
        private var txtCountViewer: TextView

        init {
            imvThumbnail = itemView.findViewById(R.id.imvThumbnail)
            txtStoryName = itemView.findViewById(R.id.txtStoryName)
            txtReadNow = itemView.findViewById(R.id.txtReadNow)
            txtReadLater = itemView.findViewById(R.id.txtReadLater)
            txtFavCount = itemView.findViewById(R.id.txtFavCount)
            txtCountViewer = itemView.findViewById(R.id.txtCountViewer)

        }

        override fun onBind(vhData: StoryDetail?) {
            super.onBind(vhData)
            vhData?.let {
                Glide.with(itemView.context).load(it.image).into(imvThumbnail)
                txtStoryName.text = if (it.title.isNullOrEmpty()) "" else it.title
                txtFavCount.text = if (it.likes != null && it.likes != 0) it.likes.toString() else "0"
                txtCountViewer.text = if (it.views != null && it.views != 0) it.views.toString() else "0"
                txtReadNow.setOnClickListener {
                    groupData.onClickTopCoverItem?.onClickReadNow(vhData)
                }
            }
        }

    }

    interface OnClickTopCoverItem {
        fun onClickReadNow(item: StoryDetail)
        fun onClickReadLater(item: StoryDetail)
    }
}