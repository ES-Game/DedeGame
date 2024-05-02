package com.quangph.jetpack.view.recyclerview.decor;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Pham Hai Quang on 12/2/2019.
 */
public class BottomPaddingDecorator extends RecyclerView.ItemDecoration {
    private final int mEndPaddingItem;
    private Drawable mDrawable;
    private Rect mDrawableBound;

    public BottomPaddingDecorator(int endPaddingItem) {
        this(null, endPaddingItem);
    }

    public BottomPaddingDecorator(@Nullable Drawable drawable, int endPaddingItem) {
        this.mEndPaddingItem = endPaddingItem;
        mDrawable = drawable;
        if (mDrawable != null) {
            mDrawableBound = new Rect();
        }
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent,
                               @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        // Apply offset only to end item
        if (parent.getChildAdapterPosition(view) == (parent.getAdapter().getItemCount() - 1)) {
            if (mDrawable != null) {
                outRect.bottom += mDrawable.getIntrinsicHeight();
            }
            outRect.bottom += mEndPaddingItem;
        }
    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDraw(c, parent, state);
        if (mDrawable != null) {
            View child = parent.getChildAt(parent.getChildCount() - 1);
            if (child != null && parent.getChildAdapterPosition(child) == (parent.getAdapter().getItemCount() - 1)) {
                getDividerOffset(mDrawableBound, child, parent, state);
                mDrawable.setBounds(mDrawableBound);
                mDrawable.draw(c);
            }
        }
    }

    public void getDividerOffset(@NonNull Rect rect, @NonNull View view, @NonNull RecyclerView parent,
                                 @NonNull RecyclerView.State state) {
        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) view.getLayoutParams();
        int dividerLeft = parent.getPaddingLeft();
        int dividerTop = view.getBottom() + params.bottomMargin;
        int dividerRight = parent.getWidth() - parent.getPaddingRight();
        int dividerBottom = dividerTop + mEndPaddingItem + mDrawable.getIntrinsicHeight();
        rect.set(dividerLeft, dividerTop, dividerRight, dividerBottom);
    }
}
