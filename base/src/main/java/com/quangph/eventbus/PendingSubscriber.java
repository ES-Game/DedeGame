package com.quangph.eventbus;

import android.os.Handler;
import android.os.Message;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Pham Hai Quang on 3/21/2019.
 */
public class PendingSubscriber {
    public String id;
    public IEventHandler handler;
    public boolean active;

    private EventHandler mHandler;
    private List<PendingEventInfo> mPendingEventList = new ArrayList<>();
    private List<EventInfo> mHandledEventInfoList = new ArrayList<>();

    public PendingSubscriber() {
        mHandler = new EventHandler();
        mHandler.subscriber = this;
    }

    @Subscribe
    public void onEvent(IEvent event) {
        InternalEvent wrap = (InternalEvent) event;
        if (!canHandleEvent(wrap.getRootEvent())) {
            return;
        }

        if (active) {
            handler.onEvent(wrap.getRootEvent());
        } else {
            if (wrap.isPostDirect()) {
                handler.onEvent(wrap.getRootEvent());
            }
        }

    }

    public void addHandledEventInfo(EventInfo eventInfo) {
        mHandledEventInfoList.add(eventInfo);
    }

    /**
     * Get info of event
     * @param event
     * @return null if this subscriber can not handle the event
     */
    public EventInfo getEventInfoByEvent(IEvent event) {
        EventInfo eventInfo = null;
        for (EventInfo info : mHandledEventInfoList) {
            if (info.eventType.equals(event.getClass())) {
                eventInfo = info;
                break;
            }
        }

        return eventInfo;
    }

    public boolean canHandleEvent(IEvent event) {
        boolean canHandle = false;
        for (EventInfo info : mHandledEventInfoList) {
            if (info.eventType.equals(event.getClass())) {
                canHandle = true;
                break;
            }
        }
        return canHandle;
    }

    public void addPendingEvent(IEvent event, EventInfo eventInfo) {
        PendingEventInfo pendingEventInfo = new PendingEventInfo();
        pendingEventInfo.event = event;
        pendingEventInfo.priority = eventInfo.priority;
        addPendingEvent(pendingEventInfo);
    }

    public void addPendingEvent(PendingEventInfo pendingEvent) {
        mPendingEventList.add(pendingEvent);
        Collections.sort(mPendingEventList, new Comparator<PendingEventInfo>() {
            @Override
            public int compare(PendingEventInfo o1, PendingEventInfo o2) {
                return o2.priority - o1.priority;
            }
        });
    }

    public void firePendingEvent() {
        for (PendingEventInfo aMPendingEventList : mPendingEventList) {
            mHandler.sendMessage(mHandler.obtainMessage(EventHandler.EXE_EVENT, aMPendingEventList));
        }
    }

    public void removeAllPendingEvent() {
        mHandler.sendMessageAtFrontOfQueue(mHandler.obtainMessage(EventHandler.REMOVE_ALL_PENDING));
    }

    public void removePendingEvent(IEvent event) {
        mHandler.sendMessageAtFrontOfQueue(mHandler.obtainMessage(EventHandler.REMOVE_PENDING_EVENT, event));
    }

    public void release() {
        mHandler.removeCallbacksAndMessages(null);
    }


    public static class PendingEventInfo {
        IEvent event;
        int priority;
        boolean allowExecution = true;
    }


    private static class EventHandler extends Handler {
        static final int EXE_EVENT = 1;
        static final int REMOVE_ALL_PENDING = 2;
        static final int REMOVE_PENDING_EVENT = 3;

        PendingSubscriber subscriber;

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case EXE_EVENT:
                    PendingEventInfo info = (PendingEventInfo) msg.obj;
                    if (info.allowExecution) {
                        subscriber.onEvent(info.event);
                    }
                    subscriber.mPendingEventList.remove(info);
                    break;
                case REMOVE_ALL_PENDING:
                    notAllowExecution();
                    break;
                case REMOVE_PENDING_EVENT:
                    removeEvent((IEvent) msg.obj);
                    break;
            }
        }

        private void notAllowExecution() {
            for (PendingEventInfo info : subscriber.mPendingEventList) {
                // Because an event is stored in many subscriber, so we need notify for all subscribers
                // to not execute this event
                info.allowExecution = false;
            }
        }

        private void removeEvent(IEvent event) {
            Iterator<PendingEventInfo> itr = subscriber.mPendingEventList.iterator();
            while (itr.hasNext()) {
                PendingEventInfo next = itr.next();
                if (next.event instanceof InternalEvent) {
                    if (((InternalEvent) next.event).getRootEvent().equals(event)) {
                        next.allowExecution = false;
                        itr.remove();
                        break;
                    }
                }
            }
        }
    }
}
