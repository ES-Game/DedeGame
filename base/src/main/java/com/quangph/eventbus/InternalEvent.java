package com.quangph.eventbus;

/**
 * Created by QuangPH on 2020-08-06.
 */
public class InternalEvent implements IEvent {

    private IEvent mRootEvent;
    private boolean isPostDirect;

    public InternalEvent(IEvent event, boolean postDirect) {
        mRootEvent = event;
        isPostDirect = postDirect;
    }

    public boolean isPostDirect() {
        return this.isPostDirect;
    }

    public IEvent getRootEvent() {
        return mRootEvent;
    }
}
