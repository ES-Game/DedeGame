package com.quangph.base.mvp.action.scheduler.thirdparty;


import androidx.annotation.NonNull;

import com.quangph.base.mvp.action.Action;
import com.quangph.base.mvp.action.ActionException;
import com.quangph.base.mvp.domain.ICallback;

/**
 * Created by Pham Hai Quang on 1/12/2019.
 */
public abstract class ThirdPartyAction<R extends Action.RequestValue, T> extends Action<R, T> {

    private WrapCallback<R,T> mCallback;

    @Override
    final protected T onExecute(R requestValue) throws ActionException {
        setStatus(Action.RUNNING);
        mCallback = new WrapCallback<R, T>(this);
        onCall(requestValue, mCallback);
        return null;
    }

    protected abstract void onCall(R requestValue, @NonNull ICallback<T> callback);

    @Override
    protected void onStop() throws Exception {
        super.onStop();
        mCallback.release();
    }
}
