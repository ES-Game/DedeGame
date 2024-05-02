package com.quangph.base.mvp.mvpcomponent;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.quangph.base.common.BaseFragment;
import com.quangph.base.lifecycle.ILifeCycle;
import com.quangph.base.lifecycle.LCDelegate;
import com.quangph.base.lifecycle.OnActivityResult;
import com.quangph.base.lifecycle.OnDestroyView;
import com.quangph.base.mvp.IChildPresenter;
import com.quangph.base.mvp.ICommand;
import com.quangph.base.mvp.IParentCommand;
import com.quangph.base.mvp.IParentPresenter;
import com.quangph.base.mvp.IView;
import com.quangph.base.mvp.action.ActionManager;
import com.quangph.base.mvp.action.IActionManager;
import com.quangph.base.statemachine.UIStateMachine;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pham Hai Quang on 1/8/2019.
 */
public class MVPFragment<V extends IView> extends BaseFragment implements IChildPresenter, IParentPresenter {

    protected V mView;
    protected IActionManager mActionManager;
    protected UIStateMachine mStateMachine;
    protected LCDelegate mLCController;
    private String mTag;
    private IParentPresenter mParentPresenter;
    private PresenterHelper mPresenterHelper = new PresenterHelper("MVPFragment");

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*if (!mStateMachine.isEmpty()) {
            mStateMachine.sendMessage(
                    mStateMachine.obtainMessage(MVPState.ACTIVITY_RESULT_CMD, requestCode, resultCode, data));
        }*/
        mLCController.invoke(this, OnActivityResult.class, requestCode, resultCode, data);
    }

    @Override
    protected void onViewDidCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewDidCreated(view, savedInstanceState);
        mActionManager = createActionManager();
        mStateMachine = new UIStateMachine(getClass().getSimpleName()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == MVPState.HANDLE_CMD) {
                    executeAndDispatchToChildren((ICommand) msg.obj);
                }
            }
        };
        mLCController = LCDelegate.INSTANCE;

        mvpComponentWillLoad();
        preparePresenter(view);
        initMVPView(view, null);
        presenterReady();
    }

    @Override
    public void onStop() {
        super.onStop();
        actionManagerPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        actionManagerResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mLCController.invoke(this, OnDestroyView.class);
        mLCController.removeComponent(this);
        mStateMachine.quit();
        mView.setStatus(IView.Status.VIEW_DESTROY);
        releaseActionManager();
        presenterRelease();
        mView = null;
    }

    @Override
    public @Nullable IParentPresenter getParentPresenter() {
        return mParentPresenter;
    }

    @Override
    public void setParentPresenter(IParentPresenter parent) {
        mParentPresenter = parent;
    }

    @Override
    public void dispatchToParent(IParentCommand command) {
        if (getParentPresenter() != null) {
            getParentPresenter().executeCommand(command);
        }
    }

    @Override
    public String getTagName() {
        return mTag;
    }

    @Override
    public void setTagName(String tag) {
        mTag = tag;
    }

    @Override
    public void presenterReady() {
        onPresenterReady();
    }

    @Override
    public void presenterRelease() {
        //mActionManager.releaseAndStopAllAction();
        mPresenterHelper.release();
        onPresenterRelease();
    }

    @Override
    public boolean onInterceptCommand(ICommand command) {
        return false;
    }

    @Override
    final public void executeCommand(@NonNull ICommand command) {
        if (mStateMachine.isEmpty()) {
            executeAndDispatchToChildren(command);
        } else {
            mStateMachine.sendMessage(MVPState.HANDLE_CMD, command);
        }
    }

    @Override
    public void setMVPView(IView view) {
        // Not necessary in fragment
    }

    @Override
    public List<IChildPresenter> getChildrenPresenter() {
        return mPresenterHelper.getChildren();
    }

    @Override
    public void addChildPresenter(IChildPresenter childPresenter, @Nullable String tag) {
        mPresenterHelper.addChildPresenter(childPresenter, tag);
    }

    @Override
    public void removeChildPresenter(IChildPresenter childPresenter) {
        mPresenterHelper.removeChildPresenter(childPresenter);
    }

    @Override
    public void removeChildPresenter(String tag) {
        mPresenterHelper.removeChildPresenter(tag);
    }

    @Override
    public void dispatchToChildren(ICommand command, @Nullable @org.jetbrains.annotations.Nullable String... childrenTag) {
        //ParentCommand is always dispatched to children
        mPresenterHelper.dispatchToChildren(command, childrenTag);
    }

    @Override
    public boolean onInterceptDispatchToChildren(ICommand command) {
        return command instanceof IParentCommand;
    }

    @Override
    public IChildPresenter findPresenterByTag(@NonNull String tag) {
        return mPresenterHelper.findPresenterByTag(tag);
    }

    @Override
    public void readyChild(@NonNull String childTag) {
        mPresenterHelper.readyChild(childTag);
    }

    public void onPresenterReady(){}

    public void onPresenterRelease(){}

    public void mvpComponentWillLoad(){}

    public IActionManager createActionManager() {
        return ActionManager.get(getClass().getName());
    }

    public void releaseActionManager() {
        if (mActionManager != null) {
            mActionManager.releaseAndStopAllAction();
        }
    }

    public void actionManagerPause() {
        onActionManagerPause();
    }

    public void onActionManagerPause() {
        mActionManager.pause();
    }

    public void actionManagerResume() {
        onActionManagerResume();
    }

    public void onActionManagerResume() {
        mActionManager.resume();
    }

    public void onExecuteCommand(@NonNull ICommand command){}

    public void addState(String name, MVPState state) {
        mStateMachine.addState(name, state);
    }

    public void addState(String name, MVPState state, String parent) {
        mStateMachine.addState(name, state, parent);
    }

    public void initState(String name) {
        mStateMachine.initState(name);
        addStateToLC(name);
        startStateMachine();
    }

    public void transitToState(String name) {
        MVPState prev = (MVPState) mStateMachine.getState(mStateMachine.getCurrentStateName());
        if (prev != null) {
            Bundle result = prev.getReturnResult();
            MVPState next = (MVPState) mStateMachine.getState(name);
            if (next != null) {
                next.initWith(result);
            }
            prev.releaseReturnResult();
        }
        removeStateFromLC(mStateMachine.getCurrentStateName());
        mStateMachine.transitTo(name);
        addStateToLC(name);
    }

    public void startStateMachine() {
        mStateMachine.start();
    }

    public V getMVPView() {
        return mView;
    }

    public IActionManager getActionManager() {
        return mActionManager;
    }

    public UIStateMachine getStateMachine() {
        return mStateMachine;
    }

    public void addLifeCycle(ILifeCycle lifecycle) {
        mLCController.addComponent(this, lifecycle);
    }

    public void removeLifeCycle(ILifeCycle lifecycle) {
        mLCController.removeComponent(this, lifecycle);
    }

    public void validView(boolean enable) {
        if (mView instanceof View) {
            validIntrinsic((View) mView, enable, preventIdsWhenValidView());
        }
    }

    public List<Integer> preventIdsWhenValidView() {
        return new ArrayList<>();
    }

    /**
     * In case of activity contains some children view and children presenter,
     * use this func to glue a child presenter to a child view
     * @param presenter
     * @param view
     */
    public void addChildPresenter(@NonNull IChildPresenter presenter, @NonNull IView view) {
        addChildPresenter(presenter, view, null);
    }

    /**
     * In case of activity contains some children view and children presenter,
     * use this func to glue a child presenter to a child view
     * @param presenter
     * @param view
     */
    public void addChildPresenter(@NonNull IChildPresenter presenter, @NonNull IView view, @Nullable String tag) {
        mPresenterHelper.addChildPresenter(presenter, view, tag);
    }

    private void validIntrinsic(View view, boolean enable, List<Integer> preventIdList) {
        if (!preventIdList.contains(view.getId())) {
            view.setEnabled(enable);
            if (view instanceof ViewGroup) {
                for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                    validIntrinsic(((ViewGroup) view).getChildAt(i), enable, preventIdList);
                }
            }
        }
    }

    private void addStateToLC(String stateName) {
        //mLCController.clearComponent(this);
        if (mStateMachine.getState(stateName) != null) {
            mLCController.addComponent(this, (MVPState)mStateMachine.getState(stateName));
        }
    }

    private void removeStateFromLC(String stateName) {
        MVPState state = (MVPState) mStateMachine.getState(stateName);
        if (state != null) {
            mLCController.removeComponent(this, state);
        }
    }

    /**
     * First: all children presenter exe cmd, this fragment will exe cmd last
     * @param command
     */
    private void executeAndDispatchToChildren(ICommand command) {
        try {
            if (!onInterceptDispatchToChildren(command)) {
                for (IChildPresenter child: mPresenterHelper.getChildren()) {
                    child.executeCommand(command);
                }
            }
        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
            if (!onInterceptCommand(command)) {
                try {
                    onExecuteCommand(command);
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }
        }
    }

    private void preparePresenter(View view) {
        if (view instanceof IView) {
            mView = (V) view;
            preparePresenterForChildren(view);
        } else {
            throw new IllegalArgumentException("Layout of Fragment "
                    + getClass().getSimpleName()
                    + " must have View extend IView");
        }
    }

    private void preparePresenterForChildren(View parent) {
        if (parent instanceof IView && ((IView) parent).getStatus() == IView.Status.VIEW_CREATE) {
            ((IView) parent).setStatus(IView.Status.VIEW_INITING);
            ((IView) parent).attachPresenter(this);
        }

        if (mustInterceptVisitChildrenView(parent)) {
            return;
        }

        if (parent instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup)parent).getChildCount(); i++) {
                View child = ((ViewGroup)parent).getChildAt(i);
                if (child instanceof IView && ((IView) child).getStatus() == IView.Status.VIEW_CREATE) {
                    ((IView) child).setStatus(IView.Status.VIEW_INITING);
                    ((IView) child).attachPresenter(this);
                }
                if (child instanceof ViewGroup) {
                    preparePresenterForChildren(child);
                }
            }
        }
    }

    private void initMVPView(View view, IView parent) {
        IView tempParent = parent;
        if (view instanceof IView && ((IView) view).getStatus() == IView.Status.VIEW_INITING) {
            ((IView) view).setStatus(IView.Status.VIEW_READY);
            ((IView) view).onInitView();
            ((IView) view).setMVPViewParent(tempParent);
            tempParent = (IView) view;
        }

        if (mustInterceptVisitChildrenView(view)) {
            return;
        }

        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View child = ((ViewGroup) view).getChildAt(i);
                if (child instanceof ViewGroup) {
                    initMVPView(child, tempParent);
                }
            }
        }
    }

    private boolean mustInterceptVisitChildrenView(View view) {
        return view instanceof IView && !((IView) view).hasMVPChildren();
    }
}
