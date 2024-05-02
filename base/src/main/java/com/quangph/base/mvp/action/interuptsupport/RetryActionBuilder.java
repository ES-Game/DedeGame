package com.quangph.base.mvp.action.interuptsupport;

import com.quangph.base.mvp.action.Action;
import com.quangph.base.mvp.action.CompoundCallback;
import com.quangph.base.mvp.action.IBuilder;
import com.quangph.base.mvp.action.RUN_MODE;
import com.quangph.base.mvp.action.scheduler.IActionScheduler;

/**
 * Created by QuangPH on 2020-09-26.
 */
public class RetryActionBuilder implements IBuilder {

    private RetryActionManager mActionManager;
    private IBuilder mBuilder;
    private ActionNode mRootNode;
    private CompoundCallback mParallelCallback;

    public RetryActionBuilder(RetryActionManager actionManager) {
        mActionManager = actionManager;
    }

    public<R extends Action.RequestValue, T> RetryActionBuilder executeAction(
            Action<R, T> action,
            R requestVal,
            Action.ActionCallback<T> callback,
            IActionScheduler scheduler) {

        mRootNode = new ActionNode(action);
        mRootNode.buildType = ActionNode.NONE;
        mRootNode.isRoot = true;
        mRootNode.scheduler = scheduler;
        mRootNode.setParallelCallback(mParallelCallback);

        Action.ActionCallback<T> wrap = wrapCallback(callback, mRootNode);
        mBuilder = mActionManager.getActionManager().executeAction(action, requestVal, wrap, scheduler);
        return this;
    }

    @Override
    public IBuilder setRunMode(RUN_MODE mode) {
        if (mBuilder != null) {
            mBuilder.setRunMode(mode);
        }
        return this;
    }

    @Override
    public IBuilder onCompound(CompoundCallback callback) {
        mParallelCallback = callback;
        if (callback != null) {
            mParallelCallback.setActionManager(mActionManager);
        }
        if (mBuilder != null) {
            mBuilder.onCompound(callback);
        }

        if (mRootNode != null) {
            mRootNode.setParallelCallback(callback);
        }
        return this;
    }

    @Override
    public<R extends Action.RequestValue, T> RetryActionBuilder and(Action<R, T> action,
                                                                    R requestVal,
                                                                    Action.ActionCallback<T> callback,
                                                                    IActionScheduler scheduler) {

        ActionNode nextNode = new ActionNode(action);
        nextNode.buildType = ActionNode.AND;
        nextNode.scheduler = scheduler;
        mRootNode.addToEnd(nextNode);

        Action.ActionCallback<T> wrap = wrapCallback(callback, nextNode);
        mBuilder.and(action, requestVal, wrap, scheduler);
        return this;
    }

    @Override
    public<R extends Action.RequestValue, T> IBuilder add(
            Action<R, T> action,
            R requestVal,
            Action.ActionCallback<T> callback,
            IActionScheduler scheduler) {
        if (mBuilder == null) {
            mBuilder = mActionManager.getActionManager().builder();
        }

        if (mRootNode == null) {
            mRootNode = new ActionNode(action);
            mRootNode.isRoot = true;
            mRootNode.buildType = ActionNode.ADD;
            mRootNode.scheduler = scheduler;
            mRootNode.setParallelCallback(mParallelCallback);
            mBuilder.add(action, requestVal, wrapCallback(callback, mRootNode), scheduler);
        } else {
            ActionNode nextNode = new ActionNode(action);
            nextNode.buildType = ActionNode.ADD;
            nextNode.scheduler = scheduler;
            mRootNode.addToEnd(nextNode);
            mBuilder.add(action, requestVal, wrapCallback(callback, nextNode), scheduler);
        }

        return this;
    }

    @Override
    public RetryActionBuilder run() {
        if (mBuilder != null) {
            mBuilder.run();
        }
        return this;
    }

    public ActionNode getRoot() {
        return mRootNode;
    }

    private<T> Action.ActionCallback<T> wrapCallback(Action.ActionCallback<T> callback, ActionNode node) {
        if (mActionManager.autoRetry()) {
            return new RetrySupportCallback<>(callback, mActionManager, node);
        } else {
            return callback;
        }
    }
}
