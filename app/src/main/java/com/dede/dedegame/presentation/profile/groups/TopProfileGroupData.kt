package com.dede.dedegame.presentation.profile.groups

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import com.bumptech.glide.Glide
import com.dede.dedegame.R
import com.dede.dedegame.domain.model.User
import com.quangph.base.mvp.IPresenter
import com.quangph.base.view.recyclerview.adapter.BaseRclvHolder
import com.quangph.base.view.recyclerview.adapter.group.GroupData
import com.quangph.base.view.recyclerview.adapter.group.GroupRclvVH

class TopProfileGroupData(data: User?) :
    GroupData<User>(data) {
    var mPresenter: IPresenter? = null

    var onClickTopInfoItem: OnClickTopInfoItem? = null

    override fun getDataInGroup(position: Int): Any? {
        return data
    }

    override fun getCount(): Int {
        return 1
    }

    override fun getItemViewType(positionInGroup: Int): Int {
        return ItemViewType.ITEM_INFORMATION
    }

    override fun onCreateVH(itemView: View, viewType: Int): BaseRclvHolder<*>? {
        Log.i("onCreateVH tablayout", "")
        if (viewType == ItemViewType.ITEM_INFORMATION) {
            return TopInfoVH(itemView, this)
        }
        return null
    }


    override fun getLayoutResource(viewType: Int): Int {
        if (viewType == ItemViewType.ITEM_INFORMATION) {
            return R.layout.item_profile_top_info
        }
        return INVALID_RESOURCE
    }

    private class TopInfoVH(itemView: View, val topProfileGroupData: TopProfileGroupData) :
        GroupRclvVH<User, TopProfileGroupData>(itemView) {

        private var imvAvatar: ImageView
        private var txtName: TextView
        private var txtEmail: TextView

        init {
            imvAvatar = itemView.findViewById(R.id.imvAvatar)
            txtName = itemView.findViewById(R.id.txtName)
            txtEmail = itemView.findViewById(R.id.txtEmail)
        }

        override fun onBind(vhData: User?) {
            super.onBind(vhData)
            vhData?.let {
                txtName.text = it.name
                txtEmail.text = it.email
                clickOn(itemView){
                    topProfileGroupData.onClickTopInfoItem?.onClickViewProfileDetail()
                }
            }
        }

    }

    interface OnClickTopInfoItem {
        fun onClickViewProfileDetail()
    }
}