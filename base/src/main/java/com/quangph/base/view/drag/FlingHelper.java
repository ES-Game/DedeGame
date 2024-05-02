package com.quangph.base.view.drag;

import android.view.View;
import android.widget.Scroller;

import androidx.core.view.ViewCompat;

/**
 * Created by Pham Hai QUANG on 9/17/2018.
 */
public class FlingHelper {
    private View mView;
    private Scroller mScroller;
    private FlingRunnable mFlingRunnable;
    private Runnable mPostOnAnimationRunnable, mEndAnimationRunnable;

    public FlingHelper(View view) {
        mView = view;
        mScroller = new Scroller(mView.getContext());
    }

    public void postOnAnimation(Runnable runnable) {
        mPostOnAnimationRunnable = runnable;
    }

    public void postOnEndAnimation(Runnable runnable) {
        mEndAnimationRunnable = runnable;
    }

    public void onScrollX(int start, int offset) {
        mScroller.startScroll(start, 0, offset, 0);
        if (mScroller.computeScrollOffset()) {
            if (mFlingRunnable == null) {
                mFlingRunnable = new FlingRunnable(mView);
            }
            ViewCompat.postOnAnimation(mView, mFlingRunnable);
        }
    }

    public void onScrollX(int start, int offset, int velocity) {
        if (offset == 0) return;
        int dur = 250;
        if (velocity != 0) {
            dur = Math.abs(offset) / Math.abs(velocity);
        }

        if (dur < 250) {
            dur = 250;
        }
        mScroller.startScroll(start, 0, offset, 0, dur);
        if (mScroller.computeScrollOffset()) {
            if (mFlingRunnable == null) {
                mFlingRunnable = new FlingRunnable(mView);
            }
            ViewCompat.postOnAnimation(mView, mFlingRunnable);
        }
    }

    public void onFlingX(int start, int end, int velocity) {
        mScroller.forceFinished(true);
        int newVelocity = velocity;
        if (velocity < 0) {
            newVelocity = -velocity;
        }
        int minX = Math.min(start, end);
        int maxX = Math.max(start, end);
        mScroller.fling(minX, 0, newVelocity, 0, minX, maxX, 0, 0);
        if (mScroller.computeScrollOffset()) {
            if (mFlingRunnable == null) {
                mFlingRunnable = new FlingRunnable(mView);
            }
            ViewCompat.postOnAnimation(mView, mFlingRunnable);
        }
    }

    private class FlingRunnable implements Runnable {

        private View mView;
        private int mLastX = 0;

        FlingRunnable(View layout) {
            mView = layout;
        }

        @Override
        public void run() {
            if (mScroller.computeScrollOffset()) {
                int offset = mScroller.getCurrX() - mLastX;
                ViewCompat.offsetLeftAndRight(mView, offset);
                mLastX = mScroller.getCurrX();
                if (mPostOnAnimationRunnable != null) {
                    mPostOnAnimationRunnable.run();
                }
                // Post ourselves so that we run on the next animation
                ViewCompat.postOnAnimation(mView, this);
            } else {
                onFlingFinished();
                if (mEndAnimationRunnable != null) {
                    mEndAnimationRunnable.run();
                }
            }
        }

        private void onFlingFinished() {
            mLastX = 0;
            mView.removeCallbacks(this);
        }
    }
}
