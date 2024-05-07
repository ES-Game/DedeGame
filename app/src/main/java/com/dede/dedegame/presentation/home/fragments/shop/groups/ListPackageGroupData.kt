package com.dede.dedegame.presentation.home.fragments.shop.groups

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.quangph.base.mvp.IPresenter
import com.quangph.base.view.recyclerview.adapter.BaseRclvHolder
import com.quangph.base.view.recyclerview.adapter.group.GroupData
import com.quangph.base.view.recyclerview.adapter.group.GroupRclvVH
import com.dede.dedegame.R
import com.dede.dedegame.domain.model.Product
import com.quangph.dedegame.domain.model.StoryDetail


class ListPackageGroupData(listStory: List<Product>?) :
    GroupData<List<Product>>(listStory) {
    var mPresenter: IPresenter? = null

    var onClickStoryItem: OnClickStoryItem? = null


    override fun getDataInGroup(position: Int): Any? {
        return data[position]
    }

    override fun getCount(): Int {
        val size = if (data != null) data.size else 0
        Log.i("Size data", size.toString())
        return size
    }

    override fun getItemViewType(positionInGroup: Int): Int {
        return ItemViewType.LIST_PACKAGE
    }

    override fun onCreateVH(itemView: View, viewType: Int): BaseRclvHolder<*>? {
        Log.i("onCreateVH_LIST_STORY", viewType.toString())
        if (viewType == ItemViewType.LIST_PACKAGE) {
            Log.i("onCreateVH", "LIST_STORY")
            return ListStoryVH(itemView, this)
        }

        return null
    }


    override fun getLayoutResource(viewType: Int): Int {
        return if (viewType == ItemViewType.LIST_PACKAGE) {
            R.layout.item_shop_package_system
        } else {
            INVALID_RESOURCE
        }
    }


    private class ListStoryVH(itemView: View, val listStoryGroupData: ListPackageGroupData) :
        GroupRclvVH<Product, ListPackageGroupData>(itemView) {

        private var imvPkgItem: ImageView
        private var textName: TextView
        private var txtPkgDes: TextView
        private var txtPkgValue: TextView

        init {

            imvPkgItem = itemView.findViewById(R.id.imvPkgItem)
            textName = itemView.findViewById(R.id.textName)
            txtPkgDes = itemView.findViewById(R.id.txtPkgDes)
            txtPkgValue = itemView.findViewById(R.id.txtPkgValue)


        }

        override fun onBind(vhData: Product?) {
            super.onBind(vhData)
            clickOn(itemView) {
                if (vhData != null) {
                    groupData.onClickStoryItem?.onClickStoryItem(vhData)
                }
            }
            vhData?.let { product ->
                imvPkgItem.setImageResource(product.getProductImage());
                textName.text = product.getProductName()
                txtPkgDes.text = "" + product.quantity + " " + product.productType.value
                txtPkgValue.text = product.price.toString()
            }

        }

    }

    interface OnClickStoryItem {
        fun onClickStoryItem(item: Product)
    }
}