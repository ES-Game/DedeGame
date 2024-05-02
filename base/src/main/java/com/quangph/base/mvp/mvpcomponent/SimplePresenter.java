package com.quangph.base.mvp.mvpcomponent;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.quangph.base.mvp.IChildPresenter;
import com.quangph.base.mvp.ICommand;
import com.quangph.base.mvp.IParentCommand;
import com.quangph.base.mvp.IParentPresenter;
import com.quangph.base.mvp.IView;

import java.util.List;

/**
 * Created by QuangPH on 2021-01-07.
 */
public class SimplePresenter<V extends IView> extends BasePresenter<V> implements IChildPresenter,
        IParentPresenter {

    private IParentPresenter mParent;
    private String mTag;
    private boolean isReady = false;
    private PresenterHelper mPresenterHelper = new PresenterHelper("SimplePresenter");

    public SimplePresenter() {
    }

    @Override
    public void presenterReady() {
        if (!isReady) {
            isReady = true;
            onPresenterReady();
        }
    }

    @Override
    public void presenterRelease() {
        super.presenterRelease();
        mPresenterHelper.release();
        onPresenterRelease();
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
        return false;
    }

    @Override
    public void executeCommand(ICommand command) {
        executeAndDispatchToChildren(command);
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
    public void removeChildPresenter(@NonNull String tag) {
        mPresenterHelper.removeChildPresenter(tag);
    }

    @Override
    public void dispatchToChildren(ICommand command, @Nullable String... childrenTag) {
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

    public void onExecuteCommand(ICommand command){}

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

    private void executeAndDispatchToChildren(ICommand command) {
        try {
            if (!onInterceptDispatchToChildren(command)) {
                for (IChildPresenter child : mPresenterHelper.getChildren()) {
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
}
