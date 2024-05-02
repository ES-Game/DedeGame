package com.quangph.jetpack.view.recyclerview.decor;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Pham Hai Quang on 10/17/2019.
 */
public class TopPaddingDecorator extends RecyclerView.ItemDecoration {

    private final int mTopPaddingItem;

    public TopPaddingDecorator(int startPaddingItem) {
        this.mTopPaddingItem = startPaddingItem;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent,
                               @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        // Apply offset only to first item
        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.top += mTopPaddingItem;
        }
    }
}
