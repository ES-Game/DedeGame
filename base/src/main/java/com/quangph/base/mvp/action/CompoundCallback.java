package com.quangph.base.mvp.action;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pham Hai Quang on 6/22/2019.
 */
public class CompoundCallback {

    private List<ActionQueueInfo> mActionInfoCached = new ArrayList<>();
    private IActionManager mActionManager;

    public void onStart(int index, Action action){}
    public void onPublishProgress(Object event, int index, Action action){}
    public void onCompletedAll(){}
    public void onError(){};
    public void onInterrupt(List<Action> interruptedActions){}

    public void setActionQueueInfo(List<ActionQueueInfo> actionInfoCached) {
        mActionInfoCached = actionInfoCached;
    }

    public void setActionManager(IActionManager actionManager) {
        this.mActionManager = actionManager;
    }

    public IActionManager getActionManager() {
        return mActionManager;
    }

    public int getResultCount() {
        return mActionInfoCached.size();
    }

    public Object getResultAtIndex(int index) {
        return mActionInfoCached.get(index).action.getResponseVal();
    }

    public @Nullable ActionException getErrorAtIndex(int index) {
        return mActionInfoCached.get(index).action.getError();
    }

    public int getStatusAtIndex(int index) {
        return mActionInfoCached.get(index).action.getStatus();
    }

    public List<Action> getInterruptedActions() {
        List<Action> results = new ArrayList<>();
        for (ActionQueueInfo info : mActionInfoCached) {
            if (info.action.isInterrupt()) {
                results.add(info.action);
            }
        }
        return results;
    }

    public void release() {
        mActionInfoCached.clear();
    }

    public void removeCallback(IActionManager actionManager) {
        for (ActionQueueInfo info : mActionInfoCached) {
            info.removeCallback(actionManager);
        }
    }
}
