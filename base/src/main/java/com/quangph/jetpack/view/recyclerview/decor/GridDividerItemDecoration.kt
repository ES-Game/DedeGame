package com.quangph.jetpack.view.recyclerview.decor

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by QuangPH on 2020-03-11.
 */
class GridDividerItemDecoration(private val space: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView,
                                state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)
        val totalSpanCount = getTotalSpanCount(parent)
        val spanSize = getItemSpanSize(parent, position)

        outRect.top = if (isInFirstRow(position, totalSpanCount, spanSize)) 0 else space / 2
        outRect.left = space / 2
        outRect.right = space / 2
        outRect.bottom = 0
    }

    private fun isInFirstRow(position: Int, totalSpanCount: Int, spanSize: Int): Boolean =
            if (totalSpanCount != spanSize) {
                position % totalSpanCount == 0
            } else {
                true
            }

    private fun getTotalSpanCount(parent: RecyclerView): Int =
            (parent.layoutManager as? GridLayoutManager)?.spanCount ?: 1

    private fun getItemSpanSize(parent: RecyclerView, position: Int): Int =
            (parent.layoutManager as? GridLayoutManager)?.spanSizeLookup?.getSpanSize(position) ?: 1
}