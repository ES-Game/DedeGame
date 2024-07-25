package com.dede.dedegame.presentation.widget.gridRecyclerview

import android.content.Context
import android.graphics.Rect
import android.util.Log
import android.view.Surface
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.recyclerview.widget.RecyclerView


class AutoResizeItemDecoration(
    private val mWidth: Int,
    private val mHeight: Int,
    private val mSpanCount: Int,
    private val mSpace: Int,
    private val mMaxRow: Int
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val layoutParams = view.layoutParams as ViewGroup.MarginLayoutParams
        //val whRatio = layoutParams.width.toFloat() / layoutParams.height

        val position = parent.getChildAdapterPosition(view)
        if (position % mSpanCount == 0) {
            layoutParams.marginStart = mSpace
            layoutParams.leftMargin = mSpace
        } else {
            layoutParams.marginStart = mSpace
            layoutParams.leftMargin = mSpace
        }

        if ((position + 1) % mSpanCount == 0) {
            layoutParams.marginEnd = mSpace
            layoutParams.rightMargin = mSpace
        } else {
            layoutParams.marginEnd = mSpace
            layoutParams.rightMargin = mSpace
        }

        if (position < mSpanCount) {
            layoutParams.topMargin = mSpace
        } else {
            layoutParams.topMargin = mSpace
        }

        if (position >= mSpanCount * (mSpanCount - 1)) {
            layoutParams.bottomMargin = mSpace
        } else {
            layoutParams.bottomMargin = mSpace
        }

        Log.d("AutoResizeItem", "Position = $position, TextureItem width: ${layoutParams.width}, TextureItem height = ${layoutParams.height}, margin top = ${layoutParams.topMargin}, margin left = ${layoutParams.marginStart}, margin bottom = ${layoutParams.bottomMargin}, margin right = ${layoutParams.marginEnd}")

        layoutParams.width = (mWidth - mSpace * (mSpanCount + 1)) / mSpanCount
        val display = (parent.context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
        when {
            display.rotation == Surface.ROTATION_0 -> layoutParams.height =
                (mHeight - mSpace * 2 * (mSpanCount + 1)) / mMaxRow
            display.rotation == Surface.ROTATION_90 -> layoutParams.height =
                (mHeight - mSpace * 2 * (mSpanCount + 1)) / mMaxRow
            display.rotation == Surface.ROTATION_180 -> layoutParams.height =
                (mHeight - mSpace * 2 * (mSpanCount + 1)) / mMaxRow
            display.rotation == Surface.ROTATION_270 -> layoutParams.height =
                (mHeight - mSpace * 2 * (mSpanCount + 1)) / mMaxRow
        }
        view.layoutParams = layoutParams
    }
}