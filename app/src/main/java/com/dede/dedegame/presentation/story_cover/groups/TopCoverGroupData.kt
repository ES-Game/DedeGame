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
        private var txtCountCmt: TextView
        private var txtCmtLabel: TextView
        private var txtStatusLabel: TextView
        private var txtStatus: TextView

        init {
            imvThumbnail = itemView.findViewById(R.id.imvThumbnail)
            txtStoryName = itemView.findViewById(R.id.txtStoryName)
            txtReadNow = itemView.findViewById(R.id.txtReadNow)
            txtReadLater = itemView.findViewById(R.id.txtReadLater)
            txtFavCount = itemView.findViewById(R.id.txtFavCount)
            txtCountViewer = itemView.findViewById(R.id.txtCountViewer)
            txtCountCmt = itemView.findViewById(R.id.txtCountCmt)
            txtCmtLabel = itemView.findViewById(R.id.txtCmtLabel)
            txtStatusLabel = itemView.findViewById(R.id.txtStatusLabel)
            txtStatus = itemView.findViewById(R.id.txtStatus)

        }

        override fun onBind(vhData: StoryDetail?) {
            super.onBind(vhData)
            vhData?.let {
                Glide.with(itemView.context).load(it.image).into(imvThumbnail)
                txtStoryName.text = if (it.title.isNullOrEmpty()) "" else it.title
                txtFavCount.text = if (it.likes != null && it.likes != 0) it.likes.toString() else "0"
                txtCountViewer.text = if (it.views != null && it.views != 0) it.views.toString() else "0"
                txtCountCmt.text = if (it.comments != null && it.comments != 0) it.comments.toString() else "0"
                txtCmtLabel.text = if (it.comments != null && it.comments != 0 && it.comments!! > 1) itemView.context.getString(R.string.story_cover_top_comments_label) else itemView.context.getString(R.string.story_cover_top_comment_label)

                txtReadNow.setOnClickListener {
                    homeTabGroupData.onClickTopCoverItem?.onClickReadNow(vhData)
                }

                if (!it.authors.isNullOrEmpty()){
                    txtStatusLabel.visibility = View.VISIBLE
                    txtStatus.visibility = View.VISIBLE
                    txtStatus.text = it.authors!![0].name
                } else{
                    txtStatusLabel.visibility = View.INVISIBLE
                    txtStatus.visibility = View.INVISIBLE
                }
            }
        }

    }

    interface OnClickTopCoverItem {
        fun onClickReadNow(item: StoryDetail)
        fun onClickReadLater(item: StoryDetail)
    }
}