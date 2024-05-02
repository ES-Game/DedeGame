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
 * Created by Pham Hai QUANG on 9/21/2016.
 */
public class DividerDecorator extends RecyclerView.ItemDecoration {

    private Drawable mDivider;
    private int mLeft, mTop, mRight, mBottom;
    private Rect mDividerOutRect = new Rect();
    private Boolean mShowLast = false;

    public DividerDecorator() {
    }

    public DividerDecorator(Context context, int resId) {
        mDivider = ContextCompat.getDrawable(context, resId);
        if (mDivider != null) {
            mDivider.setBounds(0, 0, mDivider.getIntrinsicWidth(), mDivider.getIntrinsicHeight());
        }
    }

    public DividerDecorator(Drawable drawable) {
        mDivider = drawable;
    }

    public DividerDecorator(Drawable drawable, Boolean showLast) {
        mDivider = drawable;
        mShowLast = showLast;
    }

    public DividerDecorator(Context context, int resId, Boolean showLast) {
        mDivider = ContextCompat.getDrawable(context, resId);
        mShowLast = showLast;
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
        if (parent.getChildAdapterPosition(view) == parent.getAdapter().getItemCount() - 1) {
            return;
        }
        outRect.bottom = mDivider.getIntrinsicHeight();
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int dividerLeft = parent.getPaddingLeft() + mLeft;
        int dividerRight = parent.getWidth() - parent.getPaddingRight() - mRight;
        int count = parent.getChildCount();
        for (int i = 0; i < count; i++) {
            if(i == count -1 && !mShowLast){
                return;
            }
            View child = parent.getChildAt(i);
            getDividerOffset(mDividerOutRect, child, parent, state);
            if (mDividerOutRect.height() == 0) {
                continue;
            } else {
                mDivider.setBounds(mDividerOutRect.left, mDividerOutRect.top, mDividerOutRect.right, mDividerOutRect.bottom);
                mDivider.draw(c);
            }
            /*RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            int dividerTop = child.getBottom() + params.bottomMargin + mTop;
            int dividerBottom = dividerTop + mDivider.getIntrinsicHeight() - mBottom;
            mDivider.setBounds(dividerLeft, dividerTop, dividerRight, dividerBottom);
            mDivider.draw(c);*/
        }
    }

    public Drawable getDrawable() {
        return mDivider;
    }

    /**
     * Determine bound of divider. This bound decide the position of divider
     *
     * @param rect   bound of divider
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
