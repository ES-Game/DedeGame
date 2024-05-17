package com.dede.dedegame.presentation.common

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class CustomDividerItemDecoration(
    color: Int,
    private val dividerHeight: Int,
    private val positionsToHideDivider: IntArray,
    private val marginStart: Int,
    private val marginEnd: Int
) : RecyclerView.ItemDecoration() {

    private val paint: Paint = Paint().apply {
        this.color = color
        style = Paint.Style.FILL
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val position = parent.getChildAdapterPosition(child)

            if (!shouldHideDivider(position)) {
                val params = child.layoutParams as RecyclerView.LayoutParams
                val left = parent.paddingLeft + marginStart
                val right = parent.width - parent.paddingRight - marginEnd
                val top = child.bottom + params.bottomMargin
                val bottom = top + dividerHeight

                c.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), paint)
            }
        }
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)
        if (!shouldHideDivider(position)) {
            outRect.bottom = dividerHeight
        } else {
            outRect.bottom = 0
        }
    }

    private fun shouldHideDivider(position: Int): Boolean {
        return positionsToHideDivider.contains(position)
    }
}