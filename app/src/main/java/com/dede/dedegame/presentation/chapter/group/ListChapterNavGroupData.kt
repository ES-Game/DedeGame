package com.dede.dedegame.presentation.chapter.group

import android.util.SparseBooleanArray
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.dede.dedegame.R
import com.dede.dedegame.domain.model.Chapter
import com.quangph.base.mvp.IPresenter
import com.quangph.base.view.recyclerview.adapter.BaseRclvHolder
import com.quangph.base.view.recyclerview.adapter.group.GroupData
import com.quangph.base.view.recyclerview.adapter.group.GroupRclvVH


class ListChapterNavGroupData(listChapter: List<Chapter>?) :
    GroupData<List<Chapter>>(listChapter) {
    var mPresenter: IPresenter? = null

    var onClickItemListener: OnClickItemListener? = null
    private val itemStateArray = SparseBooleanArray()

    fun getItemStateArray(): SparseBooleanArray {
        return itemStateArray
    }

    override fun getDataInGroup(position: Int): Any {
        return data[position]
    }

    override fun getCount(): Int {
        val size = if (data != null) data.size else 0
        return size
    }

    override fun getItemViewType(positionInGroup: Int): Int {
        return ItemViewType.ITEM_CHAPTER_NAV
    }

    override fun onCreateVH(itemView: View, viewType: Int): BaseRclvHolder<*>? {
        if (viewType == ItemViewType.ITEM_CHAPTER_NAV) {
            return ChapterNavVH(itemView, this)
        }
        return null
    }


    override fun getLayoutResource(viewType: Int): Int {
        return if (viewType == ItemViewType.ITEM_CHAPTER_NAV) {
            R.layout.item_dropdown
        } else {
            INVALID_RESOURCE
        }
    }

    private class ChapterNavVH(
        itemView: View,
        val listChapterNavGroupData: ListChapterNavGroupData
    ) :
        GroupRclvVH<Chapter?, ListChapterNavGroupData>(itemView) {

        private var tvChapterName: TextView = itemView.findViewById(R.id.tvChapterName)

        override fun onBind(vhData: Chapter?) {
            super.onBind(vhData)
            vhData?.let { vhData ->
                tvChapterName.text = vhData.title
                if (listChapterNavGroupData.itemStateArray.get(adapterPosition, false)) {
                    tvChapterName.setTextColor(
                        ContextCompat.getColor(
                            tvChapterName.context,
                            R.color.orange_300
                        )
                    )
                } else {
                    tvChapterName.setTextColor(
                        ContextCompat.getColor(
                            tvChapterName.context,
                            R.color.white
                        )
                    )
                }
                clickOn(itemView) {
                    listChapterNavGroupData.onClickItemListener?.onClickMenuItem(
                        vhData,
                        adapterPosition
                    )
                }
            }
        }
    }

    interface OnClickItemListener {
        fun onClickMenuItem(item: Chapter, position: Int)
    }
}