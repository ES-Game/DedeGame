package com.dede.dedegame.presentation.home.news.groups

import android.util.Log
import android.view.View
import android.widget.TextView
import com.dede.dedegame.R
import com.dede.dedegame.domain.model.StoryDetail
import com.dede.dedegame.domain.model.news.RelatedArticle
import com.quangph.base.mvp.IPresenter
import com.quangph.base.view.recyclerview.adapter.BaseRclvHolder
import com.quangph.base.view.recyclerview.adapter.group.GroupData
import com.quangph.base.view.recyclerview.adapter.group.GroupRclvVH

class OtherNewsGroupData(data: List<RelatedArticle>?) :
    GroupData<List<RelatedArticle>>(data) {
    var mPresenter: IPresenter? = null

    var onClickListener: OnClickListener? = null

    override fun getDataInGroup(position: Int): Any? {
        return data[position]
    }

    override fun getCount(): Int {
        val size = if (data != null) data.size else 0
        Log.i("Size data", size.toString())
        return size
    }

    override fun getItemViewType(positionInGroup: Int): Int {
        return NewsDetailViewType.OTHER_NEWS
    }

    override fun onCreateVH(itemView: View, viewType: Int): BaseRclvHolder<*>? {
        Log.i("onCreateVH tablayout", "")
        if (viewType == NewsDetailViewType.OTHER_NEWS) {
            return TopCoverVH(itemView, this)
        }
        return null
    }


    override fun getLayoutResource(viewType: Int): Int {
        if (viewType == NewsDetailViewType.OTHER_NEWS) {
            return R.layout.item_other_news_details
        }
        return INVALID_RESOURCE
    }

    private class TopCoverVH(itemView: View, val homeTabGroupData: OtherNewsGroupData) :
        GroupRclvVH<RelatedArticle, OtherNewsGroupData>(itemView) {

        private val txtTitleNews by lazy { itemView.findViewById<TextView>(R.id.txtTitleNews) }

        init {

        }

        override fun onBind(vhData: RelatedArticle?) {
            super.onBind(vhData)
            clickOn(itemView) {
                if (vhData != null) {
                    groupData.onClickListener?.onClickOtherNews(vhData)
                }
            }
            vhData?.let {
                txtTitleNews.text = if (it.title.isNullOrEmpty()) "" else it.title
            }
        }

    }

    interface OnClickListener {
        fun onClickOtherNews(item: RelatedArticle)
    }
}