package com.dede.dedegame.presentation.home.fragments.home_comic.groups

import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.bumptech.glide.Glide
import com.quangph.base.mvp.IPresenter
import com.quangph.base.view.recyclerview.adapter.BaseRclvHolder
import com.quangph.base.view.recyclerview.adapter.group.GroupData
import com.quangph.base.view.recyclerview.adapter.group.GroupRclvVH
import com.dede.dedegame.R
import com.dede.dedegame.presentation.home.fragments.main_game.groups.ItemViewType
import com.quangph.dedegame.domain.model.Category
import com.quangph.dedegame.domain.model.Story
import com.quangph.dedegame.domain.model.StoryDetail


class CategoriesStoryGroupData(categories: List<Category>?) :
    GroupData<List<Category>>(categories) {
    var mPresenter: IPresenter? = null

    var onClickStoryItem: OnClickStoryItem? = null


    override fun getDataInGroup(position: Int): Any? {
        return data[position]
    }

    override fun getCount(): Int {
        val size = if (data != null) data.size else 0
        return size
    }

    override fun getItemViewType(positionInGroup: Int): Int {
        return HomeComicItemViewType.CATEGORY
    }

    override fun onCreateVH(itemView: View, viewType: Int): BaseRclvHolder<*>? {
        Log.i("onCreateVH_RANK", viewType.toString())
        if (viewType == HomeComicItemViewType.CATEGORY) {
            Log.i("onCreateVH", "RANK")
            return GridStoryVH(itemView, this)
        }
        return null
    }


    override fun getLayoutResource(viewType: Int): Int {
        return if (viewType == HomeComicItemViewType.CATEGORY) {
            R.layout.item_story_grid_layout
        } else {
            INVALID_RESOURCE
        }
    }


    private class GridStoryVH(itemView: View, val listStoryGroupData: CategoriesStoryGroupData) :
        GroupRclvVH<Story, CategoriesStoryGroupData>(itemView) {

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

        override fun onBind(vhData: Story?) {
            super.onBind(vhData)
//            clickOn(itemView) {
//                if (vhData != null) {
//                    groupData.onClickStoryItem?.onClickStoryItem(vhData)
//                }
//            }
            vhData?.let { story ->
                Glide
                    .with(ivThumb.context)
                    .load(story.urlImage)
                    .centerCrop()
                    .into(ivThumb);
                tvName.text = story.title
//                tvDes.text = story.description
//                tvCreatedAt.text = story.createdAt
            }

        }

    }

    class CategoryData {
        var category: Category? = null
    }

    interface OnClickStoryItem {
        fun onClickStoryItem(item: StoryDetail)
    }
}