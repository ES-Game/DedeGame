package com.dede.dedegame.presentation.common

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CustomItemMainGameDecoration(private val space: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)
        val layoutManager = parent.layoutManager as? GridLayoutManager
        layoutManager?.let {
            val spanSizeLookup = layoutManager.spanSizeLookup
            val spanCount = layoutManager.spanCount
            val spanSize = spanSizeLookup.getSpanSize(position)
            val column = spanSizeLookup.getSpanIndex(position, spanCount)

            if (spanSize == 2) {
                // Các phần tử có spanSize là 2
                outRect.left = space
                outRect.right = space
            } else {
                // Các phần tử có spanSize là 1
                if (column == 0) {
                    // Cột 1
                    outRect.left = space
                    outRect.right = space / 2
                } else {
                    // Cột 2
                    outRect.left = space / 2
                    outRect.right = space
                }
                if (position >= spanCount - 1) {
                    outRect.top = space
                }
            }
        }
    }
}

