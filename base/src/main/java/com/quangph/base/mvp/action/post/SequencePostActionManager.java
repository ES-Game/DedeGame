package com.quangph.base.mvp.action.post;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.quangph.base.mvp.action.ACTION_POST_POLICY;
import com.quangph.base.mvp.action.Action;
import com.quangph.base.mvp.action.ActionCallbackInfo;
import com.quangph.base.mvp.action.ActionMonitor;
import com.quangph.base.mvp.action.ActionQueueInfo;
import com.quangph.base.mvp.action.IActionManager;
import com.quangph.base.thread.PostableThread;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@SuppressWarnings("rawtypes")
public class SequencePostActionManager implements IPostActionManager {

    private static SequencePostActionManager sInstance;

    private List<ActionInfo> mActionInfoCache = new ArrayList<>();
    private Handler mUIHandler;
    private PostableThread mThread;
    private IPostIDGenerator mIDGenerator = new PostActionIDGenerator();

    public static SequencePostActionManager getInstance() {
        if (sInstance == null) {
            synchronized (SequencePostActionManager.class) {
                if (sInstance == null) {
                    sInstance = new SequencePostActionManager();
                }
            }
        }
        return sInstance;
    }

    @Override
    synchronized public <R extends Action.RequestValue, T> void post(IActionManager actionManager,
                                                        @NotNull Action<R, T> action,
                                                        R requestVal,
                                                        Action.ActionCallback<T> callback,
                                                        long timeInMillis,
                                                        ACTION_POST_POLICY policy) {
        action.setRequestValue(requestVal);

        // Only one Action is in queue
        if (!canPost(action)) {
            Log.e("SequencePostAction",
                    "There is at least one action in post queue with ID: " + action.getID());

            //Maybe many screen post the same action, we need cache callback for each screen
            // in order to update screen when action finish its job
            // One screen has only one callback
            if (!hasCachedActionManager(actionManager)) {
                mActionInfoCache.add(collectInfo(actionManager, action));
                registerCallback(actionManager, action.getID(), callback);
            }
        } else {
            mActionInfoCache.add(collectInfo(actionManager, action));
            initInfoIfNeed();
            actionManager.registerCallback(action.getID(), callback);
            List<ActionCallbackInfo> callbackList = ActionMonitor.getInstance()
                    .findCallbacksRelatedAction(action.getID());
            if (callbackList == null) {
                callbackList = new ArrayList<>();
            }

            ActionMonitor.getInstance().enqueueAction(actionManager.getID(),
                    new PostAction(action, mIDGenerator.generate(action)),
                    callbackList,
                    new PostTaskScheduler(mUIHandler, mThread, mIDGenerator, actionManager.getID(),
                            timeInMillis, policy));
        }
    }

    @Override
    public <R extends Action.RequestValue, T> void registerPostCallback(IActionManager actionManager,
                                                                        String actionID,
                                                                        Action.ActionCallback<T> callback) {
        registerCallback(actionManager, actionID, callback);
    }

    @Override
    public void pause(IActionManager manager) {

    }

    @Override
    public void resume(IActionManager manager) {

    }

    @Override
    synchronized public void release(IActionManager manager) {
        Iterator<ActionInfo> itr = mActionInfoCache.iterator();
        while (itr.hasNext()) {
            ActionInfo ai = itr.next();
            if (ai.isSameActionManager(manager)) {
                itr.remove();
                break;
            }
        }
    }

    @Override
    synchronized public void releaseAndStopAllAction(IActionManager manager) {
        Iterator<ActionInfo> itr = mActionInfoCache.iterator();
        while (itr.hasNext()) {
            ActionInfo ai = itr.next();
            if (ai.isSameActionManager(manager)) {
                mIDGenerator.remove(ai.actionID);
                if (mThread != null) {
                    mThread.clearAllTask();
                    mThread.quit();
                    mThread = null;
                }
                itr.remove();
                break;
            }
        }
    }

    private void initInfoIfNeed() {
        if (mUIHandler == null) {
            mUIHandler = new Handler(Looper.getMainLooper());
        }

        if (mThread == null) {
            mThread = new PostableThread("SequencePostActionManager");
            mThread.start();
        }
    }

    private boolean canPost(Action action) {
        boolean canPost = true;
        for (ActionInfo info : mActionInfoCache) {
            if (info.isSameAction(action)) {
                canPost = false;
                break;
            }
        }
        return canPost;
    }

    private boolean hasCachedActionManager(IActionManager actionManager) {
        boolean canCache = false;
        for (ActionInfo info : mActionInfoCache) {
            if (info.isSameActionManager(actionManager)) {
                canCache = true;
                break;
            }
        }

        return canCache;
    }

    private ActionInfo findActionInfo(Action action) {
        ActionInfo info = null;
        for (ActionInfo ai : mActionInfoCache) {
            if (info.isSameAction(action)) {
                info = ai;
                break;
            }
        }
        return info;
    }

    private ActionInfo collectInfo(IActionManager actionManager,
                                   Action action) {
        ActionInfo info = new ActionInfo();
        info.actionID = action.getID();
        info.manager = actionManager;
        return info;
    }

    private void registerCallback(IActionManager actionManager,
                                  @NotNull String actionID,
                                  Action.ActionCallback callback) {
        actionManager.registerCallback(actionID, callback);
        // Attach to running action
        String currentID = mIDGenerator.getCurrentID(actionID);
        if (currentID != null) {
            ActionQueueInfo runningActionQueue = ActionMonitor.getInstance().findRunningAction(currentID);
            if (runningActionQueue != null) {
                ActionCallbackInfo info = new ActionCallbackInfo();
                info.actionManager = actionManager;
                info.callback = callback;
                //info.managerOwnAction = true;
                runningActionQueue.callbackList.add(info);
            }
        }
    }

    private static class ActionInfo {
        public IActionManager manager;
        public String actionID;

        public boolean isSameAction(@NotNull Action other) {
            return this.actionID.equals(other.getID());
        }

        public boolean isSameActionManager(IActionManager actionManager) {
            return this.manager.equals(actionManager);
        }
    }
}
