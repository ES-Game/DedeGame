package com.dede.dedegame.presentation.profile.groups

import android.util.Log
import android.view.View
import androidx.appcompat.widget.AppCompatButton
import com.dede.dedegame.R
import com.quangph.base.mvp.IPresenter
import com.quangph.base.view.recyclerview.adapter.BaseRclvHolder
import com.quangph.base.view.recyclerview.adapter.group.GroupData
import com.quangph.base.view.recyclerview.adapter.group.GroupRclvVH

class BottomLogoutGroupData(data: String?) :
    GroupData<String>(data) {
    var mPresenter: IPresenter? = null

    var onClickLogoutItem: OnClickLogoutItem? = null

    override fun getDataInGroup(position: Int): Any? {
        return data
    }

    override fun getCount(): Int {
        return 1
    }

    override fun getItemViewType(positionInGroup: Int): Int {
        return ItemViewType.ITEM_LOGOUT
    }

    override fun onCreateVH(itemView: View, viewType: Int): BaseRclvHolder<*>? {
        Log.i("onCreateVH tablayout", "")
        if (viewType == ItemViewType.ITEM_LOGOUT) {
            return LogoutVH(itemView, this)
        }
        return null
    }


    override fun getLayoutResource(viewType: Int): Int {
        if (viewType == ItemViewType.ITEM_LOGOUT) {
            return R.layout.item_profile_bottom_logout
        }
        return INVALID_RESOURCE
    }

    private class LogoutVH(itemView: View, val bottomLogoutGroupData: BottomLogoutGroupData) :
        GroupRclvVH<String, BottomLogoutGroupData>(itemView) {

        private var btnLogout: AppCompatButton

        init {
            btnLogout = itemView.findViewById(R.id.btnLogout)
        }

        override fun onBind(vhData: String?) {
            super.onBind(vhData)
            clickOn(btnLogout) {
                bottomLogoutGroupData.onClickLogoutItem?.onClickLogoutUser()
            }
        }

    }

    interface OnClickLogoutItem {
        fun onClickLogoutUser()
    }
}