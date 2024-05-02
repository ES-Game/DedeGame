package com.quangph.jetpack.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

/**
 * ViewPager without swiping
 * Created by Pham Hai Quang on 2/1/2019.
 */
public class NonSwipeViewpager extends ViewPager {

    private boolean mEnableSwipe;

    public NonSwipeViewpager(@NonNull Context context) {
        super(context);
    }

    public NonSwipeViewpager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mEnableSwipe) {
            return super.onTouchEvent(event);
        }

        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (mEnableSwipe) {
            return super.onInterceptTouchEvent(event);
        }

        return false;
    }

    public void enableSwipe(boolean enabled) {
        mEnableSwipe = enabled;
    }
}
