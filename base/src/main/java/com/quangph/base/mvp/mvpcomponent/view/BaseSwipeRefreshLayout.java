package com.quangph.base.mvp.mvpcomponent.view;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.quangph.base.mvp.ICommand;
import com.quangph.base.mvp.IPresenter;
import com.quangph.base.mvp.IView;
import com.quangph.base.viewbinder.ViewBinder;

/**
 * Created by Pham Hai Quang on 3/26/2019.
 */
public class BaseSwipeRefreshLayout extends SwipeRefreshLayout implements IView {

    protected IPresenter mPresenter;
    protected IView mParentMVPView;
    private int mStatus = Status.VIEW_CREATE;
    private boolean isPortrait;

    public BaseSwipeRefreshLayout(@NonNull Context context) {
        super(context);
    }

    public BaseSwipeRefreshLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
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
    public boolean hasMVPChildren() {
        return false;
    }

    @Override
    public void onInitView() {
        ViewBinder.bind(this);
        setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.executeCommand(new RefreshCmd());
            }
        });
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
     *
     * @param command
     * @return false command will send to MVP Parent view, true otherwise
     */
    public boolean onDispatchCommand(ICommand command) {
        return false;
    }


    public static class RefreshCmd implements ICommand {

    }
}
