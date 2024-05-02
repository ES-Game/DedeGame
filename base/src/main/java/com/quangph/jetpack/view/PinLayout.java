package com.quangph.jetpack.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.quangph.base.R;

/**
 * The child with pin attr will has flex width, it mean its width = parent width - (total width of other children)
 * Created by QuangPH on 2020-03-05.
 */
public class PinLayout extends LinearLayout {

    private boolean isDynamicScale = true;

    public PinLayout(Context context) {
        super(context);
        setOrientation(HORIZONTAL);
    }

    public PinLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(attrs);
        setOrientation(HORIZONTAL);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (!isDynamicScale) {
            View pinnableView = findPinableView();
            if (pinnableView != null) {
                LayoutParams params = (LayoutParams) pinnableView.getLayoutParams();
                params.width = 0;
                params.weight = 1;
                pinnableView.setLayoutParams(params);
            }
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthParent = MeasureSpec.getSize(widthMeasureSpec);

        int heightParent = MeasureSpec.getSize(heightMeasureSpec);
        int totalWitdh = 0;
        int height = 0;
        MarginLayoutParams params = (MarginLayoutParams) getLayoutParams();
        widthParent -= getPaddingLeft() + getPaddingRight();// + params.leftMargin + params.rightMargin;
        View child = null;
        for (int i = 0; i < getChildCount(); i++) {
            child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                /*child.measure(MeasureSpec.makeMeasureSpec(widthParent, MeasureSpec.AT_MOST),
                        MeasureSpec.makeMeasureSpec(heightParent, MeasureSpec.AT_MOST));*/
                int widthSpec = getChildMeasureSpec(widthMeasureSpec,
                        getPaddingLeft() + getPaddingRight(),
                        child.getLayoutParams().width);
                int heightSpec = getChildMeasureSpec(heightMeasureSpec,
                        getPaddingTop() + getPaddingBottom(),
                        child.getLayoutParams().height);
                child.measure(widthSpec, heightSpec);
                totalWitdh += child.getMeasuredWidth();
                if (height < child.getMeasuredHeight()) {
                    height = child.getMeasuredHeight();
                }
            }
        }

        if (totalWitdh > widthParent) {
            View pinnableView = findPinableView();
            if (pinnableView != null) {
                int width = pinnableView.getMeasuredWidth();
                int offset = totalWitdh - widthParent;
                totalWitdh -= offset;
                width -= offset;
                int heightSpec = getChildMeasureSpec(heightMeasureSpec,
                        getPaddingTop() + getPaddingBottom(),
                        pinnableView.getLayoutParams().height);
                pinnableView.measure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY), heightSpec);
                if (height < pinnableView.getMeasuredHeight()) {
                    height = pinnableView.getMeasuredHeight();
                }
            }
        } else {
            int currWidth = getLayoutParams().width;
            if (currWidth == ViewGroup.LayoutParams.MATCH_PARENT) {
                View pinnableView = findPinableView();
                if (pinnableView != null) {
                    int width = pinnableView.getMeasuredWidth();
                    int offset = widthParent - totalWitdh;
                    int heightSpec = getChildMeasureSpec(heightMeasureSpec,
                            getPaddingTop() + getPaddingBottom(),
                            pinnableView.getLayoutParams().height);
                    pinnableView.measure(MeasureSpec.makeMeasureSpec(width + offset, MeasureSpec.EXACTLY), heightSpec);
                    if (height < pinnableView.getMeasuredHeight()) {
                        height = pinnableView.getMeasuredHeight();
                    }
                }
                totalWitdh = widthParent;
            }
        }

        if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.EXACTLY) {
            setMeasuredDimension(totalWitdh + getPaddingLeft() + getPaddingRight(),
                    MeasureSpec.getSize(heightMeasureSpec));
        } else {
            if (getLayoutParams().height == ViewGroup.LayoutParams.MATCH_PARENT) {
                setMeasuredDimension(totalWitdh + getPaddingLeft() + getPaddingRight(),
                        MeasureSpec.getSize(heightMeasureSpec));
            } else {
                setMeasuredDimension(totalWitdh + getPaddingLeft() + getPaddingRight(),
                        height + getPaddingTop() + getPaddingBottom());
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

    private View findPinableView() {
        for (int i = 0; i < this.getChildCount(); i++) {
            LayoutParams p = (LayoutParams) this.getChildAt(i).getLayoutParams();
            if (p.isPinable) {
                return this.getChildAt(i);
            }
        }
        return null;
    }

    private void initAttrs(AttributeSet attrs) {
        TypedArray ta = getContext().getTheme().obtainStyledAttributes(attrs,
                R.styleable.PinLayout, 0, 0);
        isDynamicScale = ta.getBoolean(R.styleable.PinLayout_pin_layout_dynamic_scale, true);
    }


    public static class LayoutParams extends LinearLayout.LayoutParams {
        boolean isPinable;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            TypedArray a = c.obtainStyledAttributes(attrs,
                    R.styleable.PinLayout);
            isPinable = a.getBoolean(R.styleable.PinLayout_pin_layout_pin, false);
            a.recycle();
        }

        public LayoutParams(int width, int height, float weight) {
            super(width, height, weight);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }

        @TargetApi(Build.VERSION_CODES.KITKAT)
        public LayoutParams(LinearLayout.LayoutParams source) {
            super(source);
        }

        public LayoutParams(MarginLayoutParams source) {
            super(source);
        }
    }

}
