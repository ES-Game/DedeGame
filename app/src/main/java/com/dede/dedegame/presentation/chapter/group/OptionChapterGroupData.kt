package com.dede.dedegame.presentation.chapter.group

import android.content.res.ColorStateList
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.dede.dedegame.R
import com.dede.dedegame.domain.model.OptionChapter
import com.dede.dedegame.presentation.common.DimensUtil
import com.quangph.base.mvp.IPresenter
import com.quangph.base.view.recyclerview.adapter.BaseRclvHolder
import com.quangph.base.view.recyclerview.adapter.group.GroupData
import com.quangph.base.view.recyclerview.adapter.group.GroupRclvVH

class OptionChapterGroupData(listMenuChapter: List<OptionChapter>?) :
    GroupData<List<OptionChapter>>(listMenuChapter) {
    var mPresenter: IPresenter? = null

    var onClickItemListener: OnClickItemListener? = null

    fun menuChapter(): List<OptionChapter>? {
        return data
    }

    override fun getDataInGroup(position: Int): Any? {
        return data[position]
    }

    override fun getCount(): Int {
        val size = if (data != null) data.size else 0
        return size
    }

    override fun getItemViewType(positionInGroup: Int): Int {
        return ItemViewType.ITEM_OPTION_CHAPTER
    }

    override fun onCreateVH(itemView: View, viewType: Int): BaseRclvHolder<*>? {
        if (viewType == ItemViewType.ITEM_OPTION_CHAPTER) {
//            val layoutParams = itemView.layoutParams
//            layoutParams.width = DimensUtil.screenWidth(itemView.context) / 4
//            itemView.layoutParams = layoutParams
            return OptionChaperVH(itemView, this)
        }
        return null
    }


    override fun getLayoutResource(viewType: Int): Int {
        return if (viewType == ItemViewType.ITEM_OPTION_CHAPTER) {
            R.layout.item_menu_option_chapter
        } else {
            INVALID_RESOURCE
        }
    }

    private class OptionChaperVH(itemView: View, val menuEditGroupData: OptionChapterGroupData) :
        GroupRclvVH<OptionChapter?, OptionChapterGroupData>(itemView) {

        private var imvIcon: ImageView = itemView.findViewById(R.id.imvIcon)
        private var tvName: TextView = itemView.findViewById(R.id.tvName)

        override fun onBind(vhData: OptionChapter?) {
            super.onBind(vhData)
            vhData?.let { vhData ->
                imvIcon.setImageResource(vhData.id)
                tvName.visibility = View.GONE
                tvName.text = vhData.name
                if (vhData.enabled) {
                    itemView.isEnabled = true
                    if (vhData.selected) {
                        imvIcon.imageTintList = ColorStateList.valueOf(
                            ContextCompat.getColor(
                                itemView.context,
                                R.color.orange_300
                            )
                        )
                        tvName.setTextColor(
                            ContextCompat.getColor(
                                itemView.context,
                                R.color.orange_300
                            )
                        )
                    } else {
                        imvIcon.imageTintList = ColorStateList.valueOf(
                            ContextCompat.getColor(
                                itemView.context,
                                R.color.white
                            )
                        )
                        tvName.setTextColor(ContextCompat.getColor(itemView.context, R.color.white))
                    }
                } else {
                    itemView.isEnabled = false
                    imvIcon.imageTintList = ColorStateList.valueOf(
                        ContextCompat.getColor(
                            itemView.context,
                            R.color.gray_300
                        )
                    )
                    tvName.setTextColor(ContextCompat.getColor(itemView.context, R.color.gray_300))
                }
                clickOn(itemView) {
                    menuEditGroupData.onClickItemListener?.onClickMenuItem(vhData)
                }

            }
        }
    }

    interface OnClickItemListener {
        fun onClickMenuItem(item: OptionChapter)
    }
}