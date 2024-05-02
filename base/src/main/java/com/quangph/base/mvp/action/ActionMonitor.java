package com.quangph.base.mvp.action;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.quangph.base.mvp.action.actionhandler.ActionHandlerImpl;
import com.quangph.base.mvp.action.actionhandler.IActionHandler;
import com.quangph.base.mvp.action.dispatcher.HandlerDispatcher;
import com.quangph.base.mvp.action.dispatcher.IEnqueueDispatcher;
import com.quangph.base.mvp.action.queue.ActionEnqueue;
import com.quangph.base.mvp.action.queue.ActionEnqueueImpl;
import com.quangph.base.mvp.action.scheduler.IActionScheduler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by QuangPH on 2020-10-20.
 */
public class ActionMonitor {

    private final List<ActionEnqueue> mActionEnqueueCache = new ArrayList<>();
    private final Map<String, List<ActionCallbackInfo>> mCallbackCache = new ConcurrentHashMap<>();
    private IActionHandler mActionHandler = new ActionHandlerImpl(mActionEnqueueCache);
    private IEnqueueDispatcher mDispatcher = new HandlerDispatcher(); //new DispatcherImpl();

    private static class InstanceHolder {
        private static final ActionMonitor INSTANCE = new ActionMonitor();
    }

    public static synchronized ActionMonitor getInstance() {
        return InstanceHolder.INSTANCE;
    }

    public void notifyStartAction(Action action) {
        mActionHandler.dispatch(obtainActionDispatcherInfo(IActionHandler.START_ACTION, action));
    }

    public void notifyErrorAction(Action action) {
        mActionHandler.dispatch(obtainActionDispatcherInfo(IActionHandler.SHOW_ERROR, action));
    }

    public void notifySuccessAction(Action action) {
        mActionHandler.dispatch(obtainActionDispatcherInfo(IActionHandler.SHOW_RESULT, action));
    }

    public void notifyProgress(Action action) {
        mActionHandler.dispatch(obtainActionDispatcherInfo(IActionHandler.SHOW_PROGRESS, action));
    }

    public void dispatch(ActionEnqueue enqueue) {
        mDispatcher.dispatch(enqueue);
    }

    public void resume(IActionManager actionManager) {
        mActionHandler.dispatchAtFrontOfQueue(obtainActionDispatcherInfo(IActionHandler.RESUME, actionManager));
    }

    public void removeAllRegisteredCallbacksFromActionManager(IActionManager actionManager) {
        Iterator<Map.Entry<String, List<ActionCallbackInfo>>> callbackItr = mCallbackCache.entrySet().iterator();
        while (callbackItr.hasNext()) {
            Map.Entry<String, List<ActionCallbackInfo>> next = callbackItr.next();
            Iterator<ActionCallbackInfo> infoIterator = next.getValue().iterator();
            while (infoIterator.hasNext()) {
                ActionCallbackInfo nextInfo = infoIterator.next();
                if (nextInfo.isSameActionManager(actionManager)) {
                    infoIterator.remove();
                }
            }

            if (next.getValue().isEmpty()) {
                callbackItr.remove();
            }
        }
    }

    public void removeCallbackFromActionManager(IActionManager actionManager) {
        Iterator<ActionEnqueue> queueItr = mActionEnqueueCache.iterator();
        while (queueItr.hasNext()) {
            ActionEnqueue next = queueItr.next();
            next.removeCallback(actionManager);
            if (!next.onValid()) {
                queueItr.remove();
            }
        }
    }

    public void releaseAllActionFromActionManager(IActionManager actionManager) {
        mActionHandler.dispatchAtFrontOfQueue(obtainActionDispatcherInfo(IActionHandler.RELEASE_IMMEDIATE, actionManager));
    }

    public void stopAction(Action action) {
        mActionHandler.dispatchAtFrontOfQueue(obtainActionDispatcherInfo(IActionHandler.STOP_ACTION, action));
    }

    public void stopAction(String actionID) {
        mActionHandler.dispatchAtFrontOfQueue(obtainActionDispatcherInfo(IActionHandler.STOP_ACTION_BY_ID, actionID));
    }

