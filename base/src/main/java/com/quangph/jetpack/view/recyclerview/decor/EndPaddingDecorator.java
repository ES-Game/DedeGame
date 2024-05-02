package com.quangph.jetpack.view.recyclerview.decor;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Pham Hai Quang on 10/10/2019.
 */
public class EndPaddingDecorator extends RecyclerView.ItemDecoration {
    private final int mEndPaddingItem;

    public EndPaddingDecorator(int endPaddingItem) {
        this.mEndPaddingItem = endPaddingItem;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent,
                               @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        // Apply offset only to end item
        if (parent.getChildAdapterPosition(view) == (parent.getAdapter().getItemCount() - 1)) {
            outRect.right += mEndPaddingItem;
        }
    }
}
