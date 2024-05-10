package com.dede.dedegame.presentation.home.fragments.home_comic.groups

import android.util.Log
import android.view.View
import android.widget.TextView
import com.quangph.base.mvp.IPresenter
import com.quangph.base.view.recyclerview.adapter.BaseRclvHolder
import com.quangph.base.view.recyclerview.adapter.group.GroupData
import com.quangph.base.view.recyclerview.adapter.group.GroupRclvVH
import com.dede.dedegame.R
import com.quangph.dedegame.domain.model.StoryDetail


class CategoryTitleGroupData(title: String?) :
    GroupData<String?>(title) {
    var mPresenter: IPresenter? = null

    var onClickStoryItem: OnClickStoryItem? = null


    override fun getDataInGroup(position: Int): String? {
        return data
    }

    override fun getCount(): Int {
        return if (data != null) 1 else 0
    }

    override fun getItemViewType(positionInGroup: Int): Int {
        return HomeComicItemViewType.CATEGORY_TITLE
    }

    override fun onCreateVH(itemView: View, viewType: Int): BaseRclvHolder<*>? {
        Log.i("onCreateVH_RANK", viewType.toString())
        if (viewType == HomeComicItemViewType.CATEGORY_TITLE) {
            Log.i("onCreateVH", "RANK")
            return CategoryTitleVH(itemView, this)
        }
        return null
    }


    override fun getLayoutResource(viewType: Int): Int {
        return if (viewType == HomeComicItemViewType.CATEGORY_TITLE) {
            R.layout.item_category_title
        } else {
            INVALID_RESOURCE
        }
    }


    private class CategoryTitleVH(itemView: View, val categoryTitleGroup: CategoryTitleGroupData) :
        GroupRclvVH<String?, CategoryTitleGroupData>(itemView) {


        private var tvCategoryName: TextView

        init {
            tvCategoryName =itemView.findViewById(R.id.tvCategoryName)
        }

        override fun onBind(vhData: String?) {
            super.onBind(vhData)
            tvCategoryName.text = vhData

        }

    }

    interface OnClickStoryItem {
        fun onClickStoryItem(item: StoryDetail)
    }
}