package com.quangph.base.mvp.action;

import android.util.Log;

import androidx.annotation.NonNull;

import com.quangph.base.mvp.action.actionhandler.IActionHandler;
import com.quangph.base.mvp.action.post.IPostActionManager;
import com.quangph.base.mvp.action.post.SequencePostActionManager;
import com.quangph.base.mvp.action.queue.ActionEnqueue;
import com.quangph.base.mvp.action.scheduler.AsyncTaskScheduler;
import com.quangph.base.mvp.action.scheduler.IActionScheduler;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by Pham Hai Quang on 6/21/2019.
 */
public class ActionManager implements IActionManager {

    private static final String TAG = "ActionManager";
    private String id;
    private int mStatus = ActionStatus.RESUME_STATE;
    private ActionMonitor mActionMonitor = ActionMonitor.getInstance();
    private List<IActionManagerLifecycle> mLCList = new ArrayList<>();
    private IPostActionManager mPostManager;

    public static ActionManager get(String id) {
        return new ActionManager(id);
    }

    private ActionManager(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ActionManager that = (ActionManager) o;
        return id.equals(that.getID());
    }

    @Override
    public String getID() {
        return id;
    }

    @Override
    public int getStatus() {
        return mStatus;
    }

    @Override
    public void pause() {
        mStatus = ActionStatus.PAUSE_STATE;
        for (IActionManagerLifecycle lc : mLCList) {
            lc.pause(this);
        }
    }

    @Override
    public void resume() {
        if (mStatus != ActionStatus.RESUME_STATE) {
            mStatus = ActionStatus.RESUME_STATE;
            mActionMonitor.resume(this);
        }

        for (IActionManagerLifecycle lc : mLCList) {
            lc.resume(this);
        }
    }

    /**
     * Release all callbacks related to this ActionManager, but all action still running on background
     */
    @Override
    public void release() {
        if (mStatus != ActionStatus.RELEASE_STATE) {
            mActionMonitor.removeAllRegisteredCallbacksFromActionManager(this);
            mActionMonitor.removeCallbackFromActionManager(this);
            mStatus = ActionStatus.RELEASE_STATE;
        }

        for (IActionManagerLifecycle lc : mLCList) {
            lc.release(this);
        }
        mLCList.clear();
        if (mPostManager != null) {
            mPostManager = null;
        }
    }

    @Override
    public void releaseAndStopAllAction() {
        if (mStatus != ActionStatus.RELEASE_STATE) {
            mActionMonitor.removeAllRegisteredCallbacksFromActionManager(this);
            mActionMonitor.releaseAllActionFromActionManager(this);
            mStatus = ActionStatus.RELEASE_STATE;
        }

        for (IActionManagerLifecycle lc : mLCList) {
            lc.releaseAndStopAllAction(this);
        }
        mLCList.clear();
        if (mPostManager != null) {
            mPostManager = null;
        }
    }

    @Override
    public void stopAction(Action action) {
        if (mStatus != ActionStatus.RELEASE_STATE) {
            mActionMonitor.stopAction(action);
        }
    }

    @Override
    public void stopAction(String actionID) {
        if (mStatus != ActionStatus.RELEASE_STATE) {
            mActionMonitor.stopAction(actionID);
        }
    }

    @Override
    public void stopAction(Class<? extends Action> actionType) {
        if (mStatus != ActionStatus.RELEASE_STATE) {
            mActionMonitor.stopAction(actionType);
        }
    }

    @Override
    public<T> IBuilder executeAction(
            Action<Action.VoidRequest, T> action, Action.ActionCallback<T> callback) {
        return executeAction(action, new Action.VoidRequest(), callback);
    }

    @Override
    public<R extends Action.RequestValue, T> IBuilder executeAction(
            Action<R, T> action, R requestVal, Action.ActionCallback<T> callback) {
        return executeAction(action, requestVal, callback, new AsyncTaskScheduler());
    }

    @Override
    public<R extends Action.RequestValue, T> IBuilder executeAction(
            Action<R, T> action,
            R requestVal,
            Action.ActionCallback<T> callback,
            IActionScheduler scheduler) {

        Builder builder = new Builder(this);
        builder.create(action, requestVal, callback, scheduler);
        return builder;
    }

    @Override
    public<R extends Action.RequestValue, T> void executeActionOnPost(final Action<R, T> action,
                                                                      final R requestVal,
                                                                      final Action.ActionCallback<T> callback,
                                                                      final IActionScheduler scheduler) {
        mActionMonitor.executeActionOnPost(new Runnable() {
            @Override
            public void run() {
                executeAction(action, requestVal, callback, scheduler);
            }
        });
    }

    /**
     * Call this fun if you want to push a chunk of action to queue. You can create a builder,
     * do smt before adding actions to queue by calling Builder.add(), then call Builder.run()
     * to dispatch that chunks of action in order to execute
     * @return
     */
    @Override
    public IBuilder builder() {
        return new Builder(this);
    }

    @Override
    public <R extends Action.RequestValue, T> void post(Action<R, T> action,
                                                        R requestVal,
                                                        Action.ActionCallback<T> callback,
                                                        long timeInMillis,
                                                        ACTION_POST_POLICY policy) {
        if (mPostManager == null) {
            mPostManager = SequencePostActionManager.getInstance();
            mLCList.add(mPostManager);
        }

        mPostManager.post(this, action, requestVal, callback, timeInMillis, policy);
    }

    @Override
    public <T> void registerPostCallback(String actionID, Action.ActionCallback<T> callback) {
        if (mPostManager == null) {
            mPostManager = SequencePostActionManager.getInstance();
            mLCList.add(mPostManager);
        }
        mPostManager.registerPostCallback(this, actionID, callback);
    }

