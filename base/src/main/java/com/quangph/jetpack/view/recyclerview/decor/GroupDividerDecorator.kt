package com.quangph.jetpack.view.recyclerview.decor

import android.content.Context
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.quangph.base.R
import com.quangph.base.view.recyclerview.adapter.group.GroupManager

/**
 * Created by QuangPH on 2020-03-17.
 */
open class GroupDividerDecorator(context: Context, drawable: Drawable, val groupManager: GroupManager):
        DividerDecorator(drawable) {

    val padding = context.resources.getDimensionPixelSize(R.dimen.dimen_16)

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)
        outRect.set(0, 0, 0, 0)
        outRect.bottom = getBottomOffset(position)
        Log.e("ItemOffset", "Called:" + position)
    }

    override fun getDividerOffset(rect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getDividerOffset(rect, view, parent, state)
        val position = parent.getChildAdapterPosition(view)
        rect.bottom = rect.top + getBottomOffset(position)
    }

    private fun getBottomOffset(position: Int): Int {
        val group = groupManager.findGroupDataByAdapterPosition(position)
        return if (group != null) {
            if (position == group.adapterPosition + group.count - 1) {
                padding
            } else {
                0
            }
        } else {
            0
        }
    }
}