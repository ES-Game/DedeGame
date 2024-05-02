package com.quangph.base.view.recyclerview.kt

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.quangph.base.view.SafeClicked

/**
 * Created by QuangPH on 2020-03-03.
 */
abstract class KRclvVHInfo<T> : IOnBind<T> {

    private val layoutId: Int

    constructor(): this(-1)

    constructor(layoutId: Int) {
        this.layoutId = layoutId
    }

    override lateinit var viewHolder: RecyclerView.ViewHolder
    override fun onInitView(itemView: View) {}

    open fun createItemView(layoutInflater: LayoutInflater, viewGroup: ViewGroup): View {
        return layoutInflater.inflate(layoutId, viewGroup, false)
    }

    override fun onBind(itemView: View, data: T) {}

    override fun onBind(itemView: View, data: T, payload: MutableList<Any>) {}

    fun adapterPosition(): Int {
        return viewHolder.adapterPosition
    }

    fun clickOn(itemView: View, listener: ()-> Unit) {
        itemView.setOnClickListener(object : SafeClicked() {
            override fun onSafeClicked(view: View?) {
                if (viewHolder.adapterPosition > -1) {
                    listener.invoke()
                }
            }
        })
    }
}