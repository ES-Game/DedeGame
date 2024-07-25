package com.dede.dedegame.presentation.widget.gridRecyclerview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Display;
import android.view.Surface;
import android.view.WindowManager;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SquareGridLayoutManager extends GridLayoutManager {

    private float mWhRatio = 1f;
    private int mSpace = 0;
    private Context mContext;

    public SquareGridLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public SquareGridLayoutManager(Context context, int spanCount, int space) {
        super(context, spanCount);
        mSpace = space;
        mContext = context;
    }

    public SquareGridLayoutManager(Context context, int spanCount, int orientation, boolean reverseLayout) {
        super(context, spanCount, orientation, reverseLayout);
    }

    @Override
    public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state, int widthSpec, int heightSpec) {
        super.onMeasure(recycler, state, widthSpec, heightSpec);
        Display display = ((WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        if (display.getRotation() == Surface.ROTATION_0) {
            setMeasuredDimension(widthSpec, heightSpec);
        } else if (display.getRotation() == Surface.ROTATION_90) {
            setMeasuredDimension(widthSpec, heightSpec);
            //setMeasuredDimension((heightSpec * 4 / 3  + 1) + mSpace, heightSpec);
        } else if (display.getRotation() == Surface.ROTATION_180) {
            setMeasuredDimension(widthSpec, heightSpec);
        } else if (display.getRotation() == Surface.ROTATION_270) {
            setMeasuredDimension(widthSpec, heightSpec);
            //setMeasuredDimension((heightSpec * 4 / 3  + 1) + mSpace, heightSpec);
        }
    }
}
