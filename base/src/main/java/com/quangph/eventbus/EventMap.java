package com.quangph.eventbus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pham Hai Quang on 3/21/2019.
 */
public abstract class EventMap {

    private List<EventInfo> mEventInfoSet = new ArrayList<>();

    public EventMap() {
        config();
    }

    public abstract void config();

    public <E extends IEvent> EventBuilder fire(Class<E> eventType) {
        EventBuilder builder = new EventBuilder();
        builder.eventType = eventType;
        return builder;
    }

    public void addEventMap(EventMap child) {
        mEventInfoSet.addAll(child.getEventInfoSet());
    }

    public List<EventInfo> getEventInfoSet() {
        return mEventInfoSet;
    }


    public class EventBuilder {
        Class<?> eventType;

        public <EH extends IEventHandler> EventBuilder toSubscriber(Class<EH> subscriberType) {
            return toSubscriber(subscriberType, 0);
        }

        public <EH extends IEventHandler> EventBuilder toSubscriber(Class<EH> subscriberType, int priority) {
            EventInfo info = new EventInfo();
            info.eventType = eventType;
            info.subscriberType = subscriberType;
            info.priority = priority;
            mEventInfoSet.add(info);
            return this;
        }
    }
}
