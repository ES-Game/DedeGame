package com.quangph.base.view.recyclerview.kt

import android.view.View
import com.quangph.base.view.recyclerview.adapter.BaseRclvHolder

/**
 * Created by QuangPH on 2020-03-03.
 */
class KVH<T>(val containerView: View, private val binder: IOnBind<T>) : BaseRclvHolder<T>(containerView) {

    init {
        binder.viewHolder = this
        binder.onInitView(containerView)
    }

    override fun onBind(vhData: T) {
        super.onBind(vhData)
        binder.onBind(containerView, vhData)
    }

    override fun onBind(vhData: T, payloads: MutableList<Any>) {
        super.onBind(vhData, payloads)
        binder.onBind(containerView, vhData, payloads)
    }
}