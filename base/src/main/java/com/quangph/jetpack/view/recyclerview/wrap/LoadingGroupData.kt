package com.quangph.jetpack.view.recyclerview.wrap

import android.view.View
import com.quangph.base.R
import com.quangph.base.view.recyclerview.adapter.BaseRclvHolder
import com.quangph.base.view.recyclerview.adapter.group.GroupData

class LoadingGroupData: GroupData<Unit?>(null) {

    companion object {
        val LOADING_TYPE = 999456
    }

    override fun getDataInGroup(p0: Int): Unit? {
        return null
    }

    override fun getCount(): Int {
        return 1
    }

    override fun onCreateVH(itemView: View?, type: Int): BaseRclvHolder<*>? {
        return if (type == LOADING_TYPE) {
            LoadingVH(itemView!!)
        } else {
            null
        }
    }

    override fun getItemViewType(positionInGroup: Int): Int {
        return LOADING_TYPE
    }

    override fun getLayoutResource(viewType: Int): Int {
        if (viewType == LOADING_TYPE) {
            return R.layout.item_loading
        }
        return super.getLayoutResource(viewType)
    }

    private class LoadingVH(itemView: View) : BaseRclvHolder<Unit?>(itemView)
}