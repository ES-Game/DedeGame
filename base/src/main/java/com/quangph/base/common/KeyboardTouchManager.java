package com.quangph.base.common;

import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.EditText;

/**
 * Created by QuangPH on 2020-09-23.
 */
public class KeyboardTouchManager {

    public enum KEYBOARD_CONFIG {
        NONE, DISMISS, DISMISS_AND_PERFORM_CLICK
    }

    private BaseActivity mBaseActivity;
    private float mLastTouchX;
    private float mLastTouchY;
    private float mScaledTouchSlop;
    private boolean isPrevTouchEventMove = false;
    private Rect mOutSideKeyboardBound;
    private View mRootView;
    private KEYBOARD_CONFIG mAction = KEYBOARD_CONFIG.NONE;

    public KeyboardTouchManager(BaseActivity context) {
        mBaseActivity = context;
        mRootView = context.findViewById(android.R.id.content);
        mScaledTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (mAction == KEYBOARD_CONFIG.NONE) return false;

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastTouchX = ev.getX();
                mLastTouchY = ev.getY();
                //return mBaseActivity.isKeyboardShown();
                return consumeDownEvent(ev);
            case MotionEvent.ACTION_MOVE:
                if (isScrollGesture(ev, mLastTouchX, mLastTouchY)) {
                    isPrevTouchEventMove = true;
                    if (hideKeyboardWhenScrollingOutside()) {
                        hideKeyBoardWhenTouchOutside(ev);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if (hideKeyboardWhenScrollingOutside()) {
                    boolean consumed = hideKeyBoardWhenTouchOutside(ev);
                    if (mAction != KEYBOARD_CONFIG.DISMISS_AND_PERFORM_CLICK) {
                        if (consumed) return true;
                    } else {
                        break;
                    }
                }
        }
        return false;
    }

    public void setAction(KEYBOARD_CONFIG action) {
        mAction = action;
    }

    private boolean hideKeyboardWhenScrollingOutside() {
        return mAction != KEYBOARD_CONFIG.NONE;
    }

    private boolean isScrollGesture(MotionEvent event, float originalX, float originalY) {
        float moveX = Math.abs(event.getX() - originalX);
        float moveY = Math.abs(event.getY() - originalY);
        return moveX > mScaledTouchSlop || moveY > mScaledTouchSlop;
    }

    /**
     * Close Keyboard when touch outside Edittext
     *
     * @param ev
     */
    private boolean hideKeyBoardWhenTouchOutside(MotionEvent ev) {
        View view = mBaseActivity.getCurrentFocus();
        if (view instanceof EditText
                && !view.getClass().getName().startsWith("android.webkit.")) {
            if (mOutSideKeyboardBound == null) {
                mOutSideKeyboardBound = new Rect();
            }
            mRootView.getWindowVisibleDisplayFrame(mOutSideKeyboardBound);

            int[] scrcoords = new int[2];
            view.getLocationOnScreen(scrcoords);
            float x = ev.getRawX() + view.getLeft() - scrcoords[0];
            float y = ev.getRawY() + view.getTop() - scrcoords[1];
            if ((x < view.getLeft() || x > view.getRight() || y < view.getTop() || y > view.getBottom())
                    && mBaseActivity.acceptToDismissKeyboard(mOutSideKeyboardBound, ev.getRawX(), ev.getRawY())) {
                /*((InputMethodManager) mBaseActivity.getSystemService(Context.INPUT_METHOD_SERVICE))
                        .hideSoftInputFromWindow((mBaseActivity.getWindow().getDecorView().getApplicationWindowToken()), 0);*/
                mBaseActivity.hideKeyBoard();
                return true;
            }
        }
        return false;
    }

    private boolean consumeDownEvent(MotionEvent ev) {
        if (!mBaseActivity.acceptToDismissKeyboard(mOutSideKeyboardBound, ev.getRawX(), ev.getRawY())) {
            return false;
        } else {
            return mBaseActivity.isKeyboardShown();
        }
    }
}
