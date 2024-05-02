package com.quangph.base.mvp.mvpcomponent;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;

import com.quangph.base.common.BaseActivity;
import com.quangph.base.lifecycle.LCDelegate;
import com.quangph.base.lifecycle.OnResume;
import com.quangph.base.mvp.IChildPresenter;
import com.quangph.base.mvp.ICommand;
import com.quangph.base.mvp.IParentCommand;
import com.quangph.base.mvp.IParentPresenter;
import com.quangph.base.mvp.IView;
import com.quangph.base.mvp.action.ActionManager;
import com.quangph.base.mvp.action.IActionManager;
import com.quangph.base.statemachine.UIStateMachine;
import com.quangph.base.viewmodel.ISaveState;

import java.util.ArrayList;
import java.util.List;

import com.quangph.base.lifecycle.ILifeCycle;
import com.quangph.base.lifecycle.OnActivityResult;
import com.quangph.base.lifecycle.OnBackPressed;
import com.quangph.base.lifecycle.OnDestroy;

/**
 * Created by Pham Hai Quang on 1/8/2019.
 */
public class MVPActivity<V extends IView> extends BaseActivity implements IParentPresenter {

    protected V mView;
    protected IActionManager mActionManager;
    protected UIStateMachine mStateMachine;
    protected LCDelegate mLCController;

