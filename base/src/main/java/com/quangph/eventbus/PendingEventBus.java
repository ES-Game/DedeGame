package com.quangph.eventbus;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pham Hai Quang on 3/20/2019.
 */
public class PendingEventBus {
    private static final String TAG = "PendingEventBus";

    private static PendingEventBus sInstance;
    private List<EventInfo> mEventInfoSet = new ArrayList<>();
    //private Map<String, List<EventInfo>> mSubscriberMap = new HashMap<>();
    private List<PendingSubscriber> mPendingSubscriberSet = new ArrayList<>();
    private BusHandler mHandler;

    public static PendingEventBus getInstance() {
        if (sInstance == null) {
            synchronized (PendingEventBus.class) {
                if (sInstance == null) {
                    sInstance = new PendingEventBus();
                }
            }
        }

        return sInstance;
    }

    private PendingEventBus() {
        mHandler = new BusHandler(this);
    }

    public void attachToMap(EventMap map) {
        mEventInfoSet = map.getEventInfoSet();
    }

    public void notifyActive(IEventHandler subscriber) {
        mHandler.sendMessage(mHandler.obtainMessage(BusHandler.NOTIFY_ACTIVE, subscriber));
    }

    public void notifyInactive(IEventHandler subscriber) {
        mHandler.sendMessage(mHandler.obtainMessage(BusHandler.NOTIFY_INACTIVE, subscriber));
    }

    public void register(IEventHandler subscriber) {
        register(getDefaultSubscriberID(subscriber.getClass()), subscriber);
    }

    public void register(String id, IEventHandler subscriber) {
        SubscriberInfo info = new SubscriberInfo();
        info.id = id;
        info.subscriber = subscriber;
        mHandler.sendMessage(mHandler.obtainMessage(BusHandler.REGISTER, info));
    }

    public void unregister(IEventHandler subscriber) {
        unregister(getDefaultSubscriberID(subscriber.getClass()), subscriber);
    }

    public void unregister(String id, IEventHandler subscriber) {
        SubscriberInfo info = new SubscriberInfo();
        info.id = id;
        info.subscriber = subscriber;
        mHandler.sendMessage(mHandler.obtainMessage(BusHandler.UNREGISTER, info));
    }

    public void post(IEvent event) {
        mHandler.sendMessage(mHandler.obtainMessage(BusHandler.POST, event));
    }

    public void postPending(IEvent event) {
        /*if (eventHasConfigInMap(event)) {
            IEvent wrap = new InternalEvent(event, false);
            mHandler.sendMessage(mHandler.obtainMessage(BusHandler.POST_PENDING, wrap));
            EventBus.getDefault().post(wrap);
        } else {
            Log.e(TAG, "Event has not config in map: " + event.getClass().getName());
        }*/

        mHandler.sendMessage(mHandler.obtainMessage(BusHandler.POST_PENDING, event));
    }

    public void clearAllPendingEventForSubscriber(IEventHandler subscirber) {
        mHandler.sendMessageAtFrontOfQueue(
                mHandler.obtainMessage(BusHandler.REMOVE_ALL_PENDING_EVENT, subscirber));
    }

    public void removeEvent(IEvent event) {
        mHandler.sendMessage(
                mHandler.obtainMessage(BusHandler.REMOVE_PENDING_EVENT, event));
    }

    public static String getDefaultSubscriberID(Class classType) {
        return classType.getName();
    }

    private boolean eventHasConfigInMap(IEvent event) {
        boolean hasConfig = false;
        for (EventInfo ei : mEventInfoSet) {
            if (ei.eventType.equals(event.getClass())) {
                hasConfig = true;
                break;
            }
        }

        return hasConfig;
    }


    private static class BusHandler extends Handler {
        static final int NOTIFY_ACTIVE = 1;
        static final int NOTIFY_INACTIVE = 2;
        static final int REGISTER = 3;
        static final int UNREGISTER = 4;
        static final int POST = 5;
        static final int POST_PENDING = 6;
        static final int REMOVE_ALL_PENDING_EVENT = 7;
        static final int REMOVE_PENDING_EVENT = 8;

        PendingEventBus bus;

        BusHandler(PendingEventBus bus) {
            this.bus = bus;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case NOTIFY_ACTIVE:
                    notifyActive((IEventHandler) msg.obj);
                    break;
                case NOTIFY_INACTIVE:
                    notifyInactive((IEventHandler) msg.obj);
                    break;
                case REGISTER:
                    register((SubscriberInfo) msg.obj);
                    break;
                case UNREGISTER:
                    unregister((SubscriberInfo) msg.obj);
                    break;
                case POST:
                    post((IEvent) msg.obj);
                    break;
                case POST_PENDING:
                    //addPendingEventToPendingSubscriber((IEvent) msg.obj);
                    postPending((IEvent) msg.obj);
                    break;
                case REMOVE_ALL_PENDING_EVENT:
                    removeAllPendingEventOfSubscriber((IEventHandler) msg.obj);
                    break;
                case REMOVE_PENDING_EVENT:
                    removePendingEvent((IEvent) msg.obj);
                    break;
            }
        }