    @Override
    public<T> void registerCallback(String actionID, Action.ActionCallback<T> callback) {
        // Add to cache
        addCallbackToCached(actionID, callback);

        // Attach to running action
        ActionQueueInfo runningActionQueue = mActionMonitor.findRunningAction(actionID);
        if (runningActionQueue != null) {
            runningActionQueue.callbackList.add(makeCallbackInfo(callback));
        }
    }

    public void setStatus(int status) {
        mStatus = status;
    }

    public ActionEnqueue createQueue() {
        ActionEnqueue queue = mActionMonitor.createEnqueue();
        queue.setRunMode(RUN_MODE.SEQUENCE_MODE);
        return queue;
    }

    public ActionCallbackInfo makeCallbackInfo(Action.ActionCallback callback) {
        ActionCallbackInfo info = new ActionCallbackInfo();
        info.actionManager = this;
        info.callback = callback;
        //info.managerOwnAction = callbackBelongToManager;
        return info;
    }

   /* public void dispatchAction(ActionQueueInfo actionQueueInfo) {
        mDispatcher.dispatch(actionQueueInfo);
    }*/

    private<T> void addCallbackToCached(@NonNull String actionID, Action.ActionCallback<T> callback) {
        /*if (mActionMonitor.callbackHasRegistered(actionID)) {
            Log.e(TAG, "Callback is registered for id: " + actionID);
        } else {
            mActionMonitor.addActionCallbackInfo(this, actionID, makeCallbackInfo(callback));
        }*/

        mActionMonitor.addActionCallbackInfo(this, actionID, makeCallbackInfo(callback));
    }

    private static IActionHandler.ActionDispatcherInfo obtainActionDispatcherInfo(int what, Object obj) {
        IActionHandler.ActionDispatcherInfo info = new IActionHandler.ActionDispatcherInfo();
        info.what = what;
        info.obj = obj;
        return info;
    }

    public static class Builder implements IBuilder {

        private ActionManager mActionManager;
        private ActionEnqueue mQueue;
        private boolean isDispatched = false;
        private boolean hasSetRunMode = false;

        public Builder(ActionManager actionManager) {
            mActionManager = actionManager;
        }

        @Override
        public IBuilder setRunMode(RUN_MODE mode) {
            if (mQueue == null) {
                mQueue = mActionManager.createQueue();
            }
            mQueue.setRunMode(mode);
            hasSetRunMode = true;
            return this;
        }

        @Override
        public IBuilder onCompound(CompoundCallback callback) {
            // If queue is null, it mean no action was add to queue, so it seem all actions got done
            if (mQueue == null) {
                callback.onCompletedAll();
            } else {
                callback.setActionManager(mActionManager);
                mQueue.compound(callback);
            }
            if (!hasSetRunMode) {
                mQueue.setRunMode(RUN_MODE.PARALLEL_MODE);
                hasSetRunMode = true;
            }
            return this;
        }

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
        @Override
        public<R extends Action.RequestValue, T> IBuilder add(
                Action<R, T> action,
                R requestVal,
                Action.ActionCallback<T> callback,
                IActionScheduler scheduler) {
            if (mQueue == null) {
                mQueue = mActionManager.createQueue();
            }

            buildQueue(action, requestVal, callback, scheduler);
            return this;
        }

        /**
         * Dispatch queue to execute
         * @return
         */
        @Override
        public Builder run() {
            dispatch();
            return this;
        }

        @Override
        public<R extends Action.RequestValue, T> Builder and(
                Action<R, T> action,
                R requestVal,
                Action.ActionCallback<T> callback,
                IActionScheduler scheduler) {
            boolean added = buildQueue(action, requestVal, callback, scheduler);
            if (added && !isDispatched) {
                dispatch();
            }
            return this;
        }

        private  <R extends Action.RequestValue, T> Builder create(
                Action<R, T> action,
                R requestVal,
                Action.ActionCallback<T> callback,
                IActionScheduler scheduler) {
            mQueue = mActionManager.createQueue();
            boolean added = buildQueue(action, requestVal, callback, scheduler);
            if (added) {
                dispatch();
            }

            return this;
        }

        private<R extends Action.RequestValue, T> boolean buildQueue(Action<R, T> action,
                                R requestVal,
                                Action.ActionCallback<T> callback,
                                IActionScheduler scheduler) {
            action.setRequestValue(requestVal);
            if (mActionManager.mActionMonitor.findRunningAction(action.getID()) != null
                    || actionInQueue(action.getID())) {
                Log.e(TAG, "Action is running for ID: " + action.getID());
                return false;
            } else {
                List<ActionCallbackInfo> callbackInfoList = new ArrayList<>();
                if (callback != null) {
                    callbackInfoList.add(mActionManager.makeCallbackInfo(callback));
                }

                // Add registered callback
                List<ActionCallbackInfo> registeredCallback =
                        mActionManager.mActionMonitor.findRegisteredCallback(action.getID());
                if (registeredCallback != null) {
                    callbackInfoList.addAll(registeredCallback);
                }

                mQueue.add(mActionManager.getID(), action, callbackInfoList, scheduler);
                return true;
            }
        }

        private void dispatch() {
            // If no action was add to queue, nothing to dispatch
            if (mQueue == null || mQueue.getActionEnqueueInfoList().isEmpty()) {
                return;
            }
            isDispatched = true;
            mActionManager.mActionMonitor.addActionQueue(mQueue);
            mActionManager.mActionMonitor.dispatch(mQueue);
        }

        private boolean actionInQueue(String actionID) {
            if (mQueue == null) return false;
            ActionQueueInfo actionQueueInfo = mQueue.findActionQueueInfoByID(actionID);
            return actionQueueInfo != null;
        }
    }
}
