package com.quangph.base.mvp.action.post;

import com.quangph.base.mvp.action.ACTION_POST_POLICY;
import com.quangph.base.mvp.action.Action;
import com.quangph.base.mvp.action.IActionManager;
import com.quangph.base.mvp.action.IActionManagerLifecycle;

/**
 * Delay/Repeat an action manager
 */
public interface IPostActionManager extends IActionManagerLifecycle {
    <R extends Action.RequestValue, T> void post(IActionManager actionManager,
                                                 Action<R, T> action,
                                                 R requestVal,
                                                 Action.ActionCallback<T> callback,
                                                 long timeInMillis,
                                                 ACTION_POST_POLICY policy);

    <R extends Action.RequestValue, T> void registerPostCallback(IActionManager actionManager,
                                                 String actionID,
                                                 Action.ActionCallback<T> callback);
}
