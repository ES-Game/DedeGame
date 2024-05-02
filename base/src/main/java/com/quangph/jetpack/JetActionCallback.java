package com.quangph.jetpack;

import com.quangph.base.mvp.action.Action;
import com.quangph.base.mvp.action.ActionException;
import com.quangph.jetpack.error.INetworkError;
import com.quangph.jetpack.error.NetworkErrorImpl;

import java.lang.ref.WeakReference;
import java.net.UnknownHostException;

/**
 * Base of callback when presenter execute an action
 * Created by Pham Hai Quang on 10/08/2019.
 */
public class JetActionCallback<T> extends Action.SimpleActionCallback<T> {

    private WeakReference<IJetContext> mWeakContext;
    private INetworkError mNetworkHandler = NetworkErrorImpl.INSTANCE;

    public JetActionCallback(IJetContext context) {
        mWeakContext = new WeakReference<>(context);
    }

    @Override
    public void onSuccess(T responseValue) {
        super.onSuccess(responseValue);
    }

    @Override
    public void onError(ActionException e) {
        IJetContext context = mWeakContext.get();
        if (context == null) {
            return;
        }
        context.hideLoading();
        Throwable cause = e.getCause();
        if (cause instanceof JetException) {
            mNetworkHandler.onNetworkConnected();
            onException(context, (JetException) cause);
        } else if (cause instanceof UnknownHostException) {
            mNetworkHandler.onNetworkError(context);
        } else {
            onUnhandledException(context, cause);
        }
    }

    protected void onException(IJetContext context, JetException e) {
        if (e != null) {
            e.printStackTrace();
        }
    }

    protected void onUnhandledException(IJetContext context, Throwable cause) {
        if (cause != null) {
            cause.printStackTrace();
        }
    }
}
