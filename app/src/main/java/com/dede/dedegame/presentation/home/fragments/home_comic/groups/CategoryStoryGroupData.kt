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


class CategoryStoryGroupData(category: Category) :
    GroupData<Category>(category) {
    var mPresenter: IPresenter? = null

    var onClickStoryItem: OnClickStoryItem? = null


    override fun getDataInGroup(position: Int): Any? {
        if (position == 0) {
            return data.name
        }
        return data.stories!!.get(position -1)
    }

    override fun getCount(): Int {
        return (data.stories?.size ?: 0) + 1
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

        init {

            ivThumb = itemView.findViewById(R.id.ivThumb)

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
                tvDes.text = story.title

            }

        }


    }

    class CategoryNameViewHolder(itemView: View, val listStoryGroupData: CategoryStoryGroupData) :
        GroupRclvVH<String, CategoryStoryGroupData>(itemView) {

        private var tvName: TextView


        init {
            tvName = itemView.findViewById(R.id.tvCategoryName)
        }

        override fun onBind(vhData: String) {
            super.onBind(vhData)
                tvName.text = vhData

        }

    }

    interface OnClickStoryItem {
        fun onClickStoryItem(item: StoryDetail)
    }
}