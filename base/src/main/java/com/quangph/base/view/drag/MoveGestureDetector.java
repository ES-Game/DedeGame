package com.quangph.base.view.drag;

import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;

import androidx.core.view.ViewCompat;


/**
 * Move horizontally a view using gesture detector
 * Created by Pham Hai QUANG on 10/4/2018.
 */
public class MoveGestureDetector extends GestureDetector{

    public static final int IDLE = 1;
    public static final int SCROLLING = 2;
    public static final int FLING = 3;
    public static final int PREPARE_SCROLL = 4;

    public interface MyGestureListener{
        boolean onUp(MotionEvent ev);
    }

    public interface IOnGestureListener {
        void onChangeState(int prevState, int currentState);
        void onMove(float leftX);
        void onEnd(boolean isLeft);
    }

    private MoveGestureListener mMyGestureListener;

    public MoveGestureDetector(View view, MoveGestureListener listener) {
        super(view.getContext(), listener);
        mMyGestureListener = listener;
        mMyGestureListener.mView = view;
        mMyGestureListener.init();
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_UP) {
            return super.onTouchEvent(ev) || mMyGestureListener.onUp(ev);
        }

        return super.onTouchEvent(ev);
    }

    public void setGestureListener(IOnGestureListener listener) {
        mMyGestureListener.setGestureListener(listener);
    }

    public void scrollXToLeft() {
        mMyGestureListener.scrollXToLeft();
    }

    public void scrollXToRight() {
        mMyGestureListener.scrollXToRight();
    }

    public boolean isMoveToLeft() {
        return mMyGestureListener.isMovingToLeft;
    }


    /**********************************************************************************************/

    public static class MoveGestureListener extends SimpleOnGestureListener
            implements MyGestureListener {

        private FlingHelper mFlingHelper;
        private View mView;
        private int mTouchSlopSquare;
        private float mLeftBound, mRightBound;
        private IOnGestureListener mGestureListener;
        private float mLatestX;
        private int mScrollState = MoveGestureDetector.IDLE;
        private boolean isMovingToLeft = true;

        public MoveGestureListener() {
            mLeftBound = 0;
            mRightBound = 0;
        }

        private void init() {
            mFlingHelper = new FlingHelper(mView);
            final ViewConfiguration configuration = ViewConfiguration.get(mView.getContext());
            mTouchSlopSquare = configuration.getScaledTouchSlop();
        }

        @Override
        public boolean onDown(MotionEvent e) {
            mLatestX = e.getRawX();
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            float offset = e2.getRawX() - mLatestX;
            ViewGroup parent = (ViewGroup) mView.getParent();
            parent.requestDisallowInterceptTouchEvent(true);
            if (mScrollState != MoveGestureDetector.SCROLLING) {
                if (mGestureListener != null) {
                    mGestureListener.onChangeState(mScrollState, MoveGestureDetector.PREPARE_SCROLL);
                }
                mScrollState = MoveGestureDetector.PREPARE_SCROLL;
            }

            mLatestX = e2.getRawX();
            float x = mView.getX();
            float newX = x + offset;

            float left = getLeftBound(x, e2);
            float right = getRightBound(x, e2);

            if (newX < left) {
                newX = left;
            } else if (newX > right) {
                newX = right;
            }
            float newOffset = newX - x;
            if (newOffset != 0) {
                Log.e("MoveGestureDetector",  "onScroll: " + newOffset);
                if (mScrollState != MoveGestureDetector.SCROLLING) {
                    if (mGestureListener != null) {
                        mGestureListener.onChangeState(mScrollState, MoveGestureDetector.SCROLLING);
                    }
                }
                mScrollState = MoveGestureDetector.SCROLLING;
                ViewCompat.offsetLeftAndRight(mView, (int) newOffset);
                if (mGestureListener != null) {
                    mGestureListener.onMove(mView.getX());
                }
            }

            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            float offset = e2.getRawX() - mLatestX;
            if (offset < mTouchSlopSquare) offset = 0;
            //if (offset == 0) return false;
            Log.e("MoveGestureDetector", "Flinging: " + velocityX);
            float x = mView.getX();
            isMovingToLeft = getDirectionX(e1, e2) == SWIPE_DIRECTION.LEFT;
            if (isMovingToLeft) {
                if (Math.abs(velocityX) < 500) {
                    float left = getLeftBound(x, e2);
                    boolean isHaft = Math.abs(x - left) <= Math.abs(getRightBound(x, e2) - left) / 2;
                    isMovingToLeft = isMovingToLeft && isHaft;
                }
            }
            /*if (Math.abs(velocityX) > 500) {
                Log.e("MoveGestureDetector", "Left bound: " + getLeftBound(x, e2) + ", x: " + x + ", veloc: " + velocityX);
                if (velocityX < 0) {
                    isMovingToLeft = true;
                    if (mGestureListener != null) {
                        mGestureListener.onChangeState(mScrollState, MoveGestureDetector.FLING);
                    }
                    mScrollState = MoveGestureDetector.FLING;

                    if (x > getLeftBound(x, e2)) {
                        mFlingHelper.onScrollX(0, -(int)(x - getLeftBound(x, e2)), (int) velocityX);
                    }
                } else if (velocityX > 0){
                    isMovingToLeft = false;
                    if (mGestureListener != null) {
                        mGestureListener.onChangeState(mScrollState, MoveGestureDetector.FLING);
                    }
                    mScrollState = MoveGestureDetector.FLING;

                    if (x < getRightBound(x, e2)) {
                        mFlingHelper.onScrollX(0, (int)(getRightBound(x, e2) - x), (int) velocityX);
                    }
                }


            } else {
                // Sometime e1 = null and e2 ís down event(ex action down is intercepted in ViewGroup) -> offset = 0,
                // but view has been scroll, so we need fly view to left bound or right bound

                Log.e("MoveGestureDetector", "Left bound: " + getLeftBound(x, e2) + ", x: " + x + ", veloc: " + velocityX);
                boolean moveToLeft;
                if (velocityX == 0) {
                    moveToLeft = canMoveToLeft(x, e2);
                } else {
                    moveToLeft = velocityX < 0;
                }

                if (moveToLeft){
                    isMovingToLeft = true;
                    if (mGestureListener != null) {
                        mGestureListener.onChangeState(mScrollState, MoveGestureDetector.FLING);
                    }
                    mScrollState = MoveGestureDetector.FLING;

                    if (x > getLeftBound(x, e2)) {
                        mFlingHelper.onScrollX(0, -(int)(x - getLeftBound(x, e2)), (int) velocityX);
                    }
                } else {
                    isMovingToLeft = false;
                    if (mGestureListener != null) {
                        mGestureListener.onChangeState(mScrollState, MoveGestureDetector.FLING);
                    }
                    mScrollState = MoveGestureDetector.FLING;

                    if (x < getRightBound(x, e2)) {
                        mFlingHelper.onScrollX(0, (int)(getRightBound(x, e2) - x), (int) velocityX);
                    }
                }
            }*/

            if (isMovingToLeft) {
                if (mGestureListener != null) {
                    mGestureListener.onChangeState(mScrollState, MoveGestureDetector.FLING);
                }
                mScrollState = MoveGestureDetector.FLING;
                if (x > getLeftBound(x, e2)) {
                    Log.e("MoveGestureDetector", "move to left");
                    mFlingHelper.onScrollX(0, -(int)(x - getLeftBound(x, e2)), -1 * Math.abs((int) velocityX));
                } else {
                    Log.e("MoveGestureDetector", "not move to left");
                    mGestureListener.onEnd(true);
                }
            } else {
                if (mGestureListener != null) {
                    mGestureListener.onChangeState(mScrollState, MoveGestureDetector.FLING);
                }
                mScrollState = MoveGestureDetector.FLING;

                if (x < getRightBound(x, e2)) {
                    mFlingHelper.onScrollX(0, (int)(getRightBound(x, e2) - x), Math.abs((int) velocityX));
                } else {
                    mGestureListener.onEnd(false);
                }
            }
            mScrollState = MoveGestureDetector.IDLE;

            return true;
        }

        @Override
        public boolean onUp(MotionEvent ev) {
            Log.e("MoveGestureDetector", "onUp called");
            if (ev.getRawX() - mLatestX <= mTouchSlopSquare && mScrollState == MoveGestureDetector.IDLE) {
                mView.performClick();
                return true;
            }
            mScrollState = MoveGestureDetector.IDLE;
            float x = mView.getX();

            if (canMoveToLeft(x, ev)) {
                isMovingToLeft = true;
                mFlingHelper.onScrollX(0, -(int)(x - getLeftBound(x, ev)));
                return true;
            }

            if (canMoveToRight(x, ev)) {
                isMovingToLeft = false;
                mFlingHelper.onScrollX(0, (int)(getRightBound(x, ev) - mView.getX()));
                return true;
            }

            /*if (mGestureListener != null) {
                mGestureListener.onEnd();
            }*/
            return false;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            Log.e("SingleTapUp", "called");

            return super.onSingleTapUp(e);
        }

        public float getLeftBound(float currentViewX, MotionEvent currEvent) {
            return mLeftBound;
        }

        public float getRightBound(float currentViewX, MotionEvent currEvent) {
            return mRightBound;
        }

        public boolean canMoveToLeft(float currentViewX, MotionEvent currEvent) {
            float left = getLeftBound(currentViewX, currEvent);
            float right = getRightBound(currentViewX, currEvent);
            return (Math.abs(currentViewX - left) < (right - left) / 2);
        }

        public boolean canMoveToRight(float currentViewX, MotionEvent currEvent) {
            float left = getLeftBound(currentViewX, currEvent);
            float right = getRightBound(currentViewX, currEvent);
            return Math.abs(currentViewX - left) > (right - left) / 2;
        }

        public void scrollXToLeft() {
            if (mGestureListener != null) {
                mGestureListener.onChangeState(MoveGestureDetector.IDLE, MoveGestureDetector.FLING);
            }
            isMovingToLeft = true;
            float x = mView.getX();
            mFlingHelper.onScrollX(0, -(int)(x - getLeftBound(x, null)), 0);
            mScrollState = MoveGestureDetector.IDLE;
        }

        public void scrollXToRight() {
            if (mGestureListener != null) {
                mGestureListener.onChangeState(MoveGestureDetector.IDLE, MoveGestureDetector.FLING);
            }
            isMovingToLeft = false;
            float x = mView.getX();
            mFlingHelper.onScrollX(0, (int)(getRightBound(x, null) - x), 0);
            mScrollState = MoveGestureDetector.IDLE;
        }

        public void setGestureListener(IOnGestureListener listener) {
            mGestureListener = listener;
            if (listener != null) {
                mFlingHelper.postOnAnimation(new Runnable() {
                    @Override
                    public void run() {
                        mGestureListener.onMove(mView.getX());
                    }
                });
                mFlingHelper.postOnEndAnimation(new Runnable() {
                    @Override
                    public void run() {
                        mGestureListener.onChangeState(MoveGestureDetector.FLING, MoveGestureDetector.IDLE);
                        mGestureListener.onEnd(isMovingToLeft);
                    }
                });
            }
        }

        private SWIPE_DIRECTION getDirectionX(MotionEvent e1, MotionEvent e2){
            float x1, y1;
            if (e1 == null) {
                x1 = 0;
                y1 = 0;
            } else {
                x1 = e1.getRawX();
                y1 = e1.getRawY();
            }

            float x2, y2;
            if (e2 == null) {
                x2 = 0;
                y2 = 0;
            } else {
                x2 = e2.getRawX();
                y2 = e2.getRawY();
            }

            double angle = getAngle(x1, y1, x2, y2);
            return SWIPE_DIRECTION.directionXFromAngle(angle);
        }

        private double getAngle(float x1, float y1, float x2, float y2) {
            double rad = Math.atan2(y1-y2,x2-x1) + Math.PI;
            return (rad*180/Math.PI + 180)%360;
        }
    }
}
