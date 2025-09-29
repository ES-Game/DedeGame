package com.dede.dedegame.presentation.home.fragments.home_comic.groups

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.dede.dedegame.R
import com.dede.dedegame.domain.model.Category
import com.dede.dedegame.domain.model.Story
import com.dede.dedegame.extension.loadImageFromUrl
import com.dede.dedegame.extension.loadImageFromUrlReady
import com.dede.dedegame.presentation.widget.RoundedTextView
import com.quangph.base.mvp.IPresenter
import com.quangph.base.view.recyclerview.adapter.BaseRclvHolder
import com.quangph.base.view.recyclerview.adapter.group.GroupData
import com.quangph.base.view.recyclerview.adapter.group.GroupRclvVH


class CategoryStoryGroupData(category: Category?) :
    GroupData<Category?>(category) {
    var mPresenter: IPresenter? = null

    var onClickStoryItem: OnClickStoryItem? = null


    override fun getDataInGroup(position: Int): Any? {
        if (position == 0) {
            return data
        }
        return data?.stories!!.get(position - 1)
    }

    override fun getCount(): Int {
        return (data?.stories?.size ?: 0) + 1
    }

    override fun getItemViewType(positionInGroup: Int): Int {
        if (positionInGroup == 0) {
            return HomeComicItemViewType.CATEGORY_TITLE
        }
        return HomeComicItemViewType.CATEGORY
    }

    override fun onCreateVH(itemView: View, viewType: Int): BaseRclvHolder<*>? {
        Log.i("onCreateVH_RANK", viewType.toString())
        if (viewType == HomeComicItemViewType.CATEGORY) {
            Log.i("onCreateVH", "RANK")
            return GridStoryVH(itemView, this)
        }

        if (viewType == HomeComicItemViewType.CATEGORY_TITLE) {
            return CategoryNameViewHolder(itemView, this)
        }
        return null
    }


    override fun getLayoutResource(viewType: Int): Int {
        return if (viewType == HomeComicItemViewType.CATEGORY) {
            R.layout.item_story_grid_layout
        } else if (viewType == HomeComicItemViewType.CATEGORY_TITLE) {
            return R.layout.item_category_title
        } else {
            INVALID_RESOURCE
        }
    }


    class GridStoryVH(itemView: View, val listStoryGroupData: CategoryStoryGroupData) :
        GroupRclvVH<Story, CategoryStoryGroupData>(itemView) {

        private var ivThumb: ImageView

        private var tvDes: TextView
        private val tvRankIndex by lazy { itemView.findViewById<RoundedTextView>(R.id.tvRankIndex) }
        private val tvLiked by lazy { itemView.findViewById<TextView>(R.id.tvLiked) }
        private val tvViewed by lazy { itemView.findViewById<TextView>(R.id.tvViewed) }
        init {

            ivThumb = itemView.findViewById(R.id.ivThumb)

            tvDes = itemView.findViewById(R.id.tvDesc)


        }

        override fun onBind(vhData: Story?) {
            super.onBind(vhData)
            clickOn(itemView) {
                vhData?.id?.let {
                    listStoryGroupData.onClickStoryItem?.onClickStoryItem(it)
                }
            }
            tvRankIndex.visibility = View.GONE
            vhData?.let { story ->
                tvDes.text = story.title
                ivThumb.loadImageFromUrlReady(story.urlImage)
                tvLiked.text = story.featured.toString()
                tvViewed.text = story.views.toString()
            }
        }
    }

    class CategoryNameViewHolder(itemView: View, val listStoryGroupData: CategoryStoryGroupData) :
        GroupRclvVH<Category, CategoryStoryGroupData>(itemView) {

        private var tvName: TextView


        init {
            tvName = itemView.findViewById(R.id.tvCategoryName)
        }

        override fun onBind(vhData: Category) {
            super.onBind(vhData)
            tvName.text = vhData.name
            clickOn(itemView) {
                listStoryGroupData.onClickStoryItem?.onClickCategoryViewMore(vhData)
            }
        }

    }

    interface OnClickStoryItem {
        fun onClickStoryItem(id: Int)
        fun onClickCategoryViewMore(category: Category)
    }
}