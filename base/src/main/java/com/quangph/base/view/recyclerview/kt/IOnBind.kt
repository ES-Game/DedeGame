package com.quangph.base.view.recyclerview.kt

import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by QuangPH on 2020-03-03.
 */
interface IOnBind<T> {
    var viewHolder: RecyclerView.ViewHolder
    fun onInitView(itemView: View)
    fun onBind(itemView: View, data: T)
    fun onBind(itemView: View, data: T, payload: MutableList<Any>)
}