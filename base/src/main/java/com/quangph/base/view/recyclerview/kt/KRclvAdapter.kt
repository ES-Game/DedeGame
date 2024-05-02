package com.quangph.base.view.recyclerview.kt

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.quangph.base.view.recyclerview.adapter.BaseRclvAdapter
import com.quangph.base.view.recyclerview.adapter.BaseRclvHolder

/**
 * Created by QuangPH on 2020-03-03.
 */
abstract class KRclvAdapter : BaseRclvAdapter() {
    override fun getLayoutResource(viewType: Int): Int {
        throw RuntimeException("getLayoutResource() can not be impl")
    }

    override fun onCreateVH(itemView: View, viewType: Int): BaseRclvHolder<*> {
        throw RuntimeException("onCreateVH() can not be impl")
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): BaseRclvHolder<*> {
        val vhInfo = createVHInfo(viewType)
        //val v = LayoutInflater.from(viewGroup.context).inflate(vhInfo!!.layoutId, viewGroup, false)
        val v = vhInfo!!.createItemView(LayoutInflater.from(viewGroup.context), viewGroup)
        return KVH(v, vhInfo)
    }

    abstract fun createVHInfo(viewType: Int): KRclvVHInfo<*>?
}