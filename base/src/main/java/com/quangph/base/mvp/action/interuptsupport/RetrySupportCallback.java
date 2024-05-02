package com.quangph.base.mvp.action.interuptsupport;

import androidx.annotation.NonNull;

import com.quangph.base.mvp.action.Action;
import com.quangph.base.mvp.action.ActionException;

/**
 * Created by QuangPH on 2020-09-26.
 */
public class RetrySupportCallback<T> implements Action.ActionCallback<T> {

    private Action.ActionCallback<T> mCallback;
    private RetryActionManager mManager;
    private ActionNode mActionNode;

    public RetrySupportCallback(Action.ActionCallback<T> callback, RetryActionManager manager, ActionNode node) {
        mCallback = callback;
        mManager = manager;
        mActionNode = node;
    }

    @Override
    public void onStart() {
        if (mCallback != null) {
            mCallback.onStart();
        }
    }

    @Override
    public void onStartError(ActionException e) {
        if (mCallback != null) {
            mCallback.onStartError(e);
        }
        release();
    }

    @Override
    public void onPublishProgress(Object event) {
        if (mCallback != null) {
            mCallback.onPublishProgress(event);
        }
    }

    @Override
    public void onSuccess(T responseValue) {
        if (mCallback != null) {
            mCallback.onSuccess(responseValue);
        }
        mManager.handleFinished(mActionNode);
        release();
    }

    @Override
    public void onError(@NonNull ActionException e) {
        if (mCallback != null) {
            mCallback.onError(e);
        }
        mManager.handleFinished(mActionNode);
        release();
    }

    @Override
    public void onInterrupt() {
        if (mCallback != null) {
            mCallback.onInterrupt();
        }
        mManager.handleFinished(mActionNode);
        release();
    }

    private void release() {
        mManager = null;
        mCallback = null;
        mActionNode = null;
    }
}
