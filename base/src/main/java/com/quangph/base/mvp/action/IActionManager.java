package com.quangph.base.mvp.action;

import com.quangph.base.mvp.action.scheduler.IActionScheduler;

/**
 * Created by QuangPH on 2020-10-01.
 */
public interface IActionManager {
    interface ActionStatus {
        int RESUME_STATE = 1;
        int PAUSE_STATE = 2;
        int RELEASE_STATE = 3;
    }

    String getID();
    int getStatus();
    void pause();
    void resume();

    /**
     * Release all callbacks related to this ActionManager, but all action still running on background
     * Scenario: An Activity is rotated when it has an action is running in background, when app is
     * in landscape, the action finish its job, action manager find the registered callback to fire.
     * This is case {@link IActionManager#registerCallback(String, Action.ActionCallback)} take into account
     */
    void release();

    void releaseAndStopAllAction();
    void stopAction(Action action);
    void stopAction(String actionID);
    void stopAction(Class<? extends Action> actionType);
    <T> IBuilder executeAction(Action<Action.VoidRequest, T> action, Action.ActionCallback<T> callback);
    <R extends Action.RequestValue, T> IBuilder executeAction(
            Action<R, T> action, R requestVal, Action.ActionCallback<T> callback);
    <R extends Action.RequestValue, T> IBuilder executeAction(
            Action<R, T> action,
            R requestVal,
            Action.ActionCallback<T> callback,
            IActionScheduler scheduler);

    /**
     * Scenario: Stop an action, after that execute other, so you must post a runnable to queue.
     * This runnable must be posted on ActionHandler which handle status of action
     * @param action
     * @param requestVal
     * @param callback
     * @param scheduler
     * @param <R>
     * @param <T>
     */
    <R extends Action.RequestValue, T> void executeActionOnPost(final Action<R, T> action,
                                                                final R requestVal,
                                                                final Action.ActionCallback<T> callback,
                                                                final IActionScheduler scheduler);

    /**
     * Register callback
     * @param actionID
     * @param callback
     * @param <T>
     */
    <T> void registerCallback(String actionID, Action.ActionCallback<T> callback);

    /**
     * Call this fun if you want to push a chunk of action to queue. You can create a builder,
     * do smt before adding actions to queue by calling Builder.add(), then call IBuilder.run()
     * to dispatch that chunks of action in order to execute
     * @return
     */
    IBuilder builder();

    /**
     * For executing delay/repeat an action purpose
     * @param action
     * @param requestVal
     * @param callback
     * @param timeInMillis
     * @param policy
     * @param <R>
     * @param <T>
     */
    <R extends Action.RequestValue, T> void post(
            final Action<R, T> action,
            final R requestVal,
            final Action.ActionCallback<T> callback,
            final long timeInMillis,
            final ACTION_POST_POLICY policy);

    /**
     * For register callback to an delay action
     * @param actionID
     * @param callback
     * @param <T>
     */
    <T> void registerPostCallback(String actionID, Action.ActionCallback<T> callback);
}
