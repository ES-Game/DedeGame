package com.quangph.base.mvp.action.dispatcher;

import com.quangph.base.dispatcher.BaseDispatcher;
import com.quangph.base.mvp.action.Action;
import com.quangph.base.mvp.action.ActionQueueInfo;
import com.quangph.base.mvp.action.RUN_MODE;
import com.quangph.base.mvp.action.queue.ActionEnqueue;

/**
 * Created by Pham Hai Quang on 6/21/2019.
 */
public class DispatcherImpl extends BaseDispatcher<DispatcherImpl.ActionDispatcherInfo> implements IEnqueueDispatcher {

    private static final int EXECUTE = 1;

    @Override
    public void dispatch(ActionEnqueue actionEnqueue) {
        if (actionEnqueue == null) {
            throw new NullPointerException("Event is null");
        }
        ActionDispatcherInfo info = new ActionDispatcherInfo();
        info.what = EXECUTE;
        info.actionEnqueue = actionEnqueue;
        dispatch(info);
    }

    private ActionQueueInfo getNextAction(ActionEnqueue actionEnqueue) {
        for (ActionQueueInfo info : actionEnqueue.getActionEnqueueInfoList()) {
            if (info.action.getStatus() == Action.PENDING) {
                return info;
            }
        }
        return null;
    }

    @Override
    public void handleEvent(ActionDispatcherInfo event) throws Exception {
        if (event.what == EXECUTE) {
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

    static class ActionDispatcherInfo {
        int what;
        ActionEnqueue actionEnqueue;
    }
}
