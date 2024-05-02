package com.quangph.base.mvp.action.queue;

import android.util.Log;

import com.quangph.base.mvp.action.Action;
import com.quangph.base.mvp.action.ActionCallbackInfo;
import com.quangph.base.mvp.action.ActionException;
import com.quangph.base.mvp.action.ActionMonitor;
import com.quangph.base.mvp.action.ActionQueueInfo;
import com.quangph.base.mvp.action.CompoundCallback;
import com.quangph.base.mvp.action.IActionManager;
import com.quangph.base.mvp.action.RUN_MODE;

import java.util.Iterator;
import java.util.List;

/**
 * Created by Pham Hai Quang on 6/21/2019.
 */
public class ActionEnqueueImpl extends ActionEnqueue {

    private static final String TAG = "ActionEnqueueImpl";

    @Override
    public void onCompound(CompoundCallback callback) {
    }

    @Override
    public void onStartAction(Action action) {

        try {
            action.start();
            action.canExecute(true);
            ActionQueueInfo info = findActionQueueInfoByID(action.getID());
            if (info != null) {
                for (ActionCallbackInfo callbackInfo : info.callbackList) {
                    try {
                        if (callbackInfo.actionManager.getStatus() == IActionManager.ActionStatus.RESUME_STATE) {
                            callbackInfo.callback.onStart();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if (getCompoundCallback() != null) {
                    int index = -1;
                    for (int i = 0; i < getActionEnqueueInfoList().size(); i++) {
                        if (getActionEnqueueInfoList().get(i).action.getID().equals(action.getID())) {
                            index = i;
                            break;
                        }
                    }
                    if (index > -1) {
                        try {
                            getCompoundCallback().onStart(index, action);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            action.setStatus(Action.RUNNING);
        } catch (ActionException e) {
            e.printStackTrace();
            action.canExecute(false);
            action.setStatus(Action.RUNNING);
            action.setError(e);
            ActionQueueInfo info = findActionQueueInfoByID(action.getID());
            if (info != null) {
                for (ActionCallbackInfo callbackInfo : info.callbackList) {
                    try {
                        if (callbackInfo.actionManager.getStatus() == IActionManager.ActionStatus.RESUME_STATE) {
                            callbackInfo.callback.onStartError(e);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

                if (getCompoundCallback() != null) {
                    getCompoundCallback().onError();
                }
                getActionEnqueueInfoList().remove(info);
            }
        }
    }

    /**
     * Just show error and remove callback from actionEnqueueInfo for actionManager which has status = resume.
     * It mean when the action completed, maybe callback will remain in cache if its action manager state != resume
     * @param action
     */
    @Override
    public void onErrorAction(Action action) {
        ActionQueueInfo info = findActionQueueInfoByID(action.getID());
        RUN_MODE mode = getRunMode();
        if (mode == RUN_MODE.SEQUENCE_MODE) {
            showError(info);
            removeActionQueueInfoIfNeed(info);
            runNextAction();
        } else if (mode == RUN_MODE.PARALLEL_MODE) {
            showError(info);
            if (isFinishAll()) {
                showAllResult();
            }
        }

        Log.e(TAG, "After error action size: " + getActionEnqueueInfoList().size());
    }

    @Override
    public void onSuccessAction(Action action) {
        ActionQueueInfo info = findActionQueueInfoByID(action.getID());
        RUN_MODE mode = getRunMode();
        if (mode == RUN_MODE.SEQUENCE_MODE) {
            showResponse(info);
            removeActionQueueInfoIfNeed(info);
            runNextAction();
        } else if (mode == RUN_MODE.PARALLEL_MODE) {
            // Run callback
            showResponse(info);

            // Collect data of all action in queue
            if (isFinishAll()) {
                showAllResult();
            }
        }

        Log.e(TAG, "After success action size: " + getActionEnqueueInfoList().size());
    }

    @Override
    public void onProgressAction(Action action) {
        ActionQueueInfo info = findActionQueueInfoByID(action.getID());
        if (info != null) {
            showProgress(info);
        }
    }

    @Override
    public void onInterruptAction(Action action) {
        ActionQueueInfo info = findActionQueueInfoByID(action.getID());
        RUN_MODE mode = getRunMode();
        if (mode == RUN_MODE.SEQUENCE_MODE) {
            showInterrupt(info);
            removeActionQueueInfoIfNeed(info);
            runNextAction();
        } else if (mode == RUN_MODE.PARALLEL_MODE) {
            showInterrupt(info);
            if (isFinishAll()) {
                showAllResult();
            }
        }
    }

    @Override
    public void onResume(IActionManager actionManager) {
        // Call callback on each action info in queue
        Iterator<ActionQueueInfo> itr = getActionEnqueueInfoList().iterator();
        while (itr.hasNext()) {
            ActionQueueInfo next = itr.next();
            showResultOfActionQueueInfoWhenResume(next, actionManager);
            if (getRunMode() != RUN_MODE.PARALLEL_MODE) {
                if (next.callbackList.isEmpty()) {
                    itr.remove();
                }
            }
        }

        if (getRunMode() == RUN_MODE.PARALLEL_MODE) {
            if (isFinishAll()) {
                CompoundCallback compoundCallback = getCompoundCallback();
                if (compoundCallback != null) {
                    if (compoundCallback.getActionManager().equals(actionManager)) {
                        showAllResult();
                    }
                } else {
                    cleanQueue();
                }
            }
        }
    }

    @Override
    public boolean onValid() {
        return !getActionEnqueueInfoList().isEmpty();
    }

    private void runNextAction() {
        /*mCurrentIndex++;
        if (mCurrentIndex < mActionInfoCached.size()) {
            mActionManager.dispatchAction(mActionInfoCached.get(mCurrentIndex));
        }*/
        ActionMonitor.getInstance().dispatch(this);
    }

    private void showError(ActionQueueInfo info) {
        Iterator<ActionCallbackInfo> itr = info.callbackList.iterator();
        ActionCallbackInfo callbackInfo;
        while (itr.hasNext()) {
            callbackInfo = itr.next();
            if (callbackInfo.actionManager.getStatus() == IActionManager.ActionStatus.RESUME_STATE) {
                try {
                    callbackInfo.callback.onError(info.action.getError());
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    itr.remove();
                }
            }
        }
    }

    private void showResponse(ActionQueueInfo actionInfo) {
        Iterator<ActionCallbackInfo> itr = actionInfo.callbackList.iterator();
        ActionCallbackInfo callbackInfo;
        while (itr.hasNext()) {
            callbackInfo = itr.next();
            if (callbackInfo.actionManager.getStatus() == IActionManager.ActionStatus.RESUME_STATE) {
                try {
                    callbackInfo.callback.onSuccess(actionInfo.action.getResponseVal());
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    itr.remove();
                }
            }
        }
    }

    private void showInterrupt(ActionQueueInfo info) {
        Iterator<ActionCallbackInfo> itr = info.callbackList.iterator();
        ActionCallbackInfo callbackInfo;
        while (itr.hasNext()) {
            callbackInfo = itr.next();
            if (callbackInfo.actionManager.getStatus() == IActionManager.ActionStatus.RESUME_STATE) {
                try {
                    callbackInfo.callback.onInterrupt();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    itr.remove();
                }
            }
        }
    }

    private void showProgress(ActionQueueInfo actionInfo) {
        Iterator<ActionCallbackInfo> itr = actionInfo.callbackList.iterator();
        ActionCallbackInfo callbackInfo;
        while (itr.hasNext()) {
            callbackInfo = itr.next();
            if (callbackInfo.actionManager.getStatus() == IActionManager.ActionStatus.RESUME_STATE) {
                try {
                    callbackInfo.callback.onPublishProgress(actionInfo.action.getCurrentEvent());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void showAllResult() {
        CompoundCallback callback = getCompoundCallback();
        if (callback != null) {
            if (callback.getActionManager().getStatus() == IActionManager.ActionStatus.RESUME_STATE) {
                try {
                    if (isSuccessAll()) {
                        callback.onCompletedAll();
                    } else {
                        List<Action> interrupted = callback.getInterruptedActions();
                        if (!interrupted.isEmpty()) {
                            callback.onInterrupt(interrupted);
                        }
                        callback.onError();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    // Still all action are completed, try to remove action from queue
                    // if action manager is in resume state
                    cleanQueue();
                }
            }
        } else {
            cleanQueue();
        }
    }

    private void showResultOfActionQueueInfoWhenResume(ActionQueueInfo actionInfo, IActionManager actionManager) {
        Iterator<ActionCallbackInfo> itr = actionInfo.callbackList.iterator();
        ActionCallbackInfo callbackInfo;
        while (itr.hasNext()) {
            callbackInfo = itr.next();
            if (callbackInfo.actionManager.equals(actionManager)) {
                try {
                    if (actionInfo.action.isSuccessful()) {
                        callbackInfo.callback.onSuccess(actionInfo.action.getResponseVal());
                    } else if (actionInfo.action.isError()) {
                        callbackInfo.callback.onError(actionInfo.action.getError());
                    } else if (actionInfo.action.isInterrupt()) {
                        callbackInfo.callback.onInterrupt();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (actionInfo.action.isSuccessful()
                            || actionInfo.action.isError()
                            || actionInfo.action.isInterrupt()) {
                        itr.remove();
                    }
                }
            }
        }
    }

    private void removeActionQueueInfoIfNeed(ActionQueueInfo info) {
        if (info.callbackList.isEmpty()) {
            getActionEnqueueInfoList().remove(info);
        }
    }

    private boolean isSuccessAll() {
        boolean isSuccess = true;
        for (ActionQueueInfo info : getActionEnqueueInfoList()) {
            if (info.action.getStatus() != Action.SUCCESS) {
                isSuccess = false;
                break;
            }
        }
        return isSuccess;
    }

    private void cleanQueue() {
        Iterator<ActionQueueInfo> itr = getActionEnqueueInfoList().iterator();
        while (itr.hasNext()) {
            ActionQueueInfo next = itr.next();
            if (next.callbackList.isEmpty()) {
                itr.remove();
            }
        }
    }
}