    public void stopAction(Class<? extends Action> actionType) {
        mActionHandler.dispatchAtFrontOfQueue(obtainActionDispatcherInfo(IActionHandler.STOP_ACTION_BY_TYPE, actionType));
    }

    public void executeActionOnPost(Runnable runnable) {
        mActionHandler.dispatch(obtainActionDispatcherInfo(IActionHandler.POST_RUNNABLE, runnable));
    }

    public boolean callbackHasRegistered(@NonNull String actionID) {
        return mCallbackCache.containsKey(actionID);
    }

    synchronized public void addActionCallbackInfo(IActionManager actionManager,
                                                   @NonNull String actionID,
                                                   @NonNull ActionCallbackInfo info) {
        List<ActionCallbackInfo> callbackInfoList = mCallbackCache.get(actionID);
        if (callbackInfoList == null) {
            callbackInfoList = new ArrayList<>();
            mCallbackCache.put(actionID, callbackInfoList);
        }

        // Each Action Manager has only one callback which is attached to actionID
        if (callbackInfoList.isEmpty()) {
            callbackInfoList.add(info);
        } else {
            boolean hasRegistered = false;
            for (ActionCallbackInfo ai : callbackInfoList) {
                hasRegistered = ai.isSameActionManager(actionManager);
                if (hasRegistered) {
                    break;
                }
            }
            if (!hasRegistered) {
                callbackInfoList.add(info);
            } else {
                Log.e("ActionMonitor", "Action Manager has registered for id: " + actionID);
            }
        }
    }

    public @Nullable ActionQueueInfo findRunningAction(@NonNull String actionID) {
        for (ActionEnqueue queue : mActionEnqueueCache) {
            ActionQueueInfo actionQueueInfo = queue.findActionQueueInfoByID(actionID);
            if (actionQueueInfo != null) {
                return actionQueueInfo;
            }
        }
        return null;
    }

    public void addActionQueue(ActionEnqueue queue) {
        mActionEnqueueCache.add(queue);
    }

    public ActionEnqueue createEnqueue() {
        return new ActionEnqueueImpl();
    }

    /**
     * Enqueue an action with registered callback
     * @param action
     * @param scheduler
     */
    /*synchronized public void enqueueAction(@NonNull Action action, IActionScheduler scheduler) {
        if (findRunningAction(action.getID()) != null) {
            Log.e("ActionMonitor", "Action is running for ID: " + action.getID());
        } else {
            ActionEnqueue enqueue = createEnqueue();
            enqueue.add(action, findCallbacksRelatedAction(action.getID()), scheduler);
            addActionQueue(enqueue);
            dispatch(enqueue);
        }
    }*/

    synchronized public void enqueueAction(String actionManagerID,
                                           @NonNull Action action,
                                           List<ActionCallbackInfo> callbackInfoList,
                                           IActionScheduler scheduler) {
        if (findRunningAction(action.getID()) != null) {
            Log.e("ActionMonitor", "Action is running for ID: " + action.getID());
        } else {
            ActionEnqueue enqueue = createEnqueue();
            enqueue.add(actionManagerID, action, callbackInfoList, scheduler);
            addActionQueue(enqueue);
            dispatch(enqueue);
        }
    }

    synchronized public List<ActionCallbackInfo> findCallbacksRelatedAction(String actionID) {
        return mCallbackCache.get(actionID);
    }

    /*@Deprecated
    public Action.ActionCallback findRegisteredCallback(IActionManager actionManager, String actionID) {
        List<ActionCallbackInfo> callbackInfoList = mCallbackCache.get(actionID);
        if (callbackInfoList != null) {
            for (ActionCallbackInfo info : callbackInfoList) {
                if (info.actionManager.equals(actionManager)) {
                    return info.callback;
                }
            }
        }
        return null;
    }
*/
    public List<ActionCallbackInfo> findRegisteredCallback(String actionID) {
        return mCallbackCache.get(actionID);
    }

    private static IActionHandler.ActionDispatcherInfo obtainActionDispatcherInfo(int what, Object obj) {
        IActionHandler.ActionDispatcherInfo info = new IActionHandler.ActionDispatcherInfo();
        info.what = what;
        info.obj = obj;
        return info;
    }
}
