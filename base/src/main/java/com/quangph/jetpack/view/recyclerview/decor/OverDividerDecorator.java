package com.quangph.jetpack.view.recyclerview.decor;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by ChungTV on 5/5/20.
 */
public class OverDividerDecorator extends RecyclerView.ItemDecoration {

    private Drawable mDivider;
    private int mLeft, mTop, mRight, mBottom;
    private Rect mDividerOutRect = new Rect();

    public OverDividerDecorator() {
    }

    public OverDividerDecorator(Context context, int resId) {
        mDivider = ContextCompat.getDrawable(context, resId);
    }

    public OverDividerDecorator(Drawable drawable) {
        mDivider = drawable;
    }

    public void setPadding(int left, int top, int right, int bottom) {
        mLeft = left;
        mTop = top;
        mRight = right;
        mBottom = bottom;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if (parent.getChildAdapterPosition(view) == 0) {
            return;
        }

        outRect.top = mDivider.getIntrinsicHeight();
    }

    @Override
    public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        int count = parent.getChildCount();
        for (int i = 0; i < count - 1; i++) {
            View child = parent.getChildAt(i);
            getDividerOffset(mDividerOutRect, child, parent, state);
            if (mDividerOutRect.height() == 0) {
                continue;
            } else {
                mDivider.setBounds(mDividerOutRect.left, mDividerOutRect.top, mDividerOutRect.right, mDividerOutRect.bottom);
                mDivider.draw(c);
            }
        }
    }

    public Drawable getDrawable() {
        return mDivider;
    }

    /**
     * Determine bound of divider. This bound decide the position of divider
     * @param rect bound of divider
     * @param view
     * @param parent
     * @param state
     */
    public void getDividerOffset(@NonNull Rect rect, @NonNull View view, @NonNull RecyclerView parent,
                                 @NonNull RecyclerView.State state) {
        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) view.getLayoutParams();
        int dividerLeft = parent.getPaddingLeft() + mLeft;
        int dividerTop = view.getBottom() + params.bottomMargin + mTop;
        int dividerRight = parent.getWidth() - parent.getPaddingRight() - mRight;
        int dividerBottom = dividerTop + mDivider.getIntrinsicHeight() - mBottom;

        rect.set(dividerLeft, dividerTop, dividerRight, dividerBottom);
    }
}