    private PresenterHelper mPresenterHelper = new PresenterHelper("MVPActivity");
    private boolean isPresenterReady = false;
    private boolean isInitFromRestoreState = false;
    private ViewModel mViewModel;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*if (!mStateMachine.isEmpty()) {
            mStateMachine.sendMessage(
                    mStateMachine.obtainMessage(MVPState.ACTIVITY_RESULT_CMD, requestCode, resultCode, data));
        }*/
        mLCController.invoke(this, OnActivityResult.class, requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        if (!mLCController.invoke(this, OnBackPressed.class)) {
            super.onBackPressed();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        actionManagerPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        actionManagerResume();
        mLCController.invoke(this, OnResume.class);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        ViewModel vm = findViewModel();
        if (vm instanceof ISaveState) {
            ((ISaveState)vm).saveInstanceState(outState);
        }
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        isInitFromRestoreState = true;

        /**
         * If this func is call when device is not rotate -> we decide that this act is created after reclaiming resource
         */
        if (!checkOrientation()) {
            onViewDidCreated(savedInstanceState);
            onInitVM();
            ViewModel vm = findViewModel();
            if (vm instanceof ISaveState) {
                ((ISaveState)vm).restoreInstanceState(savedInstanceState);
            }
        }
    }

    @Override
    protected void onViewWillCreate(Bundle savedInstanceState) {
        super.onViewWillCreate(savedInstanceState);
        mActionManager = createActionManager();
        mStateMachine = new UIStateMachine(getClass().getSimpleName()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == MVPState.HANDLE_CMD) {
                    try {
                        executeAndDispatchToChildren((ICommand)msg.obj);
                    } catch (Throwable t) {
                        t.printStackTrace();
                    }
                }
            }
        };
        mLCController = LCDelegate.INSTANCE;
    }

    @Override
    protected void onViewDidCreated(@Nullable Bundle savedInstanceState) {
        super.onViewDidCreated(savedInstanceState);
        mvpComponentWillLoad();
        preparePresenter();
        initMVPView((View) mView, getResources().getConfiguration().orientation, null);
        mView.setStatus(IView.Status.VIEW_READY);
    }

    @Override
    protected void onPostInit(@Nullable Bundle savedInstanceState) {
        super.onPostInit(savedInstanceState);
        if (startInRightState()) {
            isPresenterReady = true;
            onInitVM();
            presenterReady();
        } else {
            isInitFromRestoreState = false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLCController.invoke(this, OnDestroy.class);
        mLCController.removeComponent(this);
        mStateMachine.quit();

        mView.setStatus(IView.Status.VIEW_DESTROY);
        releaseActionManager();
        if (isPresenterReady) {
            isPresenterReady = false;
            presenterRelease();
        }

        mView = null;
    }

    @Override
    public void presenterReady() {
        onPresenterReady();
    }

    @Override
    public void presenterRelease() {
        mPresenterHelper.release();
        mPresenterHelper = null;
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
        // Not necessary in activity
    }

    @Override
    public List<IChildPresenter> getChildrenPresenter() {
        return mPresenterHelper.getChildren();
    }

    @Override
    public void addChildPresenter(IChildPresenter childPresenter, @Nullable String tag) {
        childPresenter.setParentPresenter(this);
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
    public void dispatchToChildren(ICommand command, @Nullable String... childrenTag) {
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

    public IActionManager createActionManager() {
        return ActionManager.get(getClass().getName());
    }

    public void releaseActionManager() {
        if (mActionManager != null) {
            mActionManager.releaseAndStopAllAction();
        }
    }

    /**
     * In the case of reclaiming resource, activity will call onRestore to restore data.
     * In order activity run normally, all data setup must be process inside this func.
     * Ex: setup observer for LiveData to connect between data and views.
     * When app call onRestore, all setup will be recreated, so app still run correctly.
     * In that case, this func will be called without calling onPresenterReady. This process make sure app just restore to the state before.
     * Ex: the activity will show saved data, and the calling api (which is called in onPresenterReady) is not processed
     * The func onPresenterReady need to call after calling this func in normal state (It mean we start activity like normal), ex: in onPresenterReady,
     * we invoke api service, so all data, logic about data have been correctly -> onPresenterReady is correct
     */
    public void onInitVM(){}

    public void mvpComponentWillLoad(){}

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

    public void onExecuteCommand(@NonNull ICommand command){ }

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

    public void transitToState(@NonNull String name) {
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

    public void backStateMachine() {
        removeStateFromLC(mStateMachine.getCurrentStateName());
        mStateMachine.back();
        String currentName = mStateMachine.getCurrentStateName();
        if (currentName != null) {
            addStateToLC(currentName);
        }
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

    public void postRunnable(Runnable runnable) {
        /*if (mHandler == null) {
            mHandler = new Handler(Looper.getMainLooper());
        }
        mHandler.post(runnable);*/
        getHandler().post(runnable);
    }

    public void addLifeCycle(ILifeCycle lifecycle) {
        mLCController.addComponent(this, lifecycle);
    }

    public void removeLifeCycle(ILifeCycle lifecycle) {
        mLCController.removeComponent(this, lifecycle);
    }

    /**
     * In some case, when screen ran into wrong state like api return error, you need disable all
     * components in the screen except for the back button, you call this function with param = false
     * @param enable
     */
    public void validView(boolean enable) {
        if (mView instanceof View) {
            validIntrinsic((View) mView, enable, preventIdsWhenValidView());
        }
    }

    /**
     * Define the views which are still enable when validView(false) is invoked
     * @return
     */
    public List<Integer> preventIdsWhenValidView() {
        return new ArrayList<>();
    }

    public ViewModel findViewModel() {
        if (mViewModel != null) {
            IViewModelFinder finder = new ViewModelFinderReflection();
            mViewModel = finder.findViewModel(this);
        }
        return mViewModel;
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
        presenter.setParentPresenter(this);
        mPresenterHelper.addChildPresenter(presenter, view, tag);
    }

    private void executeAndDispatchToChildren(ICommand command) {
        try {
            if (!onInterceptDispatchToChildren(command)) {
                for (IChildPresenter child : mPresenterHelper.getChildren()) {
                    if (!child.onInterceptCommand(command)) {
                        child.executeCommand(command);
                    }
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

    private void preparePresenter() {
        ViewGroup rootView = getWindow().getDecorView().findViewById(android.R.id.content);
        View realContentView = rootView.getChildAt(0);
        if (realContentView instanceof IView) {
            mView = (V) realContentView;
            preparePresenterForChildren(realContentView);
        } else {
            throw new IllegalArgumentException("Layout of Activity "
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
                preparePresenterForChildren(child);

                /*if (child instanceof IView && ((IView) child).getStatus() == IView.Status.VIEW_CREATE) {
                    ((IView) child).setStatus(IView.Status.VIEW_INITING);
                    ((IView) child).attachPresenter(this);
                }
                if (child instanceof ViewGroup) {
                    preparePresenterForChildren(child);
                }*/
            }
        }
    }

    private void initMVPView(View view, int orientation, IView parent) {
        IView tempParent = parent;
        if (view instanceof IView && ((IView) view).getStatus() == IView.Status.VIEW_INITING) {
            ((IView) view).setStatus(IView.Status.VIEW_INITED);
            ((IView) view).onInitView();
            ((IView) view).setMVPViewParent(tempParent);
            tempParent = (IView) view;
            if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                ((IView) view).onPortrait();
            } else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                ((IView) view).onLandscape();
            }
        }

        if (mustInterceptVisitChildrenView(view)) {
            return;
        }

        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View child = ((ViewGroup) view).getChildAt(i);
                /*if (child instanceof ViewGroup) {
                    initMVPView(child, orientation, tempParent);
                }*/
                initMVPView(child, orientation, tempParent);
            }
        }
    }

    private boolean mustInterceptVisitChildrenView(View view) {
        return view instanceof IView && !((IView) view).hasMVPChildren();
    }

    private boolean startInRightState() {
        // If screen is changed orientation, it like in normal. In the case app be reclaimed resource,
        // lc is : onRestore -> onActivityResult(if back from other activity) -> onResume -> onPostInit
        // (because it dispatch to the end of Looper)
        // In order to this screen act normally, we need run all process in onRestore to make sure onActivityResult() run normally
        if (checkOrientation()) {
            return true;
        } else {
            return !isInitFromRestoreState;
        }
    }
}
