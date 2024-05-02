package com.quangph.base.mvp.action.queue;

import android.util.Log;

import com.quangph.base.mvp.action.Action;
import com.quangph.base.mvp.action.ActionCallbackInfo;
import com.quangph.base.mvp.action.ActionQueueInfo;
import com.quangph.base.mvp.action.IActionManager;
import com.quangph.base.mvp.action.CompoundCallback;
import com.quangph.base.mvp.action.RUN_MODE;
import com.quangph.base.mvp.action.scheduler.IActionScheduler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Pham Hai Quang on 6/21/2019.
 */
public abstract class ActionEnqueue {

    private RUN_MODE mRunMode = RUN_MODE.SEQUENCE_MODE;

    private List<ActionQueueInfo> mActionInfoCached = new ArrayList<>();
    private CompoundCallback mCompoundCallback;

    public RUN_MODE getRunMode() {
        return mRunMode;
    }

    public CompoundCallback getCompoundCallback() {
        return mCompoundCallback;
    }

    /**
     * Add an action to queue. Each queue will be manage by ActionManager
     * @param action
     * @param callbackInfoList
     * @param scheduler
     * @param <R>
     * @param <T>
     * @return
     */
    public <R extends Action.RequestValue, T> ActionEnqueue add(String actionManagerID, Action<R, T> action,
                                                                List<ActionCallbackInfo> callbackInfoList,
                                                                IActionScheduler scheduler) {
        ActionQueueInfo info = new ActionQueueInfo();
        info.actionManagerId = actionManagerID;
        info.action = action;
        info.callbackList.addAll(callbackInfoList);
        info.scheduler = scheduler;
        mActionInfoCached.add(info);
        return this;
    }

    public void setRunMode(RUN_MODE mode) {
        mRunMode = mode;
    }

    public void compound(CompoundCallback callback) {
        mCompoundCallback = callback;
        if (mCompoundCallback != null) {
            mCompoundCallback.setActionQueueInfo(mActionInfoCached);
        }
        onCompound(callback);
    }

    public void startAction(Action action) {
        /*if (mParallelCallback != null) {
            int index = -1;
            for (int i = 0; i < mActionInfoCached.size(); i++) {
                if (mActionInfoCached.get(i).action.getID().equals(action.getID())) {
                    index = i;
                    break;
                }
            }
            if (index > -1) {
                try {
                    mParallelCallback.onStart(index, action);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }*/

        action.setStatus(Action.START);
        onStartAction(action);
    }

    /**
     * Call callback of action.
     * @param action
     * @return true if callback is called and this queue need to try to remove the action, false otherwise
     */
    public void error(Action action) {
        action.setStatus(Action.ERROR);
        onErrorAction(action);
    }

    /**
     * Call callback of action.
     * @param action
     */
    public void success(Action action) {
        action.setStatus(Action.SUCCESS);
        onSuccessAction(action);
    }

    public void progress(Action action) {
        onProgressAction(action);
    }

    /**
     * Call when an action is stopped
     * @param action
     */
    public void interrupt(Action action) {
        action.setStatus(Action.INTERRUPT);
        onInterruptAction(action);
        Log.e("ActionEnqueue", "After interrupt action: " + action.getID() + " remain: "
                + mActionInfoCached.size());
    }

    /**
     * Resume action manager. This queue is try to call all callbacks of all action attached to the actionmanager
     * @param actionManager
     */
    public void resume(IActionManager actionManager) {
        onResume(actionManager);
    }

    /**
     * Release callback from cached
     * @param actionManager
     */
    public void releaseImmediately(IActionManager actionManager) {
        removeCallback(actionManager);
        Iterator<ActionQueueInfo> itr = mActionInfoCached.iterator();
        while (itr.hasNext()) {
            ActionQueueInfo next = itr.next();
            if (next.callbackList.isEmpty()) {
                itr.remove();
            }
        }
    }

    /**
     * Remove all callback which attached to the action manager
     * @param actionManager
     */
    public void removeCallback(IActionManager actionManager) {
        Iterator<ActionQueueInfo> itr = mActionInfoCached.iterator();
        while (itr.hasNext()) {
            ActionQueueInfo next = itr.next();
            next.removeCallback(actionManager);

            // In the case action is finished but its action manager is not in resume state,
            // and it has callback, so it is still in action cached. We need remove the action from cache
            // Note: In PARALLEL_MODE we need to cache all actions to waiting for finishing all of them

            if (next.callbackList.isEmpty()) {
                if (getRunMode() != RUN_MODE.PARALLEL_MODE) {
                    if (next.action.isSuccessful()
                            || next.action.isError()
                            || next.action.isInterrupt()) {
                        itr.remove();
                    }
                }
            }
        }

        if (mCompoundCallback != null) {
            mCompoundCallback.removeCallback(actionManager);
            if (isFinishAll()) {
                if (isDetachAllActionManager()) {
                    mActionInfoCached.clear();
                }
            }
        }
    }

    public ActionQueueInfo findActionQueueInfoByID(String actionID) {
        for (ActionQueueInfo info : mActionInfoCached) {
            if (info.action.getID().equals(actionID)) {
                return info;
            }
        }
        return null;
    }

    public List<ActionQueueInfo> getActionEnqueueInfoList() {
        return mActionInfoCached;
    }

    public boolean isFinishAll() {
        boolean isFinished = true;
        for (ActionQueueInfo info : getActionEnqueueInfoList()) {
            if (info.action.getStatus() == Action.PENDING
                    || info.action.getStatus() == Action.RUNNING) {
                isFinished = false;
                break;
            }
        }
        return isFinished;
    }

    public boolean isDetachAllActionManager() {
        boolean isDetach = true;
        for (ActionQueueInfo info : getActionEnqueueInfoList()) {
            if (!info.callbackList.isEmpty()) {
                isDetach = false;
                break;
            }
        }
        return isDetach;
    }

    public abstract void onCompound(CompoundCallback callback);
    public abstract void onStartAction(Action action);
    public abstract void onErrorAction(Action action);
    public abstract void onSuccessAction(Action action);
    public abstract void onProgressAction(Action action);
    public abstract void onInterruptAction(Action action);
    public abstract void onResume(IActionManager actionManager);

    /**
     * Valid queue
     * @return true if the queue is valid, otherwise the queue is candidate for remove from action manager
     */
    public abstract boolean onValid();

    /*private void removeCallback(ActionQueueInfo info, ActionManager actionManager) {
        Iterator<ActionCallbackInfo> itr = info.callbackList.iterator();
        while (itr.hasNext()) {
            ActionCallbackInfo next = itr.next();
            if (next.actionManager.equals(actionManager)) {
                itr.remove();
            }
        }
    }*/
}
