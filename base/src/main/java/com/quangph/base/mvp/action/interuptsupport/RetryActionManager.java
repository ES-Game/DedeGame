package com.quangph.base.mvp.action.interuptsupport;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import com.quangph.base.mvp.action.ACTION_POST_POLICY;
import com.quangph.base.mvp.action.Action;
import com.quangph.base.mvp.action.ActionManager;
import com.quangph.base.mvp.action.IActionManager;
import com.quangph.base.mvp.action.IBuilder;
import com.quangph.base.mvp.action.interuptsupport.condition.INeedRetryCondition;
import com.quangph.base.mvp.action.interuptsupport.condition.NeedRetryConditionDefault;
import com.quangph.base.mvp.action.interuptsupport.retrystrategy.IRebuildActionStrategy;
import com.quangph.base.mvp.action.interuptsupport.retrystrategy.RebuildActionStrategyDefault;
import com.quangph.base.mvp.action.scheduler.AsyncTaskScheduler;
import com.quangph.base.mvp.action.scheduler.IActionScheduler;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by QuangPH on 2020-09-26.
 */
public class RetryActionManager implements IActionManager {
    private final ActionManager mActionManager;
    private volatile boolean needToRetry;
    private final List<ActionNode> mActionNodeCache = new CopyOnWriteArrayList<ActionNode>();
    private final RetryHandler mHandler;
    private final INeedRetryCondition mCondition;

    public RetryActionManager(String id) {
        mActionManager = ActionManager.get(id);
        mHandler = new RetryHandler(this);
        mHandler.strategy = new RebuildActionStrategyDefault();
        mCondition = new NeedRetryConditionDefault();
    }

    public void setRetry(boolean retry) {
        needToRetry = retry;
    }

    public boolean autoRetry() {
        return needToRetry;
    }

    @Override
    public String getID() {
        return mActionManager.getID();
    }

    @Override
    public int getStatus() {
        return mActionManager.getStatus();
    }

    @Override
    public void pause() {
        mActionManager.pause();
    }

    @Override
    public void resume() {
        mActionManager.resume();
    }

    @Override
    public void release() {
        if (mActionNodeCache.size() > 0) {
            Log.e("SplashScreenActivity", "Release retry cache: " + mActionNodeCache.size());
            Log.e("SplashScreenActivity", "Release retry cache: " + mActionNodeCache.get(0).getData().getID());
        }

        doRelease();
        mActionManager.release();
    }

    @Override
    public void releaseAndStopAllAction() {
        doRelease();
        mActionManager.releaseAndStopAllAction();
    }

    @Override
    public void stopAction(Action action) {
        mActionManager.stopAction(action);
    }

    @Override
    public void stopAction(String actionID) {
        mActionManager.stopAction(actionID);
    }

    @Override
    public void stopAction(Class<? extends Action> actionType) {
        mActionManager.stopAction(actionType);
    }

    @Override
    public <T> IBuilder executeAction(Action<Action.VoidRequest, T> action, Action.ActionCallback<T> callback) {
        return executeAction(action, new Action.VoidRequest(), callback);
    }

    @Override
    public <R extends Action.RequestValue, T> IBuilder executeAction(Action<R, T> action, R requestVal, Action.ActionCallback<T> callback) {
        return executeAction(action, requestVal, callback, new AsyncTaskScheduler());
    }

    @Override
    public<R extends Action.RequestValue, T> RetryActionBuilder executeAction(
            Action<R, T> action,
            R requestVal,
            Action.ActionCallback<T> callback,
            IActionScheduler scheduler) {
        RetryActionBuilder builder = new RetryActionBuilder(this);
        builder.executeAction(action, requestVal, callback, scheduler);
        mActionNodeCache.add(builder.getRoot());
        return builder;
    }

    @Override
    public <R extends Action.RequestValue, T> void executeActionOnPost(Action<R, T> action, R requestVal, Action.ActionCallback<T> callback, IActionScheduler scheduler) {
        ActionNode node = new ActionNode(action);
        node.isRoot = true;
        node.buildType = ActionNode.NONE;
        mActionNodeCache.add(node);
        mActionManager.executeActionOnPost(action, requestVal, callback, scheduler);
    }

    @Override
    public <T> void registerCallback(String actionID, Action.ActionCallback<T> callback) {
        mActionManager.registerCallback(actionID, callback);
    }

    @Override
    public IBuilder builder() {
        return new RetryActionBuilder(this);
    }

    @Override
    public <R extends Action.RequestValue, T> void post(Action<R, T> action, R requestVal, Action.ActionCallback<T> callback, long timeInMillis, ACTION_POST_POLICY policy) {
        mActionManager.post(action, requestVal, callback, timeInMillis, policy);
    }

    @Override
    public <T> void registerPostCallback(String actionID, Action.ActionCallback<T> callback) {
        mActionManager.registerPostCallback(actionID, callback);
    }

    public void retry() {
        List<ActionNode> temp = new ArrayList<>();
        for (ActionNode node : mActionNodeCache) {
            if (mCondition.needRetry(node)) {
                temp.add(node);
            }
        }

        for (ActionNode node : temp) {
            mActionNodeCache.remove(node);
        }

        mHandler.sendMessage(Message.obtain(mHandler, 1, temp));
    }

    public void handleFinished(ActionNode node) {
        if (!needToRetry) {
            removeActionNodeFromCache(node);
        } else {
            if (node.isFinishedAll()) {
                if (!mCondition.needRetry(node.findRoot())) {
                    removeActionNodeFromCache(node);
                }
            }
        }
    }

    public ActionManager getActionManager() {
        return mActionManager;
    }

    private void removeActionNodeFromCache(ActionNode node) {
        if (node.isFinishedAll()) {
            mActionNodeCache.remove(node.getRoot());
        }
    }

    private void doRelease() {
        mHandler.removeCallbacksAndMessages(null);
        for (ActionNode node : mActionNodeCache) {
            node.release();
        }
        mActionNodeCache.clear();
    }


    private static class RetryHandler extends Handler {

        IRebuildActionStrategy strategy;
        RetryActionManager manager;

        RetryHandler(RetryActionManager manager) {
            this.manager = manager;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            List<ActionNode> actionNodeList = (List<ActionNode>) msg.obj;
            for (ActionNode node : actionNodeList) {
                RetryActionBuilder retryActionBuilder = strategy.build(manager, node);
                ActionNode root = retryActionBuilder.getRoot();
                root.setActionStatus(Action.START);
                root.resetActionError();
                manager.mActionNodeCache.add(root);
                retryActionBuilder.run();
            }
        }
    }
}
