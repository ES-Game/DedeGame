package com.quangph.base.mvp.mvpcomponent;

import android.os.Bundle;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.quangph.base.mvp.IChildPresenter;
import com.quangph.base.mvp.ICommand;
import com.quangph.base.mvp.IPresenter;
import com.quangph.base.mvp.IView;
import com.quangph.base.statemachine.UIState;

import com.quangph.base.lifecycle.ILifeCycle;


/**
 * Created by Pham Hai Quang on 8/24/2019.
 */
public class MVPState<P extends IPresenter, V extends IView> extends UIState implements ILifeCycle {

    public static final int HANDLE_CMD = -19998;
    //public static final int ACTIVITY_RESULT_CMD = -9997;

    protected P mStateContext;
    protected V mView;

    private Bundle mResult;
    private Bundle mReceivedResult;

    private final PresenterHelper mPresenterHelper = new PresenterHelper("MVPState");

    public MVPState(P stateContext, V view) {
        mStateContext = stateContext;
        mView = view;
    }

    @Override
    public boolean processMessage(Message msg) {
        if (msg.what == HANDLE_CMD) {
            try {
                return executeCommand((ICommand) msg.obj);
            } catch (Throwable t) {
                t.printStackTrace();
                return false;
            }

        } /*else if (msg.what == ACTIVITY_RESULT_CMD) {
            onActivityResult(msg.arg1, msg.arg2, (Intent) msg.obj);
        }*/
        return super.processMessage(msg);
    }

    @Override
    public void transitFrom(String prevStateName) {
        super.transitFrom(prevStateName);
        onTransitFrom(prevStateName, getResult());
    }

    @Override
    public void transitTo(String nextStateName) {
        super.transitTo(nextStateName);
        onTransitTo(nextStateName);
    }

    public Boolean executeCommand(ICommand command) {
        dispatchToChildren(command);
        if (!onInterceptCommand(command)) {
            return onExecuteCommand(command);
        } else {
            return false;
        }
    }

    public boolean onInterceptCommand(ICommand command) {
        return false;
    }
    public boolean onExecuteCommand(@NonNull ICommand command) {
        return false;
    }
    public void onTransitFrom(@Nullable String prevStateName, @Nullable Bundle extra) {}
    public void onTransitTo(@NonNull String nextStateName){}

    public Bundle buildResult() {
        mResult = new Bundle();
        return mResult;
    }

    public @Nullable Bundle getReturnResult() {
        return mResult;
    }

    /**
     * Get data transferred from other
     * @return
     */
    public Bundle getResult() {
        return mReceivedResult;
    }

    /**
     * Transfer data from other state to this state.
     * NOTE: This func will be called by state machine, so don't use this func in your business
     * @param bundle data need to be transferred
     */
    public void initWith(Bundle bundle) {
        mReceivedResult = bundle;
    }

    /**
     * After transferring data to the next state, the data will be released
     */
    public void releaseReturnResult() {
        if (mResult != null) {
            mResult = null;
        }
    }

    public void readyChild(@NonNull String childTag) {
        mPresenterHelper.readyChild(childTag);
    }

    public void addChildPresenter(IChildPresenter childPresenter, @Nullable String tag) {
        mPresenterHelper.addChildPresenter(childPresenter, tag);
    }

    public void addChildPresenter(@NonNull IChildPresenter presenter, @NonNull IView view) {
        addChildPresenter(presenter, view, null);
    }

    public void addChildPresenter(@NonNull IChildPresenter presenter,
                                  @NonNull IView view,
                                  @Nullable String tag) {
        mPresenterHelper.addChildPresenter(presenter, view, tag);
    }

    public void removeChildPresenter(IChildPresenter childPresenter) {
        mPresenterHelper.removeChildPresenter(childPresenter);
    }

    public IChildPresenter findPresenterByTag(@NonNull String tag) {
        return mPresenterHelper.findPresenterByTag(tag);
    }

    private void dispatchToChildren(ICommand command) {
        try {
            for (IChildPresenter child : mPresenterHelper.getChildren()) {
                child.executeCommand(command);
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
