package com.quangph.jetpack.view.recyclerview.decor;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Pham Hai Quang on 10/10/2019.
 */
public class SpaceItemDecoration extends RecyclerView.ItemDecoration {
    private final int mSpace;
    private int mOrientation;

    public SpaceItemDecoration(int space) {
        this(space, LinearLayoutManager.VERTICAL);
    }

    public SpaceItemDecoration(int space, int orientation) {
        this.mSpace = space;
        mOrientation = orientation;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        if (mOrientation == LinearLayoutManager.VERTICAL) {
            if (parent.getChildAdapterPosition(view) != 0) {
                outRect.top = mSpace;
            }
        } else if (mOrientation == LinearLayoutManager.HORIZONTAL){
            if (parent.getChildAdapterPosition(view) != 0) {
                outRect.left = mSpace;
            }
        }
    }
}
