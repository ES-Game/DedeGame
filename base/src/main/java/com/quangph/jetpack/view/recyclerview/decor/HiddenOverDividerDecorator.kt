package com.quangph.jetpack.view.recyclerview.decor

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class HiddenOverDividerDecorator(context: Context, resId: Int) : OverDividerDecorator(context, resId){
    var positionHidden = 0
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)
        val itemCount = parent.adapter?.itemCount ?: 0
        if (position == positionHidden || itemCount == 0 || position == itemCount - 1) {
            outRect.set(0,0,0,0)
        } else {
            super.getItemOffsets(outRect, view, parent, state)
        }
    }

    override fun getDividerOffset(rect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        if(parent.getChildAdapterPosition(view) == positionHidden) {
            rect.bottom = rect.top
        } else {
            super.getDividerOffset(rect, view, parent, state)
        }
    }
}