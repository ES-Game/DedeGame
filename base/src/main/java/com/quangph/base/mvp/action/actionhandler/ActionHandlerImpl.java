package com.quangph.base.mvp.action.actionhandler;

import android.util.Log;

import com.quangph.base.dispatcher.BaseDispatcher;
import com.quangph.base.dispatcher.IDispatcher;
import com.quangph.base.mvp.action.Action;
import com.quangph.base.mvp.action.ActionCallbackInfo;
import com.quangph.base.mvp.action.ActionQueueInfo;
import com.quangph.base.mvp.action.IActionManager;
import com.quangph.base.mvp.action.queue.ActionEnqueue;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Pham Hai Quang on 6/22/2019.
 */
public class ActionHandlerImpl implements IActionHandler {
    private static String TAG = "ActionHandlerImpl";

    private List<ActionEnqueue> mActionQueueCache;
    private IDispatcher<ActionDispatcherInfo> mDispatcher = new CallbackDispatcher();

    public ActionHandlerImpl(List<ActionEnqueue> actionEnqueueList) {
        mActionQueueCache = actionEnqueueList;
        mDispatcher.start();
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    synchronized public void dispatch(ActionDispatcherInfo event) {
        mDispatcher.dispatch(event);
    }

    @Override
    synchronized public void dispatchAtFrontOfQueue(ActionDispatcherInfo event) {
        mDispatcher.dispatchAtFrontOfQueue(event);
    }

    @Override
    synchronized public void dispatchToEndOfQueue(ActionDispatcherInfo event) {
        mDispatcher.dispatchToEndOfQueue(event);
    }

    private static void loge(String msg) {
        Log.e(TAG, msg);
    }


    private class CallbackDispatcher extends BaseDispatcher<ActionDispatcherInfo> {

        @Override
        public void handleEvent(ActionDispatcherInfo event) throws Exception {
            switch (event.what) {
                case RESUME:
                    resume((IActionManager) event.obj);
                    break;
                case START_ACTION:
                    Action action = (Action) event.obj;
                    notifyStartAction(action);
                    break;
                case SHOW_RESULT:
                    Action resultAction = (Action) event.obj;
                    if (!resultAction.isInterrupt()) {
                        notifySuccessAction(resultAction);
                    }
                    break;
                case SHOW_ERROR:
                    Action errorAction = (Action) event.obj;
                    if (!errorAction.isInterrupt()) {
                        notifyErrorAction(errorAction);
                    }
                    break;
                case SHOW_PROGRESS:
                    Action progressAction = (Action) event.obj;
                    if (!progressAction.isInterrupt()) {
                        notifyProgress(progressAction);
                    }
                    break;
                case STOP_ACTION:
                    Action stopAction = (Action) event.obj;
                    stopAction(stopAction);
                    break;
                case STOP_ACTION_BY_ID:
                    stopAction((String) event.obj);
                    break;
                case STOP_ACTION_BY_TYPE:
                    stopAction((Class) event.obj);
                    break;

                case STOP_ALL_ACTION:
                    stopActions((IActionManager) event.obj);
                    break;
                case RELEASE_IMMEDIATE:
                    releaseAllActionImmediately((IActionManager) event.obj);
                    break;
                case POST_RUNNABLE:
                    Runnable runnable = (Runnable) event.obj;
                    runnable.run();
                    break;
            }
        }

        private void notifyStartAction(Action action) {
            ActionEnqueue enqueue = findActionEnqueueByAction(action);
            if (enqueue != null) {
                enqueue.startAction(action);
            } else {
                loge("Can not find any action enqueue!!!");
            }
        }

        private void notifyErrorAction(Action action) {
            ActionEnqueue enqueue = findActionEnqueueByAction(action);
            if (enqueue != null) {
                enqueue.error(action);
                if (!enqueue.onValid()) {
                    removeActionEnqueueFromCached(enqueue);
                }
            } else {
                loge("Can not find any action enqueue!!!");
            }
        }

        private void notifySuccessAction(Action action) {
            Log.e(TAG, "SUCCESS: " + action.getID());
            ActionEnqueue enqueue = findActionEnqueueByAction(action);
            if (enqueue != null) {
                Log.e(TAG, "ActionEnqueue size: success");
                enqueue.success(action);
                if (!enqueue.onValid()) {
                    Log.e(TAG, "ActionEnqueue size: removing");
                    removeActionEnqueueFromCached(enqueue);
                }
                Log.e(TAG, "ActionEnqueue size: " + mActionQueueCache.size());
            } else {
                loge("Can not find any action enqueue!!!");
            }
        }

        private void notifyProgress(Action action) {
            ActionEnqueue enqueue = findActionEnqueueByAction(action);
            if (enqueue != null) {
                enqueue.progress(action);
            } else {
                loge("Can not find any action enqueue!!!");
            }
        }

        private void notifyInterrupt(Action action) {
            ActionEnqueue enqueue = findActionEnqueueByAction(action);
            if (enqueue != null) {
                enqueue.interrupt(action);
                if (!enqueue.onValid()) {
                    removeActionEnqueueFromCached(enqueue);
                }
                Log.e(TAG, "After interrupt action : " + action.getID()
                        + " remain: " + mActionQueueCache.size());
            } else {
                loge("Can not find any action enqueue!!!");
            }
        }

        private void resume(IActionManager actionManager) {
            Iterator<ActionEnqueue> itr = mActionQueueCache.iterator();
            while (itr.hasNext()) {
                try {
                    ActionEnqueue next = itr.next();
                    next.resume(actionManager);
                    if (!next.onValid()) {
                        itr.remove();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            Log.e(TAG, "Action cache size after resume:" + mActionQueueCache.size());
        }

        private void stopAction(Action action) {
            ActionQueueInfo actionInfo = findActionInfoByAction(action);
            if (actionInfo != null) {
                stopAction(actionInfo);
            }
        }

        private void stopAction(String actionID) {
            ActionQueueInfo actionInfo = findActionInfoByActionID(actionID);
            if (actionInfo != null) {
                stopAction(actionInfo);
            }
        }

        private void stopAction(Class actionType) {
            List<ActionQueueInfo> actionInfoList = findActionByType(actionType);
            for (ActionQueueInfo info : actionInfoList) {
                stopAction(info);
            }
        }

        private void stopAction(ActionQueueInfo actionInfo) {
            actionInfo.action.setStatus(Action.INTERRUPT);
            actionInfo.scheduler.stop(actionInfo.action);
            Log.e(TAG, "Cancelling action: " + actionInfo.action.getClass().getName() + " with ID: " + actionInfo.action.getID());
            notifyInterrupt(actionInfo.action);
        }

        private void stopActions(IActionManager actionManager) {
            List<ActionQueueInfo> actions = findActionsRelatedActionManager(actionManager);
            for (ActionQueueInfo info : actions) {
                stopAction(info);
            }
        }

        private void releaseAllActionImmediately(IActionManager actionManager) {
            List<ActionQueueInfo> actions = findActionsRelatedActionManager(actionManager);
            for (ActionQueueInfo info : actions) {
                releaseAction(info, actionManager);
            }
            Log.e(TAG, "Action queue size after releasing immediately an action manager: "
                    + mActionQueueCache.size());
        }

        private void releaseAction(ActionQueueInfo actionInfo, IActionManager actionManager) {
            // Just Action Manager which execute Action has the permission to stop that action
            // The Action Manager which have not the permission to stop that action, just remove
            // callback related that Action Manager from cache

            if (actionInfo.isOwner(actionManager)) {
                actionInfo.action.setStatus(Action.INTERRUPT);
                actionInfo.scheduler.stop(actionInfo.action);
                Log.e(TAG, "Releasing action: " + actionInfo.action.getClass().getName()
                        + " with ID: " + actionInfo.action.getID());
            }

            //Remove callback
            ActionEnqueue enqueue = findActionEnqueueByAction(actionInfo.action);
            if (enqueue != null) {
                enqueue.releaseImmediately(actionManager);
                if (!enqueue.onValid()) {
                    removeActionEnqueueFromCached(enqueue);
                }
            }
        }

        private ActionQueueInfo findActionInfoByAction(Action action) {
            return findActionInfoByActionID(action.getID());
        }

        private ActionQueueInfo findActionInfoByActionID(String actionID) {
            ActionQueueInfo result = null;
            for (ActionEnqueue info : mActionQueueCache) {
                result = info.findActionQueueInfoByID(actionID);
                if (result != null) {
                    break;
                }
            }
            return result;
        }

        private ActionEnqueue findActionEnqueueByAction(Action action) {
            ActionEnqueue result = null;
            for (ActionEnqueue info : mActionQueueCache) {
                if (info.findActionQueueInfoByID(action.getID()) != null) {
                    result = info;
                    break;
                }
            }
            return result;
        }

        private List<ActionQueueInfo> findActionByType(Class actionType) {
            List<ActionQueueInfo> results = new ArrayList<>();
            for (ActionEnqueue queue : mActionQueueCache) {
                for (ActionQueueInfo info : queue.getActionEnqueueInfoList()) {
                    if (info.action.getClass().equals(actionType)) {
                        results.add(info);
                    }
                }
            }
            return results;
        }

        private List<ActionQueueInfo> findActionsRelatedActionManager(IActionManager actionManager) {
            List<ActionQueueInfo> results = new ArrayList<>();
            for (ActionEnqueue queue : mActionQueueCache) {
                for (ActionQueueInfo info : queue.getActionEnqueueInfoList()) {
                    if (info.hasCallbackRelatedToActionManager(actionManager)) {
                        results.add(info);
                    }
                }
            }
            return results;
        }

        private void showError(ActionQueueInfo actionInfo) {
            Iterator<ActionCallbackInfo> itr = actionInfo.callbackList.iterator();
            ActionCallbackInfo callbackInfo;
            while (itr.hasNext()) {
                callbackInfo = itr.next();
                if (callbackInfo.actionManager.getStatus() == IActionManager.ActionStatus.RESUME_STATE) {
                    callbackInfo.callback.onError(actionInfo.action.getError());
                    itr.remove();
                }
            }
        }

        private void removeActionFromCacheIfNeed(ActionQueueInfo info) {
            if (canRemoveActionEnqueueFromCache(info)) {
                mActionQueueCache.remove(info);
            }
        }

        private boolean canRemoveActionEnqueueFromCache(ActionQueueInfo actionInfo) {
            return actionInfo.callbackList.size() == 0;
        }

        private void removeActionEnqueueFromCached(ActionEnqueue queue) {
            mActionQueueCache.remove(queue);
        }
    }
}
