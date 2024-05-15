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

class SummaryGroupData(data: StoryDetail?) :
    GroupData<StoryDetail>(data) {
    var mPresenter: IPresenter? = null

    override fun getDataInGroup(position: Int): Any? {
        return data
    }

    override fun getCount(): Int {
        return 1
    }

    override fun getItemViewType(positionInGroup: Int): Int {
        return CoverStoryViewType.SUM_CONTENT
    }

    override fun onCreateVH(itemView: View, viewType: Int): BaseRclvHolder<*>? {
        Log.i("onCreateVH tablayout", "")
        if ( viewType == CoverStoryViewType.SUM_CONTENT) {
            Log.i("onCreateVH tablayout", "TAB_LAYOUT")
            return SummaryVH(itemView, this)
        }
        return null
    }


    override fun getLayoutResource(viewType: Int): Int {
        if (viewType == CoverStoryViewType.SUM_CONTENT) {
            return R.layout.item_story_cover_summary
        }
        return INVALID_RESOURCE
    }

    private class SummaryVH(itemView: View, val homeTabGroupData: SummaryGroupData) :
        GroupRclvVH<StoryDetail, SummaryGroupData>(itemView) {

        private var txtSummContent: TextView
        init {
            txtSummContent = itemView.findViewById(R.id.txtSummContent)
        }

        override fun onBind(vhData: StoryDetail?) {
            super.onBind(vhData)
            txtSummContent.text = vhData?.description
        }

    }
}