package com.quangph.base.mvp.action;

import com.quangph.base.mvp.action.scheduler.IActionScheduler;

/**
 * Created by QuangPH on 2020-10-01.
 */
public interface IBuilder {

    IBuilder setRunMode(RUN_MODE mode);

    /**
     * Set callback for all actions built in this builder
     * @param callback
     */
    IBuilder onCompound(CompoundCallback callback);

    /**
     * Add all actions to queue and dispatch by calling run()
     * @param action
     * @param requestVal
     * @param callback
     * @param scheduler
     * @param <R>
     * @param <T>
     * @return
     */
    <R extends Action.RequestValue, T> IBuilder add(
            Action<R, T> action,
            R requestVal,
            Action.ActionCallback<T> callback,
            IActionScheduler scheduler);

    /**
     * Action will be dispatched immediately
     * @param action
     * @param requestVal
     * @param callback
     * @param scheduler
     * @param <R>
     * @param <T>
     * @return
     */
    <R extends Action.RequestValue, T> IBuilder and(
            Action<R, T> action,
            R requestVal,
            Action.ActionCallback<T> callback,
            IActionScheduler scheduler);

    /**
     * Dispatch queue to execute
     * @return
     */
    IBuilder run();
}
