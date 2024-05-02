package com.quangph.jetpack.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.quangph.base.R;

/**
 * Auto add child view to grid view. Child view will be inflated from xml by using item_layout attr
 * Created by QuangPH on 2020-03-10.
 */
public class AutoGridLayout extends ViewGroup {

    private int mColumnCount;
    private int mItemCount;
    private int mItemLayoutId;
    private int mItemMarginStart;
    private int mItemMarginTop;
    private int mItemMarginEnd;
    private int mItemMarginBottom;

    public AutoGridLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
        for (int i = 0; i < mItemCount; i++) {
            addItemView(i);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = 0;
        int itemWidth = width / mColumnCount;
        int currRow = -1;
        View child;
        for(int i = 0; i < getChildCount(); i++) {
            child = getChildAt(i);
            LayoutParams params = (LayoutParams) child.getLayoutParams();
            //int heightSpec = MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(heightMeasureSpec), MeasureSpec.UNSPECIFIED);
            int heightSpec = getChildMeasureSpec(heightMeasureSpec,
                    getPaddingTop() + getPaddingBottom(),
                    child.getLayoutParams().height);
            child.measure(MeasureSpec.makeMeasureSpec(itemWidth - params.leftMargin - params.rightMargin, MeasureSpec.EXACTLY), heightSpec);
            if (params.rowIndex > currRow) {
                currRow = params.rowIndex;
                height += child.getMeasuredHeight() + params.topMargin + params.bottomMargin;
            }
        }

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (!changed) return;
        View child;
        int currLeft = 0;
        int currTop = 0;
        int maxColIndex = mColumnCount - 1;
        for (int i = 0; i < getChildCount(); i++) {

            child = getChildAt(i);
            Log.e("AutoGrid", " Top: " + currTop + " " + t  + " " + child.getMeasuredHeight() + " " + changed);
            LayoutParams params = (LayoutParams) child.getLayoutParams();
            child.layout(currLeft + params.leftMargin, currTop + params.topMargin,
                    currLeft + params.leftMargin + child.getMeasuredWidth(),
                    currTop + params.topMargin + child.getMeasuredHeight());
            if (params.colIndex == maxColIndex) {
                currLeft = 0;
                currTop += child.getMeasuredHeight() + params.topMargin + params.bottomMargin;
            } else {
                currLeft += child.getMeasuredWidth() + params.leftMargin + params.rightMargin;
            }
        }
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(this.getContext(), attrs);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected LayoutParams generateLayoutParams(
            ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return super.checkLayoutParams(p) && (p instanceof LayoutParams);
    }

    public void setOnItemClickedListener(final IOnItemClickedListener listener) {
        for (int i = 0; i < getChildCount(); i++) {
            final int position = i;
            getChildAt(i).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClicked(v, position);
                }
            });
        }
    }

    private void init(AttributeSet attrs) {
        TypedArray ta = getContext().getTheme().obtainStyledAttributes(attrs,
                R.styleable.AutoGridLayout, 0, 0);
        mColumnCount = ta.getInt(R.styleable.AutoGridLayout_auto_gridlayout_column_count, 0);
        mItemCount = ta.getInt(R.styleable.AutoGridLayout_auto_gridlayout_item_count, 0);
        mItemLayoutId = ta.getResourceId(R.styleable.AutoGridLayout_auto_gridlayout_item_layout, -1);
        mItemMarginStart = ta.getDimensionPixelSize(R.styleable.AutoGridLayout_auto_gridlayout_item_margin_start, 0);
        mItemMarginEnd = ta.getDimensionPixelSize(R.styleable.AutoGridLayout_auto_gridlayout_item_margin_end, 0);
        mItemMarginTop = ta.getDimensionPixelSize(R.styleable.AutoGridLayout_auto_gridlayout_item_margin_top, 0);
        mItemMarginBottom = ta.getDimensionPixelSize(R.styleable.AutoGridLayout_auto_gridlayout_item_margin_bottom, 0);
        ta.recycle();
    }

    private void addItemView(int index) {
        View v = LayoutInflater.from(getContext()).inflate(mItemLayoutId, this, false);
        LayoutParams params = (LayoutParams) v.getLayoutParams();
        params.width = LayoutParams.MATCH_PARENT;
        //params.height = LayoutParams.WRAP_CONTENT;
        params.colIndex = index % mColumnCount;
        params.rowIndex = index / mColumnCount;
        params.leftMargin += mItemMarginStart;
        params.topMargin += mItemMarginTop;
        params.rightMargin += mItemMarginEnd;
        params.bottomMargin += mItemMarginBottom;
        v.setLayoutParams(params);
        addView(v);
    }


    public static class LayoutParams extends MarginLayoutParams {
        int colIndex;
        int rowIndex;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }

        @TargetApi(Build.VERSION_CODES.KITKAT)
        public LayoutParams(LayoutParams source) {
            super(source);
        }

    }

    public interface IOnItemClickedListener {
        void onClicked(View view, int position);
    }
}
