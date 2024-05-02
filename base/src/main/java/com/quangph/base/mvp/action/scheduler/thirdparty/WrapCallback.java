package com.quangph.base.mvp.action.scheduler.thirdparty;

import com.quangph.base.mvp.action.Action;
import com.quangph.base.mvp.action.ActionException;
import com.quangph.base.mvp.action.ActionMonitor;
import com.quangph.base.mvp.domain.ICallback;

import java.lang.ref.WeakReference;


/**
 * Created by Pham Hai Quang on 1/11/2019.
 */
public class WrapCallback<R extends Action.RequestValue, T> implements ICallback<T> {

    private WeakReference<Action<R, T>> mActionWeakReference;

    public WrapCallback(Action<R, T> action) {
        mActionWeakReference = new WeakReference<>(action);
    }

    @Override
    public void onStart() {
        Action action = mActionWeakReference.get();
        if (action != null) {
            ActionMonitor.getInstance().notifyStartAction(action);
        }
    }

    @Override
    public void onSuccess(T response) {
        Action action = mActionWeakReference.get();
        if (action != null) {
            action.setResponse(response);
            ActionMonitor.getInstance().notifySuccessAction(action);
        }
    }

    @Override
    public void onError(Exception error) {
        Action action = mActionWeakReference.get();
        if (action != null) {
            action.setError(new ActionException(error));
            ActionMonitor.getInstance().notifyErrorAction(action);
        }
    }

    @Override
    public void onPublishEvent(Object event) {
        Action action = mActionWeakReference.get();
        if (action != null) {
            action.setCurrentEvent(event);
            ActionMonitor.getInstance().notifyProgress(action);
        }
    }

    public void release() {
        mActionWeakReference.clear();
    }
}
