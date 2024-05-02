package com.quangph.eventbus;

import org.greenrobot.eventbus.Subscribe;

/**
 * Created by Pham Hai Quang on 3/21/2019.
 */
public interface IEventHandler {
    @Subscribe
    void onEvent(IEvent event);
}
