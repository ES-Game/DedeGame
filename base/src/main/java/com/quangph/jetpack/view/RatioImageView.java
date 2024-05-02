package com.quangph.jetpack.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

import com.quangph.base.R;


/**
 * Created by Pham Hai Quang on 5/14/2019.
 */
public class RatioImageView extends AppCompatImageView {

    private float mWidthRatio;
    private float mHeightRatio;

    public RatioImageView(Context context) {
        super(context);
    }

    public RatioImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int originalWidth = MeasureSpec.getSize(widthMeasureSpec);
        int originalHeight = MeasureSpec.getSize(heightMeasureSpec);
        if (originalHeight == 0) {
            int newH = (int) ((float)originalWidth * mHeightRatio/ mWidthRatio);
            super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(newH, MeasureSpec.EXACTLY));
        } else if (originalWidth == 0){
            int newW = (int) ((mWidthRatio / mHeightRatio) * originalHeight);
            super.onMeasure(MeasureSpec.makeMeasureSpec(newW, MeasureSpec.EXACTLY), heightMeasureSpec);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    /**
     * ratio = width:height
     * @param ratio
     */
    public void setRatio(String ratio) {
        extractRatio(ratio);
    }

    private void init(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs,
                R.styleable.RatioImageView);
        String ratioDes = typedArray.getString(R.styleable.RatioImageView_image_ratio);
        if (ratioDes != null) {
            extractRatio(ratioDes);
        }

        typedArray.recycle();
    }

    private void extractRatio(String ratioStr) {
        String[] splits = ratioStr.split(":");
        mWidthRatio = Float.valueOf(splits[0]);
        mHeightRatio = Float.valueOf(splits[1]);
    }
}
