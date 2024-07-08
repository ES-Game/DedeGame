package com.dede.dedegame.presentation.common

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.dede.dedegame.presentation.story_cover.groups.CoverStoryViewType.Companion.LATEST_CHAPTER

class CustomItemDecoration(
    context: Context,
    verticalDirectSpaceResId: Int,
    horizontalDirectSpaceResId: Int,
) : RecyclerView.ItemDecoration() {

    private val leftSpace = context.resources.getDimensionPixelSize(horizontalDirectSpaceResId)
    private val rightSpace = context.resources.getDimensionPixelSize(horizontalDirectSpaceResId)
    private val topSpace = context.resources.getDimensionPixelSize(verticalDirectSpaceResId)
    private val bottomSpace = context.resources.getDimensionPixelSize(verticalDirectSpaceResId)

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val position = parent.getChildAdapterPosition(view)
        val itemCount = parent.adapter?.itemCount ?: 0
        val viewType = parent.adapter?.getItemViewType(position)
        if (viewType == LATEST_CHAPTER) {
            outRect.left = leftSpace
            outRect.right = rightSpace
            outRect.top = 0
            outRect.bottom = 0
        } else {
            if (position == 0) {
                outRect.left = leftSpace
                outRect.right = rightSpace
                outRect.top = topSpace
                outRect.bottom = bottomSpace / 2
            } else if (position == itemCount - 1) {
                outRect.left = leftSpace
                outRect.right = rightSpace
                outRect.top = topSpace / 2
                outRect.bottom = bottomSpace
            } else {
                outRect.left = leftSpace
                outRect.right = rightSpace
                outRect.top = topSpace / 2
                outRect.bottom = bottomSpace / 2
            }
        }
    }
}