package com.dede.dedegame.presentation.profile.groups

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.dede.dedegame.R
import com.dede.dedegame.domain.model.ProfileSettingModel
import com.quangph.base.mvp.IPresenter
import com.quangph.base.view.recyclerview.adapter.BaseRclvHolder
import com.quangph.base.view.recyclerview.adapter.group.GroupData
import com.quangph.base.view.recyclerview.adapter.group.GroupRclvVH


class ListMenuGroupData(listMenu: List<ProfileSettingModel>?) :
    GroupData<List<ProfileSettingModel>>(listMenu) {
    var mPresenter: IPresenter? = null

    var onClickItemListener: OnClickItemListener? = null

    override fun getDataInGroup(position: Int): Any {
        return data[position]
    }

    override fun getCount(): Int {
        val size = if (data != null) data.size else 0
        return size
    }

    override fun getItemViewType(positionInGroup: Int): Int {
        return ItemViewType.ITEM_MENU
    }

    override fun onCreateVH(itemView: View, viewType: Int): BaseRclvHolder<*>? {
        if (viewType == ItemViewType.ITEM_MENU) {
            return MenuProfileVH(itemView, this)
        }
        return null
    }


    override fun getLayoutResource(viewType: Int): Int {
        return if (viewType == ItemViewType.ITEM_MENU) {
            R.layout.item_profile_menu
        } else {
            INVALID_RESOURCE
        }
    }

    private class MenuProfileVH(
        itemView: View,
        val listMenuGroupData: ListMenuGroupData
    ) :
        GroupRclvVH<ProfileSettingModel?, ListMenuGroupData>(itemView) {

        private var imvIcon: ImageView = itemView.findViewById(R.id.imvIcon)
        private var txtMenuName: TextView = itemView.findViewById(R.id.txtMenuName)

        override fun onBind(vhData: ProfileSettingModel?) {
            super.onBind(vhData)
            vhData?.let { menu ->
                txtMenuName.text = menu.name
                Glide.with(itemView.context).load(menu.icon).into(imvIcon)
                clickOn(itemView) {
                    listMenuGroupData.onClickItemListener?.onClickMenuItem(
                        menu,
                        adapterPosition
                    )
                }
            }
        }
    }

    interface OnClickItemListener {
        fun onClickMenuItem(item: ProfileSettingModel, position: Int)
    }
}