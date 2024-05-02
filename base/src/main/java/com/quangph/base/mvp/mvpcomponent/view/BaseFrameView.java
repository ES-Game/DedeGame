package com.quangph.base.mvp.mvpcomponent.view;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.RequiresApi;

import com.quangph.base.mvp.ICommand;
import com.quangph.base.mvp.IPresenter;
import com.quangph.base.mvp.IView;
import com.quangph.base.viewbinder.ViewBinder;

public class BaseFrameView extends FrameLayout implements IView {

    protected IPresenter mPresenter;
    protected IView mParentMVPView;
    private int mStatus = Status.VIEW_CREATE;
    private boolean isPortrait;

    public BaseFrameView(Context context) {
        super(context);
    }

    public BaseFrameView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseFrameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public BaseFrameView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void setStatus(int status) {
        this.mStatus = status;
    }

    @Override
    public int getStatus() {
        return mStatus;
    }

    @Override
    public void attachPresenter(IPresenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void onInitView() {
        ViewBinder.bind(this);
    }

    @Override
    public boolean hasMVPChildren() {
        return false;
    }

    @Override
    public void onPortrait() {
        isPortrait = true;
    }

    @Override
    public void onLandscape() {
        isPortrait = false;
    }

    @Override
    public boolean isPortrait() {
        return isPortrait;
    }

    @Override
    public void setMVPViewParent(IView parent) {
        mParentMVPView = parent;
    }

    @Override
    public void executeCommand(ICommand command) {
        if (!onDispatchCommand(command)) {
            if (mParentMVPView != null) {
                mParentMVPView.executeCommand(command);
            } else {
                mPresenter.executeCommand(command);
            }
        }
    }

    /**
     * Handle and dispatch command.
     * @param command
     * @return false command will send to MVP Parent view, true otherwise
     */
    public boolean onDispatchCommand(ICommand command) {
        return false;
    }
}
