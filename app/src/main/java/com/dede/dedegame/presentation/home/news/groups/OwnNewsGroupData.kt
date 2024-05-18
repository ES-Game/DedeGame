package com.dede.dedegame.presentation.home.news.groups

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.dede.dedegame.R
import com.dede.dedegame.domain.model.StoryDetail
import com.dede.dedegame.domain.model.news.Article
import com.dede.dedegame.presentation.widget.CustomWebView
import com.quangph.base.mvp.IPresenter
import com.quangph.base.view.recyclerview.adapter.BaseRclvHolder
import com.quangph.base.view.recyclerview.adapter.group.GroupData
import com.quangph.base.view.recyclerview.adapter.group.GroupRclvVH

class OwnNewsGroupData(data: Article?) :
    GroupData<Article>(data) {
    var mPresenter: IPresenter? = null

    var onClickTopCoverItem: OnClickTopCoverItem? = null

    override fun getDataInGroup(position: Int): Any? {
        return data
    }

    override fun getCount(): Int {
        return 1
    }

    override fun getItemViewType(positionInGroup: Int): Int {
        return NewsDetailViewType.OWN_NEWS
    }

    override fun onCreateVH(itemView: View, viewType: Int): BaseRclvHolder<*>? {
        Log.i("onCreateVH tablayout", "")
        if (viewType == NewsDetailViewType.OWN_NEWS) {
            return OwnNewsVH(itemView, this)
        }
        return null
    }


    override fun getLayoutResource(viewType: Int): Int {
        if (viewType == NewsDetailViewType.OWN_NEWS) {
            return R.layout.item_own_news_details
        }
        return INVALID_RESOURCE
    }

    private class OwnNewsVH(itemView: View, val ownNewsGroupData: OwnNewsGroupData) :
        GroupRclvVH<Article, OwnNewsGroupData>(itemView) {

        private val ivImageNews by lazy { itemView.findViewById<ImageView>(R.id.ivImageNews) }
        private val txtDateTime by lazy { itemView.findViewById<TextView>(R.id.txtDateTime) }
        private val txtTitleNews by lazy { itemView.findViewById<TextView>(R.id.txtTitleNews) }
        private val wvContent by lazy { itemView.findViewById<CustomWebView>(R.id.wvContent) }

        override fun onBind(vhData: Article?) {
            super.onBind(vhData)
            vhData?.let {
                Glide.with(itemView.context).load(it.image).into(ivImageNews)
                txtDateTime.text = if (it.date.isNullOrEmpty()) "" else it.date
                txtTitleNews.text = if (it.title.isNullOrEmpty()) "" else it.title
                it.content?.let { htmlCode ->
                    wvContent.visibility = View.VISIBLE
                    wvContent.loadHtml(htmlCode)
                    wvContent.setScrollingEnabled(false)
                } ?: {
                    wvContent.visibility = View.GONE
                }

            }
        }

    }

    interface OnClickTopCoverItem {
        fun onClickReadNow(item: StoryDetail)
        fun onClickReadLater(item: StoryDetail)
    }
}