        void notifyActive(IEventHandler subscriber) {
            for (PendingSubscriber wrap : bus.mPendingSubscriberSet) {
                if (wrap.handler == subscriber) {
                    wrap.active = true;
                    wrap.firePendingEvent();
                    break;
                }
            }
        }

        void notifyInactive(IEventHandler subscriber) {
            for (PendingSubscriber wrap : bus.mPendingSubscriberSet) {
                if (wrap.handler == subscriber) {
                    wrap.active = false;
                    break;
                }
            }
        }

        void register(SubscriberInfo subscriberInfo) {
            /*List<EventInfo> eventListOfSubscriber = bus.mSubscriberMap.get(subscriberInfo.id);
            if (eventListOfSubscriber != null) {
                Log.e(TAG, "Subscriber has been registered: " + subscriberInfo.id);
                return;
            }
            eventListOfSubscriber = new ArrayList<>();
            List<EventInfo> eventInfoSet = bus.mEventInfoSet;
            for (EventInfo info : eventInfoSet) {
                if (info.subscriberType.equals(subscriberInfo.subscriber.getClass())) {
                    eventListOfSubscriber.add(info);
                }
            }
            bus.mSubscriberMap.put(subscriberInfo.id, eventListOfSubscriber);*/

            for (PendingSubscriber sub : bus.mPendingSubscriberSet) {
                if (sub.id.equals(subscriberInfo.id)) {
                    Log.e(TAG, "Subscriber has been registered: " + subscriberInfo.id);
                    return;
                }
            }

            PendingSubscriber pendingSubscriber = new PendingSubscriber();
            pendingSubscriber.id = subscriberInfo.id;
            pendingSubscriber.handler = subscriberInfo.subscriber;
            for (EventInfo info : bus.mEventInfoSet) {
                if (info.subscriberType.equals(subscriberInfo.subscriber.getClass())) {
                    pendingSubscriber.addHandledEventInfo(info);
                }
            }
            bus.mPendingSubscriberSet.add(pendingSubscriber);
            EventBus.getDefault().register(pendingSubscriber);
        }

        void unregister(SubscriberInfo subscriberInfo) {
            //bus.mSubscriberMap.remove(subscriberInfo.id);

            PendingSubscriber pendingSubscriber = null;
            for (PendingSubscriber wrap : bus.mPendingSubscriberSet) {
                if (wrap.handler == subscriberInfo.subscriber) {
                    pendingSubscriber = wrap;
                    break;
                }
            }
            if (pendingSubscriber != null) {
                pendingSubscriber.release();
                EventBus.getDefault().unregister(pendingSubscriber);
                bus.mPendingSubscriberSet.remove(pendingSubscriber);
            }

            /*for (PendingSubscriber sub : bus.mPendingSubscriberSet) {
                Log.e("PendingBus", "After unregister: " + sub.id);
            }*/
        }

        void addPendingEventToPendingSubscriber(IEvent event) {
            for (PendingSubscriber pending : bus.mPendingSubscriberSet) {
                if (!pending.active) {
                    EventInfo eventInfo = pending.getEventInfoByEvent(((InternalEvent) event).getRootEvent());//getEventInfoForEvent(pending, (InternalEvent) event);
                    if (eventInfo != null) {
                        pending.addPendingEvent(event, eventInfo);
                    }
                }
            }
        }

        void post(IEvent event) {
            if (bus.eventHasConfigInMap(event)) {
                EventBus.getDefault().post(new InternalEvent(event, true));
            } else {
                Log.e(TAG, "Event has not config in map: " + event.getClass().getName());
            }
        }

        void postPending(IEvent event) {
            if (bus.eventHasConfigInMap(event)) {
                IEvent wrap = new InternalEvent(event, false);
                addPendingEventToPendingSubscriber(wrap);
                EventBus.getDefault().post(wrap);
            } else {
                Log.e(TAG, "Event has not config in map: " + event.getClass().getName());
            }
        }

        void removeAllPendingEventOfSubscriber(IEventHandler subscriber) {
            for(PendingSubscriber pending : bus.mPendingSubscriberSet) {
                if (pending.handler == subscriber) {
                    pending.removeAllPendingEvent();
                }
            }
        }

        void removePendingEvent(IEvent event) {
            for(PendingSubscriber pending : bus.mPendingSubscriberSet) {
                pending.removePendingEvent(event);
            }
        }
    }

    private class SubscriberInfo {
        IEventHandler subscriber;
        String id;
    }
}
