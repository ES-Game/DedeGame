package com.quangph.base.mvp.action.dispatcher;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import com.quangph.base.mvp.action.Action;
import com.quangph.base.mvp.action.ActionQueueInfo;
import com.quangph.base.mvp.action.RUN_MODE;
import com.quangph.base.mvp.action.queue.ActionEnqueue;

/**
 * Created by QuangPH on 2020-03-24.
 */
public class HandlerDispatcher implements IEnqueueDispatcher {

    private static final int EXECUTE = 1;

    private Handler mHandler = new EnqueueHandler(Looper.getMainLooper());
    @Override
    public void dispatch(ActionEnqueue actionEnqueue) {
        if (actionEnqueue == null) {
            throw new NullPointerException("Event is null");
        }
        ActionDispatcherInfo info = new ActionDispatcherInfo();
        info.what = EXECUTE;
        info.actionEnqueue = actionEnqueue;
        mHandler.sendMessage(mHandler.obtainMessage(EXECUTE, info));
    }


    private static class EnqueueHandler extends Handler {

        public EnqueueHandler(Looper mainLooper) {
            super(mainLooper);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == EXECUTE) {
                ActionDispatcherInfo event = (ActionDispatcherInfo) msg.obj;
                ActionEnqueue enqueue = event.actionEnqueue;
                RUN_MODE mode = enqueue.getRunMode();
                if (mode == RUN_MODE.SEQUENCE_MODE) {
                    ActionQueueInfo info = getNextAction(enqueue);
                    if (info != null) {
                        info.scheduler.execute(info.action);
                    }
                } else if (mode == RUN_MODE.PARALLEL_MODE) {
                    for (ActionQueueInfo acInfo : enqueue.getActionEnqueueInfoList()) {
                        acInfo.scheduler.execute(acInfo.action);
                    }
                }
            }
        }

        private ActionQueueInfo getNextAction(ActionEnqueue actionEnqueue) {
            for (ActionQueueInfo info : actionEnqueue.getActionEnqueueInfoList()) {
                if (info.action.getStatus() == Action.PENDING) {
                    return info;
                }
            }
            return null;
        }
    }

    static class ActionDispatcherInfo {
        int what;
        ActionEnqueue actionEnqueue;
    }
}
