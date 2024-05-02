package com.quangph.base.mvp.mvpcomponent;


import com.quangph.base.mvp.IChildPresenter;
import com.quangph.base.mvp.ICommand;
import com.quangph.base.mvp.IParentCommand;
import com.quangph.base.mvp.IParentPresenter;
import com.quangph.base.mvp.IView;

/**
 * Created by QuangPH on 2021-01-07.
 */
public class BaseChildPresenter<V extends IView> extends BasePresenter<V> implements IChildPresenter {

    private IParentPresenter mParent;
    private String mTag;
    private boolean isReady = false;

    public BaseChildPresenter() {

    }

    @Override
    public void presenterReady() {
        if (!isReady) {
            isReady = true;
            onPresenterReady();
        }
    }

    @Override
    public IParentPresenter getParentPresenter() {
        return mParent;
    }

    @Override
    public void setParentPresenter(IParentPresenter parent) {
        mParent = parent;
    }

    @Override
    public void dispatchToParent(IParentCommand command) {
        if (!isReady) return;

        if (mParent != null) {
            mParent.executeCommand(command);
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
    public boolean onInterceptCommand(ICommand command) {
        return command instanceof IParentCommand;
    }

    @Override
    public void executeCommand(ICommand command) {
        if (isReady) {
            onExecuteCommand(command);
        }
    }

    public void onPresenterReady(){}
    public void onExecuteCommand(ICommand command){}
}
