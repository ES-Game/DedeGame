package com.dede.dedegame.presentation.home.fragments.home.groups

import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.dede.dedegame.R
import com.dede.dedegame.domain.model.home.Article
import com.quangph.base.mvp.IPresenter
import com.quangph.base.view.recyclerview.adapter.BaseRclvHolder
import com.quangph.base.view.recyclerview.adapter.group.GroupData
import com.quangph.base.view.recyclerview.adapter.group.GroupRclvVH


class NewsGroupData(listArticle: List<Article>?) :
    GroupData<List<Article>>(listArticle) {
    var mPresenter: IPresenter? = null

    var onClickNewsListener: OnClickNewsListener? = null


    override fun getDataInGroup(position: Int): Any? {
        return data[position]
    }

    override fun getCount(): Int {
        val size = if (data != null) data.size else 0
        Log.i("Size data", size.toString())
        return size
    }

    override fun getItemViewType(positionInGroup: Int): Int {
        return ItemViewType.TAB_NEWS
    }

    override fun onCreateVH(itemView: View, viewType: Int): BaseRclvHolder<*>? {
        Log.i("onCreateVH_LIST_STORY", viewType.toString())
        if (viewType == ItemViewType.TAB_NEWS) {
            Log.i("onCreateVH", "LIST_STORY")
            return NewsItemVH(itemView, this)
        }
        return null
    }


    override fun getLayoutResource(viewType: Int): Int {
        return if (viewType == ItemViewType.TAB_NEWS) {
            R.layout.item_home_story
        } else {
            INVALID_RESOURCE
        }
    }


    private class NewsItemVH(itemView: View, val newsGroupData: NewsGroupData) :
        GroupRclvVH<Article?, NewsGroupData>(itemView) {

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

        override fun onBind(vhData: Article?) {
            super.onBind(vhData)
            clickOn(itemView) {
                if (vhData != null) {
                    groupData.onClickNewsListener?.onClickNewsItem(vhData)
                }
            }
            vhData?.let { article ->
                Glide
                    .with(ivThumb.context)
                    .load(article.image)
                    .centerCrop()
                    .into(ivThumb);
                tvName.text = article.title
                tvDes.text = article.description
                tvCreatedAt.text = article.date

                tvDes.post {
                    val maxLines = getMaxLinesForHeight(tvDes, tvDes.height)
                    tvDes.setMaxLines(maxLines)
                    tvDes.ellipsize = TextUtils.TruncateAt.END
                }
            }

        }

        private fun getMaxLinesForHeight(textView: TextView, height: Int): Int {
            val textPaint = textView.paint
            val lineHeight = textPaint.getFontMetrics(null)
            return (height / lineHeight).toInt()
        }

    }

    interface OnClickNewsListener {
        fun onClickNewsItem(item: Article)
    }
}